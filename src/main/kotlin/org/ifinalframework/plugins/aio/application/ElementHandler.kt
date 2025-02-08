package org.ifinalframework.plugins.aio.application

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.application.annotation.EDT


/**
 * ElementHandler
 *
 * @author iimik
 * @since 0.0.1
 **/
interface ElementHandler {
//    @EDT
    fun handle(element: PsiElement)
}