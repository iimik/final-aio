package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList
import com.intellij.util.xml.SubTagsList

/**
 * SqlFragment
 *
 * @author iimik
 */
interface SqlFragment : DomElement {

    @SubTagsList("include", "if", "where", "trim", "foreach")
    fun getSqlFragments(): List<SqlFragment>

    @SubTagList("include")
    fun getIncludes(): List<Include>

    fun addInclude(): Include

    @SubTagList("if")
    fun getIfs(): List<Test.If>

    fun addIf(): Test.If

    @SubTagList("where")
    fun getWheres(): List<Where>

    fun addWhere(): Where

    @SubTagList("trim")
    fun getTrims(): List<Trim>

    fun addTrim(): Trim

    @SubTagList("foreach")
    fun getForeachs(): List<Foreach>

    fun addForeach(): Foreach
}

/**
 * Where
 *
 * @author iimik
 */
interface Where : SqlFragment {
}

