package org.ifinalframework.plugins.aio.mybatis.xml.dom;

import com.intellij.util.xml.*


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

interface Statement : IdDomElement {
}

interface Insert : Statement {}
interface Delete : Statement {}
interface Update : Statement {}
interface Select : Statement {}
