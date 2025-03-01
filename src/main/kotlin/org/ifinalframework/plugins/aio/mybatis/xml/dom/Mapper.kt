package org.ifinalframework.plugins.aio.mybatis.xml.dom;

import com.intellij.util.xml.*
import org.ifinalframework.plugins.aio.mybatis.xml.converter.StatementMethodResolvingConverter


/**
 * Mapper
 *
 * @author iimik
 * @since 0.0.4
 **/
interface Mapper : DomElement {
    @Required
    @NameValue
    @Attribute("namespace")
    fun getNamespace(): GenericAttributeValue<String>

    @SubTagList("resultMap")
    fun getResultMaps(): List<ResultMap>

    @SubTagsList("insert", "delete", "update", "select")
    fun getStatements(): List<Statement>

    @SubTagList("insert")
    fun getInserts(): List<Insert>

    @SubTagList("delete")
    fun getDeletes(): List<Delete>

    @SubTagList("update")
    fun getUpdates(): List<Update>

    @SubTagList("select")
    fun getSelects(): List<Select>

}

interface IdDomElement : DomElement {
    @Required
    @NameValue
    @Attribute("id")
    fun getId(): GenericAttributeValue<String>

    fun setValue(content: String)
}

interface ResultMap : IdDomElement {
    @Required
    @NameValue
    @Attribute("type")
    fun getType(): GenericAttributeValue<String>
}

interface Statement : IdDomElement {
    @Required
    @NameValue
    @Attribute("id")
    @Convert(StatementMethodResolvingConverter::class)
    override fun getId(): GenericAttributeValue<String>
}

interface Insert : Statement {}
interface Delete : Statement {}
interface Update : Statement {}
interface Select : Statement {}
