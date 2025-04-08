package org.ifinalframework.plugins.aio.mybatis

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.xml.XmlFile
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomUtil
import org.ifinalframework.plugins.aio.mybatis.xml.dom.IdDomElement
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.jetbrains.uast.UClass


/**
 * MapperUtils
 *
 * @author iimik
 * @since 0.0.6
 **/
object MyBatisUtils {

    private const val MAPPER = "Mapper"

    /**
     * 判断一个元素是不是Mapper
     * Mapper需要满足以下条件
     * - 是接口
     * - 名称以Mapper结尾
     * @since 0.0.10
     * @see [isStatementMethod]
     */
    fun isMapper(element: PsiElement): Boolean {
        return when (element) {
            is UClass -> element.isInterface && element.name != null && element.name!!.endsWith(MAPPER) && element.name != MAPPER
            else -> false
        }
    }

    fun isMybatisFile(file: PsiFile?): Boolean {
        if (file !is XmlFile) {
            return false
        }
        val rootTag = file.rootTag
        return null != rootTag && rootTag.name == "mapper"
    }

    fun getNamespace(mapper: Mapper): String {
        val ns = mapper.getNamespace().stringValue
        return ns ?: ""
    }

    fun getNamespace(element: DomElement): String {
        return getNamespace(getMapper(element))
    }

    fun getMapper(element: DomElement): Mapper {
        return DomUtil.getParentOfType(
            element, Mapper::class.java, true
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

    fun findParentIdDomElement(element: PsiElement?): IdDomElement? {
        val domElement = DomUtil.getDomElement(element) ?: return null
        if (domElement is IdDomElement) {
            return domElement
        }
        return DomUtil.getParentOfType(domElement, IdDomElement::class.java, true)
    }

    /**
     * 判断一个方法是不是Statement方法
     * - 非默认方法
     * - 不含有特定的注解
     * @see [isMapper]
     */
    fun isStatementMethod(method: PsiMethod): Boolean {
        // 排除默认方法
        if (method.hasModifierProperty(PsiModifier.DEFAULT)) {
            return false
        }

        val clazz = method.containingClass ?: return false
        val qualifiedName = clazz.qualifiedName
        if ("java.lang.Object" == qualifiedName || Any::class.qualifiedName == qualifiedName) return false

        // 含有特定注解
        val hasAnnotation = MybatisConstants.ALL_STATEMENTS.map { method.hasAnnotation(it) }.firstOrNull { it } ?: false
        return !hasAnnotation
    }


}