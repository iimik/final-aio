package org.ifinalframework.plugins.aio.mybatis.contributor

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiEnumConstant
import com.intellij.psi.PsiModifier
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.PsiType
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.xml.XmlToken
import com.intellij.util.PlatformIcons
import com.intellij.util.ProcessingContext
import com.intellij.util.xml.DomUtil
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.mybatis.xml.dom.*
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.service.PsiService
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.swing.Icon

private const val TEST_COMPLETION_PLACE_HOLDER = "\${TARGET}"

/**
 * Mapper Xml 自动补全提示
 *
 * @issue 33
 * @author iimik
 */
class MapperXmlCompletionContributor : AbsMapperCompletionContributor() {

    val docService = service<DocService>()

    init {
        statementIdCompletion()
        resultMapCompletion()
        propertyCompletion()
        foreachCompletion()
        jdbcTypeCompletion()
        includeRefidCompletion()
        testPropertyCompletion()
    }


    /**
     * 自动补全`statement`的`id`属性，其中`id`为`namespace`指定的Mapper接口中方法。
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
                            val mapper = MyBatisUtils.getMapper(statement)
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
                            val mapper = MyBatisUtils.getMapper(domElement)

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
     * @see [jdbcTypeCompletion]
     */
    private fun propertyCompletion() {

        val property = XmlPatterns.psiElement()
            .inside(
                XmlPatterns.xmlAttributeValue()
                    .inside(XmlPatterns.xmlAttribute().withName("property"))
            )

        Stream.of(property)
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

    private fun foreachCompletion() {

        val collection = XmlPatterns.psiElement()
            .inside(
                XmlPatterns.xmlAttributeValue()
                    .inside(XmlPatterns.xmlAttribute().withName("collection"))
            )

        Stream.of(collection)
            .forEach { place ->

                thisLogger().info("foreachCollectionCompletion: $place")

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
                            val statement = if (domElement is Statement) domElement else DomUtil.getParentOfType(
                                domElement,
                                Statement::class.java,
                                true
                            ) ?: return

                            val method = statement.getId().value ?: return
                            val params = method.parameterList.parameters
                            if (params.size == 1) {
                                val type = params[0].type
                                processParamForCollection(null, type, result, PlatformIcons.PARAMETER_ICON, position.project)
                            } else {
                                params.forEach { param ->
                                    val prefix = param.name
                                    val type = param.type
                                    processParamForCollection(prefix, type, result, PlatformIcons.PARAMETER_ICON, position.project)
                                }
                            }

                            result.stopHere()

                        }
                    })
            }


        val map = mutableMapOf<ElementPattern<out PsiElement>,List<String>>()
        map[XmlPatterns.psiElement()
                    .inside(
                        XmlPatterns.xmlAttributeValue()
                            .inside(XmlPatterns.xmlAttribute().withName("open"))
                    )] = listOf("(")
        map[XmlPatterns.psiElement()
            .inside(
                XmlPatterns.xmlAttributeValue()
                    .inside(XmlPatterns.xmlAttribute().withName("close"))
            )] = listOf(")")
        map[XmlPatterns.psiElement()
            .inside(
                XmlPatterns.xmlAttributeValue()
                    .inside(XmlPatterns.xmlAttribute().withName("separator"))
            )] = listOf(",")


        map[XmlPatterns.psiElement()
            .inside(
                XmlPatterns.xmlAttributeValue()
                    .inside(XmlPatterns.xmlAttribute().withName("item"))
            )] = listOf("item","entry")

        map.forEach { place, tips ->
                thisLogger().info("foreachCollectionCompletion: $place")

                extend(
                    CompletionType.BASIC,
                    place,
                    object : CompletionProvider<CompletionParameters>() {
                        override fun addCompletions(
                            parameters: CompletionParameters,
                            context: ProcessingContext,
                            result: CompletionResultSet
                        ) {

                            tips.forEach { tip ->
                                result.addElement(
                                    LookupElementBuilder.create(tip)
                                        .withCaseSensitivity(false)
                                )
                            }

                            result.stopHere()

                        }
                    })
            }
    }

    private fun processParamForCollection(prefix: String?, type: PsiType, result: CompletionResultSet, icon: Icon, project: Project) {

        if (type is PsiClassReferenceType) {
            val className = type.reference!!.qualifiedName
            if (className.startsWith("java.lang.") || className.startsWith("java.util.")) {
                val name = prefix ?: "value"
                result.addElement(
                    LookupElementBuilder.create(name)
                        .withTypeText(type.presentableText)
                        .withIcon(icon)
                        .withCaseSensitivity(false)
                )
            } else {
                val clazz = project.service<PsiService>().findClass(className) ?: return
                clazz.allFields
                    .filter { !it.hasModifierProperty(PsiModifier.STATIC) }
                    .filter { it.type is PsiClassReferenceType && (it.type as PsiClassReferenceType).reference!!.qualifiedName.startsWith("java.util.") }
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
            }
        }
    }


    /**
     * 自动补全`jdbcType`
     *
     * ```xml
     * <id jdbcType=""/>
     * <result jdbcType=""/>
     * ```
     * @see [propertyCompletion]
     */
    private fun jdbcTypeCompletion() {

        val jdbcType = XmlPatterns.psiElement()
            .inside(
                XmlPatterns.xmlAttributeValue()
                    .inside(XmlPatterns.xmlAttribute().withName("jdbcType"))
            )

        Stream.of(jdbcType)
            .forEach { place ->

                thisLogger().info("jdbcTypeCompletion: $place")

                extend(
                    CompletionType.BASIC,
                    place,
                    object : CompletionProvider<CompletionParameters>() {
                        override fun addCompletions(
                            parameters: CompletionParameters,
                            context: ProcessingContext,
                            result: CompletionResultSet
                        ) {

                            val project = parameters.position.project
                            val jdbcTypeClass = project.service<PsiService>().findClass("org.apache.ibatis.type.JdbcType") ?: return

                            val jdbcTypes = jdbcTypeClass.allFields
                                .filterIsInstance<PsiEnumConstant>()
                                .map { it.name }

                            jdbcTypes.forEach { jdbcType ->
                                result.addElement(
                                    LookupElementBuilder.create(jdbcType)
                                        .withIcon(AllIcons.Mybatis.JVM)
                                        .withCaseSensitivity(false)
                                )
                            }
                            result.stopHere()

                        }
                    })
            }
    }

    /**
     * `include`标签`refid`使用`sql.id`补全提示
     * ```xml
     * <include refid="{sqlId}"/>
     * ```
     */
    private fun includeRefidCompletion() {
        val includeRefid = XmlPatterns.psiElement().inside(
            XmlPatterns.xmlAttribute().withName("refid").inside(XmlPatterns.xmlTag().withName("include"))
        )

        thisLogger().info("resultMapPropertyCompletion: $includeRefid")

        extend(
            CompletionType.BASIC,
            includeRefid,
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    if (position !is XmlToken) return
                    val domElement = DomUtil.getDomElement(position) ?: return
                    val mapper = MyBatisUtils.getMapper(domElement)

                    val self = DomUtil.getParentOfType(domElement, Sql::class.java, true)

                    mapper.getSqls()
                        // 排除自己
                        .filter { self == null || self.getId().rawText != it.getId().rawText }
                        .forEach { sql ->
                            result.addElement(
                                LookupElementBuilder.create(sql.getId().rawText!!)
                                    .withIcon(AllIcons.Mybatis.XML)
                                    .withCaseSensitivity(false)
                            )
                        }
                    result.stopHere()
                }
            })
    }

    /**
     * ```xml
     * <if test=""/>
     * <when test=""/>
     * ```
     */
    private fun testPropertyCompletion() {
        val test = XmlPatterns.psiElement()
            .inside(
                XmlPatterns.xmlAttributeValue()
                    .inside(XmlPatterns.xmlAttribute().withName("test"))
            )

        Stream.of(test)
            .forEach { place ->

                thisLogger().info("testPropertyCompletion: $place")

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
                            val statement = if (domElement is Statement) domElement else DomUtil.getParentOfType(
                                domElement,
                                Statement::class.java,
                                true
                            ) ?: return

                            val method = statement.getId().value ?: return
                            val params = method.parameterList.parameters
                            if (params.size == 1) {
                                val type = params[0].type
                                processParam(null, type, result, PlatformIcons.PARAMETER_ICON, position.project)
                            } else {
                                params.forEach { param ->
                                    val prefix = param.name
                                    val type = param.type
                                    processParam(prefix, type, result, PlatformIcons.PARAMETER_ICON, position.project)
                                }
                            }

                            result.stopHere()
                        }
                    })
            }
    }

    private fun processParam(prefix: String?, type: PsiType, result: CompletionResultSet, icon: Icon, project: Project) {

        val testCompletion = project.service<MyBatisProperties>().testCompletion

        if (type is PsiClassReferenceType) {
            val className = type.reference!!.qualifiedName
            if (className.startsWith("java.lang.") || className.startsWith("java.util.")) {
                val name = prefix ?: "value"
                result.addElement(
                    LookupElementBuilder.create(buildTest(null, type, name, testCompletion))
                        .withIcon(icon)
                        .withCaseSensitivity(false)
                )
            } else {
                val clazz = project.service<PsiService>().findClass(className) ?: return
                clazz.allFields
                    .filter { !it.hasModifierProperty(PsiModifier.STATIC) }
                    .forEach { field ->
                        var typeText = field.type.presentableText

                        docService.getSummary(field)?.let { typeText = "$it ($typeText)" }

                        result.addElement(
                            LookupElementBuilder.create(buildTest(prefix, field.type, field.name, testCompletion))
                                .withIcon(PlatformIcons.PROPERTY_ICON)
                                .withTypeText(typeText)
                                .withCaseSensitivity(false)
                        )
                    }
            }
        } else if (type is PsiPrimitiveType) {
            val name = prefix ?: "value"
            result.addElement(
                LookupElementBuilder.create(buildTest(null, type, name, testCompletion))
                    .withIcon(icon)
                    .withCaseSensitivity(false)
            )
        }
    }


    private fun buildTest(prefix: String? = null, type: PsiType, name: String, testCompletion: MyBatisProperties.TestCompletion): String {

        val param = Stream.of(prefix, name).filter { Objects.nonNull(it) }.collect(Collectors.joining("."))

        if (type is PsiClassReferenceType) {
            val className = type.reference!!.qualifiedName
            if ("java.lang.String" == className) {
                return testCompletion.stringType.replace(TEST_COMPLETION_PLACE_HOLDER, param)
            } else if ("java.util.List" == className || "java.util.Set" == className) {
                return testCompletion.collectionType.replace(TEST_COMPLETION_PLACE_HOLDER, param)
            }
        }

        return testCompletion.defaultType.replace(TEST_COMPLETION_PLACE_HOLDER, param)
    }


}