package org.ifinalframework.plugins.aio.application

import com.intellij.psi.PsiElement


/**
 * ElementPropertySourcesLoader
 *
 * @author iimik
 * @since 0.0.1
 **/
interface ElementPropertySourcesLoader {
    fun load(classLoader: ClassLoader, element: PsiElement)
}