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
    var testCompletion: TestCompletion = TestCompletion(),
    var insertMethodRegex: String = "^(insert|add|create)+\\w*\$",
    var deleteMethodRegex: String = "^(del|remove|clean)+\\w*\$",
    var updateMethodRegex: String = "^(update|set)+\\w*\$",
    var selectMethodRegex: String = "^(select|find|get|query|count)+\\w*\$",

    ) : PersistentStateComponent<MyBatisProperties> {
    override fun getState(): MyBatisProperties? {
        return this
    }

    override fun loadState(properties: MyBatisProperties) {
        XmlSerializerUtil.copyBean<MyBatisProperties>(properties, this)
    }

    data class TestCompletion(
        var stringType: String = "null != \${TARGET} and \${TARGET} != ''",
        var collectionType: String = "null != \${TARGET} and \${TARGET}.size > 0",
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


    enum class MapperXmlPath {
        ASK, SOURCE, RESOURCE
    }
}
