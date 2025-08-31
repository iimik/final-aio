package org.ifinalframework.plugins.aio.mybatis

import com.intellij.openapi.components.service
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.psi.*
import com.intellij.psi.xml.XmlFile
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomUtil
import org.ifinalframework.plugins.aio.datasource.model.Table
import org.ifinalframework.plugins.aio.datasource.service.DataSourceService
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils.isMapper
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils.isStatementMethod
import org.ifinalframework.plugins.aio.mybatis.xml.dom.IdDomElement
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Statement
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.service.NotificationService
import org.ifinalframework.plugins.aio.util.CaseFormatUtils
import java.util.function.Consumer


/**
 * MyBaati 工具类
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
            is PsiClass -> element.isInterface && element.name != null && element.name!!.endsWith(MAPPER) && element.name != MAPPER
            else -> false
        }
    }

    /**
     * 判断一个文件是否是Mapper.xml文件
     */
    fun isMapperXml(file: PsiFile?): Boolean {
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

    fun findStatementDomElement(element: PsiElement?): Statement? {
        val domElement = DomUtil.getDomElement(element) ?: return null
        if (domElement is Statement) {
            return domElement
        }
        return DomUtil.getParentOfType(domElement, Statement::class.java, true)
    }


    /**
     * 判断一个方法是不是Statement方法
     * - 非默认方法
     * - 不含有特定的注解[MybatisConstants.ALL_SQL_ANNOTATIONS]
     * @see [isMapper]
     */
    fun isStatementMethod(method: PsiMethod): Boolean {
        // 排除默认方法
        if (method.hasModifierProperty(PsiModifier.DEFAULT)) {
            return false
        }

        val clazz = method.containingClass ?: return false
        // Mapper 必须是接口
        if (!isMapper(clazz)) {
            return false
        }
        val qualifiedName = clazz.qualifiedName
        if ("java.lang.Object" == qualifiedName || Any::class.qualifiedName == qualifiedName) return false

        // 含有特定注解
        val hasAnnotation = MybatisConstants.ALL_SQL_ANNOTATIONS.map { method.hasAnnotation(it) }.firstOrNull { it } ?: false
        return !hasAnnotation
    }

    fun doSelectTable(mapperClass: PsiClass, consumer: Consumer<Table>) {
        val project = mapperClass.project
        val myBatisProperties = project.service<MyBatisProperties>()
        val tables = project.service<DataSourceService>().getTables(myBatisProperties.tableSqlFragment.prefix)
        if (tables.isEmpty()) {
            service<NotificationService>().error("未找到可以操作的数据表！！！")
            return
        }

        val title = "请选择${mapperClass.name}要操作的数据表："

        val suggestTable = CaseFormatUtils.upperCamel2LowerUnderscore(mapperClass.name!!.substringBeforeLast("Mapper"))
        val list = tables.filter { it.logicTable.endsWith(suggestTable) }.toList()
        if (list.isEmpty()) {
            showTableSelectPopup(title, tables, consumer)
        }
        if (list.size > 1) {
            showTableSelectPopup(title, list, consumer)
        }

        if (list.size == 1) {
            consumer.accept(list[0])
        }
    }


    fun showTableSelectPopup(title: String, tables: List<Table>, consumer: Consumer<Table>) {
        JBPopupFactory.getInstance().createListPopup(
            object : BaseListPopupStep<Table>(
                title,
                tables,
                AllIcons.Mybatis.JVM
            ) {
                override fun onChosen(selectedValue: Table, finalChoice: Boolean): PopupStep<*>? {
                    consumer.accept(selectedValue)
                    return FINAL_CHOICE
                }
            }
        ).showInFocusCenter()
    }


}