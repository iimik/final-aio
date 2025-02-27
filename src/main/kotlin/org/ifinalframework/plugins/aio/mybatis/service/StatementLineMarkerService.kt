package org.ifinalframework.plugins.aio.mybatis.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlToken
import com.intellij.psi.xml.XmlTokenType
import com.intellij.util.xml.DomUtil
import org.ifinalframework.plugins.aio.common.util.getService
import org.ifinalframework.plugins.aio.mybatis.MybatisMarker
import org.ifinalframework.plugins.aio.mybatis.xml.dom.*
import org.ifinalframework.plugins.aio.service.PsiService
import org.jetbrains.kotlin.idea.util.CommentSaver.Companion.tokenType
import java.util.stream.Collectors
import java.util.stream.Stream


/**
 * StatementLineMarkerService
 *
 * - mapper:
 * - resultMap:
 * - insert:
 * - delete:
 * - update:
 * - select:
 *
 * @issue 26
 * @author iimik
 * @since 0.0.4
 **/
@Service
class StatementLineMarkerService : MapperLineMarkerService<XmlToken> {

    private val TARGET_TAGS = Stream.of(
        Mapper::class, ResultMap::class, Insert::class, Delete::class, Update::class, Select::class
    ).map { it.simpleName!!.lowercase() }.collect(Collectors.toSet())


    override fun apply(token: XmlToken): MybatisMarker? {
        val project = token.project
        val psiFile = token.containingFile
        if (psiFile !is XmlFile) return null
        if ("mapper" != psiFile.rootTag?.name) return null
        val text = token.text
        return if (token.parent is XmlTag && TARGET_TAGS.contains(text.lowercase()) && token.nextSibling is PsiWhiteSpace) {

            val prevSibling = token.prevSibling

            if (prevSibling !is XmlToken && prevSibling.tokenType != XmlTokenType.XML_START_TAG_START) return null

            val domElement = DomUtil.getDomElement(token) ?: return null
            return when (domElement) {
                is Mapper -> process(project, domElement)
                is ResultMap -> process(project, domElement)
                is Statement -> process(project, domElement)
                else -> null
            }
        } else null

    }

    private fun process(project: Project, mapper: Mapper): MybatisMarker? {
        val namespace = mapper.getNamespace().stringValue ?: return null
        val clazz = project.getService<PsiService>().findClass(namespace) ?: return MybatisMarker.NOT_EXISTS
        return MybatisMarker(listOf(clazz))
    }

    private fun process(project: Project, resultMap: ResultMap): MybatisMarker {
        val type = resultMap.getType().stringValue ?: return MybatisMarker.NOT_EXISTS
        val clazz = project.getService<PsiService>().findClass(type) ?: return MybatisMarker.NOT_EXISTS
        return MybatisMarker(listOf(clazz))
    }

    private fun process(project: Project, statement: Statement): MybatisMarker {
        val mapper = DomUtil.getParentOfType(statement, Mapper::class.java, true) ?: return MybatisMarker.NOT_EXISTS
        val namespace = mapper.getNamespace().stringValue ?: return MybatisMarker.NOT_EXISTS
        val clazz = project.getService<PsiService>().findClass(namespace) ?: return MybatisMarker.NOT_EXISTS
        val id = statement.getId().stringValue!!
        val methods = clazz.findMethodsByName(id, false)
        if (methods.isEmpty()) return MybatisMarker.NOT_EXISTS

        return MybatisMarker(methods.toList())
    }
}