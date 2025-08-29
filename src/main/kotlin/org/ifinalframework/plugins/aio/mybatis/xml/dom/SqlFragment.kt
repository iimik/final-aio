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

    @SubTagsList("include", "if", "where")
    fun getSqlFragments(): List<SqlFragment>

    @SubTagList("include")
    fun getIncludes(): List<Include>

    @SubTagList("if")
    fun getIfs(): List<Test.If>

    @SubTagList("where")
    fun getWheres(): List<Where>

    fun addInclude(): Include

    fun addWhere(): Where
}

/**
 * Where
 *
 * @author iimik
 */
interface Where : SqlFragment {
}