package org.ifinalframework.plugins.aio.service;

import com.intellij.ide.util.ClassFilter
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.idea.base.psi.kotlinFqName
import org.jetbrains.kotlin.idea.core.util.toPsiFile
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.uast.UClass
import org.jetbrains.uast.toUElement


/**
 * PsiService
 *
 * @author iimik
 * @since 0.0.4
 **/
@Service(Service.Level.PROJECT)
class PsiService(val project: Project) {
    private val javaPsiFacade = JavaPsiFacade.getInstance(project)

    private val jvmFileExtensions = listOf("java", "kt")

    /**
     */
    fun findClass(className: String): PsiClass? {
        return javaPsiFacade.findClass(className, GlobalSearchScope.allScope(project)) ?: return null
    }

    fun findAllClasses(filter: ClassFilter = ClassFilter.ALL): List<PsiClass> {
        return jvmFileExtensions.flatMap {
            FilenameIndex.getAllFilesByExt(project, it)
                .mapNotNull { f -> f.toPsiFile(project) }
                .flatMap { f ->
                    PsiTreeUtil.findChildrenOfAnyType(f, PsiClass::class.java, KtClass::class.java)
                        .filter { clazz -> clazz.toUElement() is UClass }
                        .mapNotNull { f -> f.kotlinFqName?.asString() }
                }
                .mapNotNull { name -> findClass(name) }
                .filter { f -> filter.isAccepted(f) }
        }
    }

}