package org.ifinalframework.plugins.aio.mybatis

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * MyBatisProperties
 *
 * @author iimik
 */
@Service(Service.Level.PROJECT)
@State(
    name = "org.ifinalframework.plugins.aio.mybatis.MyBatisConfig",
    storages = [Storage("final-aio-mybatis.xml")]
)
data class MyBatisProperties(
    var mapperXmlPath: MapperXmlPath = MapperXmlPath.RESOURCE,
    var tableSqlFragment: TableSqlFragment = TableSqlFragment(),
    var resultMapInspection: Boolean = true,
    var statementMethodCompletion: StatementMethodCompletion = StatementMethodCompletion(),
    var testCompletion: TestCompletion = TestCompletion(),
    var lineMarker: LineMarker = LineMarker(),
) : PersistentStateComponent<MyBatisProperties> {
    override fun getState(): MyBatisProperties? {
        return this
    }

    override fun loadState(properties: MyBatisProperties) {
        XmlSerializerUtil.copyBean<MyBatisProperties>(properties, this)
    }

    data class StatementMethodCompletion(
        var insertMethodRegex: String = "^(insert|add|create|new)+\\w*\$",
        var deleteMethodRegex: String = "^(del|remove|clean)+\\w*\$",
        var updateMethodRegex: String = "^(update|set)+\\w*\$",
        var selectMethodRegex: String = "^(select|find|get|query|count)+\\w*\$",
        var filterWithRegex: Boolean = true
    )

    /**
     * Test属性补全配置
     * ```xml
     * <if test=""/>
     * <when test=""/>
     * ```
     */
    data class TestCompletion(
        /**
         * 字符串
         */
        var stringType: String = "null != \${TARGET} and \${TARGET} != ''",
        /**
         * 集合
         */
        var collectionType: String = "null != \${TARGET} and \${TARGET}.size > 0",
        /**
         * 区间
         */
        var betweenType: String = "null != \${START_TARGET} and null != \${END_TARGET}",
        /**
         * 默认
         */
        var defaultType: String = "null != \${TARGET}",
    )

    /**
     * <sql id="${id}">
     *
     * </sql>
     *
     */
    data class TableSqlFragment(
        var enable: Boolean = true,
        var id: String = "table",
        var prefix: String = "t_"
    )

    data class LineMarker(
        var mapperMethod: Boolean = true,
        var mapperStatement: Boolean = true,
        var resultMapProperty: Boolean = true,
        var resultMapResult: Boolean = true,
    )


    enum class MapperXmlPath {
        ASK, SOURCE, RESOURCE
    }
}
