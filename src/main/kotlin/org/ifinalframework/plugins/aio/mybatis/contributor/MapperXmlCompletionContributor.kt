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
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.xml.XmlToken
import com.intellij.util.PlatformIcons
import com.intellij.util.ProcessingContext
import com.intellij.util.xml.DomUtil
import org.ifinalframework.plugins.aio.datasource.service.DataSourceService
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.mybatis.xml.MapperUtils
import org.ifinalframework.plugins.aio.mybatis.xml.dom.*
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.service.PsiService
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

private const val TEST_COMPLETION_PLACE_HOLDER = "\${TARGET}"
private const val TEST_COMPLETION_START_PLACE_HOLDER = "\${START_TARGET}"
private const val TEST_COMPLETION_END_PLACE_HOLDER = "\${END_TARGET}"

/**
 * Mapper Xml 自动补全提示
 *
 * ## Statement
 *
 * Statement.id属性使用对应的mapper中声明的方法进行补全
 *
 * ## ResultMap
 *
 * 对引用了`<resultMap>`的属性进行补全，支持以下属性：
 *
 * - `<resultMap extends="{resultMap.id}"`
 * - `<select resultMap="{resultMap.id}"/>`
 *
 * 对`<resultMap>`子标签<id>和<result>中的`property`、`column`、`jdbcType`等属性进行补全：
 *
 * - `<id column="{column}" property="{property}" jdbcType="{jdbcType}"/>`
 * - `<result column="{column}" property="{property}" jdbcType="{jdbcType}"/>`
 *
 * ## Include
 *
 * 对`<include>`标签的`refId`属性进行补全：
 *
 * - `<include refId="{sql.id}"`/>
 *
 * ## Test
 *
 * 对`test`属性标间补全：
 *
 * - `<if test="{test}"/>`
 * - `<when test="{test}"/>`
 *
 * @issue 33
 * @author iimik
 */
class MapperXmlCompletionContributor : AbsMapperCompletionContributor() {

    val docService = service<DocService>()

