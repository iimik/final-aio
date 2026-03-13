package org.ifinalframework.plugins.aio.util

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiType
import com.intellij.psi.impl.source.PsiClassReferenceType
import org.ifinalframework.plugins.aio.service.PsiService

/**
 * PsiTypeUtils
 *
 * @author iimik
 */
object PsiTypeUtils {
    fun getClass(project: Project, psiType: PsiType): PsiClass?{
        val psiService = project.service<PsiService>()
        if (psiType is PsiClassReferenceType) {
            val className = psiType.reference.qualifiedName
            return psiService.findClass(className)
        }

        return null
    }

    fun isList(psiType: PsiType): Boolean {
        if(psiType is PsiClassReferenceType) {
            val className = psiType.reference.qualifiedName
            psiType.reference.typeParameters
            return className != null && className == "java.util.List"
        }
        return false;
    }
}