package org.ifinalframework.plugins.aio.mybatis.contributor

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.xml.XmlToken
import com.intellij.util.PlatformIcons
import com.intellij.util.ProcessingContext
import com.intellij.util.xml.DomUtil
import org.ifinalframework.plugins.aio.mybatis.MapperUtils
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.mybatis.xml.dom.*
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.util.SpiUtil
import java.util.stream.Stream

/**
 * MapperXmlCompletionContributor
 *
 * @issue 33
 * @author iimik
 */
class MapperXmlCompletionContributor : AbsMapperCompletionContributor() {

    val docService = SpiUtil.languageSpi<DocService>()

    init {
        statementIdCompletion()
        resultMapCompletion()
        resultMapPropertyCompletion()
    }

    /**
     * 自动补全`statement`的`id`属性
     *
     * ```xml
     * <insert id="insert"/>
     * <update id="update"/>
     * <delete id="delete"/>
     * <select id="select"/>
     * ```
     *
     * @issue 33
     */
    private fun statementIdCompletion() {
        Stream.of(Insert::class, Delete::class, Update::class, Select::class)
            .map { it.simpleName!!.lowercase() }
            .forEach { name ->

                thisLogger().info("extendStatements: $name")

                extend(
                    CompletionType.BASIC,
                    XmlPatterns.psiElement()
                        .inside(XmlPatterns.xmlAttribute().withName("id").inside(XmlPatterns.xmlTag().withName(name))),
                    object : CompletionProvider<CompletionParameters>() {
                        override fun addCompletions(
                            parameters: CompletionParameters,
                            context: ProcessingContext,
                            result: CompletionResultSet
                        ) {
                            val position = parameters.position
                            if (position !is XmlToken) return
                            val domElement = DomUtil.getDomElement(position) ?: return
                            val statement = DomUtil.getParentOfType(domElement, Statement::class.java, true) ?: return
                            val mapper = MapperUtils.getMapper(statement)
                            val className = mapper.getNamespace().rawText ?: return

                            position.project.service<MapperService>().findStatements(className)
                                .forEach { method ->
                                    var summary = docService.getSummary(method)

                                    result.addElement(
                                        LookupElementBuilder.create(method.name)
                                            .withIcon(PlatformIcons.METHOD_ICON)
                                            .withTypeText(summary)
                                            .withCaseSensitivity(false)
                                    )
                                }

                            result.stopHere()
                        }
                    })
            }
    }

    /**
     * ```xml
     * <select resultMap=""/>
     * <resultMap extends=""/>
     * ```
     *
     * Note: 需要排除自身
     */
    private fun resultMapCompletion() {
        val selectResultMap = XmlPatterns.psiElement().inside(
            XmlPatterns.xmlAttribute().withName("resultMap").inside(XmlPatterns.xmlTag().withName("select"))
        )

        val resultMapExtends = XmlPatterns.psiElement().inside(
            XmlPatterns.xmlAttribute().withName("extends").inside(XmlPatterns.xmlTag().withName("resultMap"))
        )

        Stream.of(selectResultMap, resultMapExtends)
            .forEach { place ->

                thisLogger().info("resultMapCompletion: $place")

                extend(
                    CompletionType.BASIC,
                    place,
                    object : CompletionProvider<CompletionParameters>() {
                        override fun addCompletions(
                            parameters: CompletionParameters,
                            context: ProcessingContext,
                            result: CompletionResultSet
                        ) {
                            val position = parameters.position
                            if (position !is XmlToken) return
                            val domElement = DomUtil.getDomElement(position) ?: return
                            val mapper = MapperUtils.getMapper(domElement)

                            val self = DomUtil.getParentOfType(domElement, ResultMap::class.java, true)

                            mapper.getResultMaps()
                                // 排除自己
                                .filter { self == null || self.getId().rawText != it.getId().rawText }
                                .forEach { resultMap ->
                                    result.addElement(
                                        LookupElementBuilder.create(resultMap.getId().rawText!!)
                                            .withIcon(AllIcons.Mybatis.XML)
                                            .withCaseSensitivity(false)
                                    )
                                }
                            result.stopHere()
                        }
                    })
            }

    }

    /**
     * ```xml
     * <id property=""/>
     * <result property=""/>
     * ```
     */
    private fun resultMapPropertyCompletion() {
        val idProperty = XmlPatterns.psiElement().inside(
            XmlPatterns.xmlAttribute().withName("property").inside(XmlPatterns.xmlTag().withName("id"))
        )
        val resultProperty = XmlPatterns.psiElement().inside(
            XmlPatterns.xmlAttribute().withName("property").inside(XmlPatterns.xmlTag().withName("result"))
        )

        Stream.of(idProperty, resultProperty)
            .forEach { place ->

                thisLogger().info("resultMapPropertyCompletion: $place")

                extend(
                    CompletionType.BASIC,
                    place,
                    object : CompletionProvider<CompletionParameters>() {
                        override fun addCompletions(
                            parameters: CompletionParameters,
                            context: ProcessingContext,
                            result: CompletionResultSet
                        ) {
                            val position = parameters.position
                            if (position !is XmlToken) return
                            val domElement = DomUtil.getDomElement(position) ?: return
                            val property = DomUtil.getParentOfType(domElement, ResultMap.Property::class.java, true) ?: return
                            val parent = property.parent ?: return

                            val clazz = when (parent) {
                                is ResultMap -> parent.getType().value
                                else -> null
                            } ?: return

                            clazz.allFields
                                .forEach { field ->

                                    var typeText = field.type.presentableText

                                    docService.getSummary(field)?.let { typeText = "$it ($typeText)" }

                                    result.addElement(
                                        LookupElementBuilder.create(field.name)
                                            .withIcon(PlatformIcons.PROPERTY_ICON)
                                            .withTypeText(typeText)
                                            .withCaseSensitivity(false)
                                    )
                                }
                            result.stopHere()
                        }
                    })
            }
    }


}