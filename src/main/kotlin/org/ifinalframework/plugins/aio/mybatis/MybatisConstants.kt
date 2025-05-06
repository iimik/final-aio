package org.ifinalframework.plugins.aio.mybatis

import com.intellij.psi.util.ReferenceSetBase
import com.intellij.psi.xml.XmlTag


/**
 * MyBatis 常量
 *
 * @author iimik
 * @since 0.0.4
 * @see <a href="https://github.com/mybatis/mybatis-3/tree/master/src/main/java/org/apache/ibatis/annotations">Annotations</a>
 **/
object MybatisConstants {
    /**
     * SQL注解，被标记的方法不需要在xml中定义SQL
     */
    const val INSERT = "org.apache.ibatis.annotations.Insert"
    const val DELETE = "org.apache.ibatis.annotations.Delete"
    const val UPDATE = "org.apache.ibatis.annotations.Update"
    const val SELECT = "org.apache.ibatis.annotations.Select"

    /**
     * SQL提供者注释，被标记的方法也不需要在xml中定义SQL
     */
    const val INSERT_PROVIDER = "org.apache.ibatis.annotations.InsertProvider"
    const val DELETE_PROVIDER = "org.apache.ibatis.annotations.DeleteProvider"
    const val UPDATE_PROVIDER = "org.apache.ibatis.annotations.UpdateProvider"
    const val SELECT_PROVIDER = "org.apache.ibatis.annotations.SelectProvider"

    val ALL_SQL_ANNOTATIONS = setOf(INSERT, DELETE, UPDATE, SELECT, INSERT_PROVIDER, DELETE_PROVIDER, UPDATE_PROVIDER, SELECT_PROVIDER)

    const val XML_TAG_MAPPER = "mapper"
    const val XML_TAG_INSERT = "insert"
    const val XML_TAG_DELETE = "delete"
    const val XML_TAG_UPDATE = "update"
    const val XML_TAG_SELECT = "select"

    const val PARAM = "org.apache.ibatis.annotations.Param"

    private val XML_STATEMENTS = setOf(XML_TAG_INSERT, XML_TAG_DELETE, XML_TAG_UPDATE, XML_TAG_SELECT)

    const val DOT_SEPARATOR: String = ReferenceSetBase.DOT_SEPARATOR.toString()
    const val PRIORITY: Double = 400.0
    fun isMapper(xmlTag: XmlTag): Boolean {
        return XML_TAG_MAPPER == xmlTag.name
    }

    fun isStatement(xmlTag: XmlTag): Boolean {
        return XML_STATEMENTS.contains(xmlTag.name)
    }

}