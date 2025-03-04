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
import org.ifinalframework.plugins.aio.resource.AllIcons
import java.util.stream.Stream

/**
 * MapperXmlCompletionContributor
 *
 * @issue 33
 * @author iimik
 */
class MapperXmlCompletionContributor : AbsMapperCompletionContributor() {

    init {
        statementIdCompletion()
        resultMapCompletion()
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
                                    result.addElement(LookupElementBuilder.create(method.name).withIcon(PlatformIcons.METHOD_ICON))
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

                            mapper.getResultMaps()
                                .forEach { resultMap ->
                                    result.addElement(LookupElementBuilder.create(resultMap.getId().rawText!!).withIcon(AllIcons.Mybatis.XML))
                                }
                            result.stopHere()
                        }
                    })
            }

    }


}