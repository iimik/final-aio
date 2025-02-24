package org.ifinalframework.plugins.aio.service;

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope


/**
 * PsiService
 *
 * @author iimik
 * @since 0.0.4
 **/
@Service(Service.Level.PROJECT)
class PsiService(val project: Project) {
    private val javaPsiFacade = JavaPsiFacade.getInstance(project)

    /**
     */
    fun findClass(className: String): PsiClass? {
        return javaPsiFacade.findClass(className, GlobalSearchScope.allScope(project)) ?: return null
    }
}