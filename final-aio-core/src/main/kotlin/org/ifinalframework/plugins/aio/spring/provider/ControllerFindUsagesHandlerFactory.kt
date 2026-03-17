package org.ifinalframework.plugins.aio.spring.provider;

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getUastParentOfType

/**
 * ControllerFindUsagesHandlerFactory
 *
 * @author iimik
 * @since 1.6.0
 **/
class ControllerFindUsagesHandlerFactory : FindUsagesHandlerFactory() {
    override fun canFindUsages(element: PsiElement): Boolean {
        return when (element) {
            is PsiMethod -> true
            is KtNamedFunction -> true
            else -> false
        }
    }

    override fun createFindUsagesHandler(
        element: PsiElement,
        forHighlightUsages: Boolean
    ): FindUsagesHandler? {
        val u = element.getUastParentOfType<UMethod>() ?: return null
        return ControllerFindUsagesHandler(u)
    }
}