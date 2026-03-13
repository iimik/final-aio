package org.ifinalframework.plugins.aio.mybatis.inspection;

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.highlighting.BasicDomElementsInspection


/**
 * MapperXmlInspection
 *
 * @author iimik
 * @since 0.0.4
 **/
class MapperXmlInspection : BasicDomElementsInspection<DomElement>(DomElement::class.java) {
}