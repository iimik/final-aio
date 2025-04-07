package org.ifinalframework.plugins.aio.mybatis.xml.dom;

import com.intellij.psi.PsiClass
import com.intellij.util.xml.*
import org.ifinalframework.plugins.aio.mybatis.xml.converter.MapperConverter


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
    @Convert(MapperConverter::class)
    fun getNamespace(): GenericAttributeValue<PsiClass>

    @SubTagList("resultMap")
    fun getResultMaps(): List<ResultMap>

    @SubTagList("sql")
    fun getSqls(): List<Sql>

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

    @SubTag("insert")
    fun addInsert(): Insert

    @SubTag("update")
    fun addUpdate(): Update

    @SubTag("delete")
    fun addDelete(): Delete

    @SubTag("select")
    fun addSelect(): Select

    @SubTag("sql")
    fun addSql(): Sql

}
