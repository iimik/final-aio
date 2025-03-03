package org.ifinalframework.plugins.aio.mybatis

import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomUtil
import org.ifinalframework.plugins.aio.mybatis.xml.dom.IdDomElement
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper


/**
 * MapperUtils
 *
 * @author iimik
 * @since 0.0.6
 **/
object MapperUtils {

    fun getNamespace(mapper: Mapper): String {
        val ns = mapper.getNamespace().stringValue
        return ns ?: ""
    }

    fun getNamespace(element: DomElement): String {
        return getNamespace(getMapper(element))
    }

    fun getMapper(element: DomElement): Mapper {
        return DomUtil.getParentOfType(
            element,
            Mapper::class.java, true
        ) ?: throw IllegalArgumentException("Unknown element")
    }

    fun <T : IdDomElement> getId(domElement: T): String? {
        return domElement.getId().rawText
    }

    fun <T : IdDomElement> getIdSignature(domElement: T): String {
        return getNamespace(domElement) + "." + getId(domElement)
    }

    fun <T : IdDomElement> getIdSignature(domElement: T, mapper: Mapper): String {
        val contextMapper = getMapper(domElement)
        var id = getId(domElement)
        if (id == null) {
            id = ""
        }
        val idsignature = getIdSignature(domElement)
        //getIdSignature(domElement)
        return if (isMapperWithSameNamespace(contextMapper, mapper)) id else idsignature
    }

    fun isMapperWithSameNamespace(mapper: Mapper?, target: Mapper?): Boolean {
        return null != mapper && null != target && getNamespace(mapper) == getNamespace(target)
    }
}