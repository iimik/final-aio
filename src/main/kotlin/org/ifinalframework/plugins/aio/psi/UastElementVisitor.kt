package org.ifinalframework.plugins.aio.psi;

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.uast.toUElement
import org.jetbrains.uast.visitor.UastVisitor


/**
 * UastElementVisitor
 *
 * @author iimik
 * @since 0.0.4
 **/
open class UastElementVisitor(private val uastVisitor: UastVisitor) : PsiElementVisitor() {

    override fun visitElement(element: PsiElement) {
        element.toUElement()?.let { it.accept(uastVisitor) }
    }

}