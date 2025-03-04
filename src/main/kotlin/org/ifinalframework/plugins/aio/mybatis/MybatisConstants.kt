package org.ifinalframework.plugins.aio.mybatis

import com.intellij.psi.util.ReferenceSetBase
import com.intellij.psi.xml.XmlTag


/**
 * MybatisAnnotations
 *
 * @author iimik
 * @since 0.0.4
 * @see <a href="https://github.com/mybatis/mybatis-3/tree/master/src/main/java/org/apache/ibatis/annotations">Annotations</a>
 **/
object MybatisConstants {
    val INSERT = "org.apache.ibatis.annotations.Insert"
    val DELETE = "org.apache.ibatis.annotations.Delete"
    val UPDATE = "org.apache.ibatis.annotations.Update"
    val SELECT = "org.apache.ibatis.annotations.Select"

    val INSERT_PROVIDER = "org.apache.ibatis.annotations.InsertProvider"
    val DELETE_PROVIDER = "org.apache.ibatis.annotations.DeleteProvider"
    val UPDATE_PROVIDER = "org.apache.ibatis.annotations.UpdateProvider"
    val SELECT_PROVIDER = "org.apache.ibatis.annotations.SelectProvider"

    val ALL_STATEMENTS = setOf(INSERT, DELETE, UPDATE, SELECT, INSERT_PROVIDER, DELETE_PROVIDER, UPDATE_PROVIDER, SELECT_PROVIDER)

    val XML_TAG_MAPPER = "mapper"
    val XML_TAG_INSERT = "insert"
    val XML_TAG_DELETE = "delete"
    val XML_TAG_UPDATE = "update"
    val XML_TAG_SELECT = "select"

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