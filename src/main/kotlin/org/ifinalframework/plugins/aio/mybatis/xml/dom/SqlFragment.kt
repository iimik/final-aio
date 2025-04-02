package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.SubTagList

/**
 * SqlFragment
 *
 * @author iimik
 */
interface SqlFragment: DomElement {
    @SubTagList("include")
    fun getIncludes():List<Include>
}