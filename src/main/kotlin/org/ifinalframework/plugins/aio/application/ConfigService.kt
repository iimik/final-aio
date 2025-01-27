package org.ifinalframework.plugins.aio.application

import com.intellij.psi.PsiElement


/**
 * ConfigService
 *
 * @author iimik
 * @since 0.0.1
 **/
interface ConfigService {
    fun getConfigPaths(element: PsiElement): List<String>
}