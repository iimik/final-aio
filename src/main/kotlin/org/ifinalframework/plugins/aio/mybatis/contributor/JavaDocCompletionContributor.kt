package org.ifinalframework.plugins.aio.mybatis.contributor

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.psi.JavaDocTokenType
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ClassInheritorsSearch
import com.intellij.util.ProcessingContext
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.service.PsiService

/**
 * MyBatis JavaDoc 补全
 *
 * - `@typeHandler`: 自定义`typeHandler`文档标签
 *
 * @author iimik
 */
class JavaDocCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PsiJavaPatterns.psiElement(JavaDocTokenType.DOC_TAG_NAME),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {

                    val typeHandlers = foundTypeHandlers(parameters.position.project)


                    typeHandlers?.forEach { typeHandler ->

                        var icon = AllIcons.Mybatis.JVM
                        result.addElement(
                            LookupElementBuilder.create("typeHandler ${typeHandler.qualifiedName}")
                                .withIcon(icon)
                                .withCaseSensitivity(false)
                        )
                    }

                }
            })

    }


    private fun foundTypeHandlers(project: Project): List<PsiClass> {
        val typeHandler = project.service<PsiService>().findClass("org.apache.ibatis.type.TypeHandler") ?: return emptyList()
        val projectScope = GlobalSearchScope.projectScope(project)
        return ClassInheritorsSearch.search(typeHandler, projectScope, true)
            .filter { it.qualifiedName != null && !it.qualifiedName!!.contains("org.apache.ibatis") }
            .toList()
    }
}