    init {
        statementIdCompletion()
        resultMapReferenceCompletion()
        resultMapColumnCompletion()
        resultMapPropertyCompletion()
        foreachCompletion()
        jdbcTypeCompletion()
        includeRefidCompletion()
        testPropertyCompletion()
        sqlIdCompletion()
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
                            val statementMethodCompletion =
                                position.project.getService(MyBatisProperties::class.java).statementMethodCompletion
                            val domElement = DomUtil.getDomElement(position) ?: return
                            val statement = DomUtil.getParentOfType(domElement, Statement::class.java, true) ?: return
                            val mapper = MyBatisUtils.getMapper(statement)
                            val className = mapper.getNamespace().rawText ?: return

                            val regex = if (statementMethodCompletion.filterWithRegex) {
                                val value = when (statement.xmlTag!!.name) {
                                    "insert" -> statementMethodCompletion.insertMethodRegex
                                    "delete" -> statementMethodCompletion.deleteMethodRegex
                                    "update" -> statementMethodCompletion.updateMethodRegex
                                    "select" -> statementMethodCompletion.selectMethodRegex
                                    else -> throw RuntimeException()
                                }

                                Regex(value)

                            } else null

                            position.project.service<MapperService>().findStatements(className)
                                .filter {

                                    return@filter if (regex != null) {
                                        it.name.matches(regex)
                                    } else true

                                }
                                .forEach { method ->
                                    val summary = docService.getSummary(method)

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
    private fun resultMapReferenceCompletion() {
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
     * <id column=""/>
     * <result column=""/>
     * ```
     */
    private fun resultMapColumnCompletion() {
        val column = XmlPatterns.psiElement()
            .inside(
                XmlPatterns.xmlAttributeValue()
                    .inside(XmlPatterns.xmlAttribute().withName("column"))
            )

        Stream.of(column)
            .forEach { place ->

                thisLogger().info("columnCompletion: $place")

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

                            val project = position.project
                            val tableName = MapperUtils.getTableName(project, property)
                            val dataSourceService = project.service<DataSourceService>()
                            val myBatisProperties = project.service<MyBatisProperties>()
                            val tables = dataSourceService.getTables(myBatisProperties.tableSqlFragment.prefix)
                            if (tables.isEmpty()) {
                                return
                            }

                            val matchTables = tables.filter { tableName == it.logicTable }.toList()
                            if (matchTables.isNotEmpty()) {
                                // 找到了匹配的表
                                val table = matchTables.get(0).actualTables[0]

                                table.columns.forEach { column ->
                                    result.addElement(
                                        LookupElementBuilder.create(column.name)
                                            .withTypeText("${column.dasType.toDataType()}(${table.comment})")
                                    )
                                }

                                result.stopHere()
                            }

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
    private fun resultMapPropertyCompletion() {

        val property = XmlPatterns.psiElement()
            .inside(
                XmlPatterns.xmlAttributeValue()
                    .inside(XmlPatterns.xmlAttribute().withName("property"))
            )

        Stream.of(property)
            .forEach { place ->

                thisLogger().info("propertyCompletion: $place")

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

                thisLogger().info("foreachCompletion: $place")

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
                                processParamForCollection(null, type, result, position.project)
                            } else {
                                params.forEach { param ->
                                    val prefix = param.name
                                    val type = param.type
                                    processParamForCollection(prefix, type, result, position.project)
                                }
                            }

                            result.stopHere()

                        }
                    })
            }


        val map = mutableMapOf<ElementPattern<out PsiElement>, List<String>>()
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
            )] = listOf("item", "entry")

        map.forEach { (place, tips) ->
            thisLogger().info("foreachCompletion: $place")

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

    private fun processParamForCollection(prefix: String?, type: PsiType, result: CompletionResultSet, project: Project) {

        if (type is PsiClassReferenceType) {
            val className = type.reference.qualifiedName
            if (className.startsWith("java.lang.") || className.startsWith("java.util.")) {
                val name = prefix ?: "value"
                result.addElement(
                    LookupElementBuilder.create(name)
                        .withTypeText(type.presentableText)
                        .withIcon(PlatformIcons.PARAMETER_ICON)
                        .withCaseSensitivity(false)
                )
            } else {
                val clazz = project.service<PsiService>().findClass(className) ?: return
                clazz.allFields
                    .filter { !it.hasModifierProperty(PsiModifier.STATIC) }
                    .filter { it.type is PsiClassReferenceType && (it.type as PsiClassReferenceType).reference.qualifiedName.startsWith("java.util.") }
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
     * @see [resultMapPropertyCompletion]
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
     * SQL片段`id`属性补全
     *
     * 从以下配置中读取：
     * - 表：[MyBatisProperties.TableSqlFragment.ids]
     * - 列：[MyBatisProperties.ColumnSqlFragment.ids]
     */
    private fun sqlIdCompletion() {
        val sqlId = XmlPatterns.psiElement().inside(
            XmlPatterns.xmlAttribute().withName("id").inside(XmlPatterns.xmlTag().withName("sql"))
        )

        thisLogger().info("sqlIdCompletion: $sqlId")

        extend(
            CompletionType.BASIC,
            sqlId,
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext, result: CompletionResultSet
                ) {
                    val position = parameters.position
                    if (position !is XmlToken) return
                    val domElement = DomUtil.getDomElement(position) ?: return

                    val myBatisProperties = position.project.service<MyBatisProperties>()
                    myBatisProperties.tableSqlFragment.ids.split(",")
                        .forEach { id ->
                            result.addElement(LookupElementBuilder.create(id))
                        }

                    myBatisProperties.columnSqlFragment.ids.split(",")
                        .forEach { id -> result.addElement(LookupElementBuilder.create(id)) }

                    result.stopHere()
                }
            })
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

        thisLogger().info("includeRefidCompletion: $includeRefid")

        extend(
            CompletionType.BASIC,
            includeRefid,
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext, result: CompletionResultSet
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
                                processParam(null, type, result, position.project)
                            } else {
                                params.forEach { param ->
                                    val prefix = param.name
                                    val type = param.type
                                    processParam(prefix, type, result, position.project)
                                }
                            }

                            result.stopHere()
                        }
                    })
            }
    }

    private fun processParam(prefix: String?, type: PsiType, result: CompletionResultSet, project: Project) {

        val testCompletion = project.service<MyBatisProperties>().testCompletion

        if (type is PsiClassReferenceType) {
            val className = type.reference.qualifiedName
            if (className.startsWith("java.lang.") || className.startsWith("java.util.")) {
                val name = prefix ?: "value"
                result.addElement(
                    LookupElementBuilder.create(buildTest(null, type, name, testCompletion))
                        .withIcon(PlatformIcons.PARAMETER_ICON)
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

                        // 扩展区间补全
                        // 如果属性名为 startXXX，且能找到endXXX，则生成一个between参数的校验
                        if (field.name.startsWith("start")) {
                            val endName = field.name.replace("start", "end")
                            val endField = clazz.findFieldByName(endName, true)
                            if (endField != null) {

                                val betweenTest = testCompletion.betweenType.replace(TEST_COMPLETION_START_PLACE_HOLDER, field.name)
                                    .replace(TEST_COMPLETION_END_PLACE_HOLDER, endField.name)

                                var endTypeText = endField.type.presentableText

                                docService.getSummary(endField)?.let { endTypeText = "$it ($endTypeText)" }

                                result.addElement(
                                    LookupElementBuilder.create(betweenTest)
                                        .withIcon(PlatformIcons.PROPERTY_ICON)
                                        .withTypeText("$typeText and $endTypeText")
                                        .withCaseSensitivity(false)
                                )
                            }
                        }
                    }
            }
        } else if (type is PsiPrimitiveType) {
            val name = prefix ?: "value"
            result.addElement(
                LookupElementBuilder.create(buildTest(null, type, name, testCompletion))
                    .withIcon(PlatformIcons.PARAMETER_ICON)
                    .withCaseSensitivity(false)
            )
        }
    }


    private fun buildTest(prefix: String? = null, type: PsiType, name: String, testCompletion: MyBatisProperties.TestCompletion): String {

        val param = Stream.of(prefix, name).filter { Objects.nonNull(it) }.collect(Collectors.joining("."))

        if (type is PsiClassReferenceType) {
            val className = type.reference.qualifiedName
            if ("java.lang.String" == className) {
                return testCompletion.stringType.replace(TEST_COMPLETION_PLACE_HOLDER, param)
            } else if ("java.util.List" == className || "java.util.Set" == className) {
                return testCompletion.collectionType.replace(TEST_COMPLETION_PLACE_HOLDER, param)
            }
        }

        return testCompletion.defaultType.replace(TEST_COMPLETION_PLACE_HOLDER, param)
    }


}