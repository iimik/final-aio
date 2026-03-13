package org.ifinalframework.plugins.aio.jvm

import com.intellij.psi.PsiElement


/**
 * ElementService
 *
 * @author iimik
 * @since 0.0.4
 **/
interface ElementService {
    fun getQualifiedName(element: PsiElement): String?;
}