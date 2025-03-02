package org.ifinalframework.plugins.aio.mybatis.xml.dom;

import com.intellij.util.xml.*
import org.ifinalframework.plugins.aio.mybatis.xml.converter.ClassResolvingConverter


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
    @Convert(ClassResolvingConverter::class)
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
