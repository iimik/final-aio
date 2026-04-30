package org.ifinalframework.plugins.aio.spring.provider;

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import org.ifinalframework.plugins.aio.spring.SpringUtils
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getUastParentOfType

/**
 * ControllerFindUsagesHandlerFactory
 *
 * 如果返回了自定义的`FindUsagesHandler`，默认的就不会生效
 *
 * @author iimik
 * @since 0.0.25
 **/
class ControllerFindUsagesHandlerFactory : FindUsagesHandlerFactory() {

    override fun canFindUsages(element: PsiElement): Boolean {
        return when (element) {
            // java method
            is PsiMethod -> SpringUtils.isControllerMethod(element)
            // kotlin method
            is KtNamedFunction -> SpringUtils.isControllerMethod(element)
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