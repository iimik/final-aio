package org.ifinalframework.plugins.mybatis

import com.intellij.psi.PsiElement


/**
 * MybatisMarker
 *
 * @author iimik
 * @since 0.0.4
 **/
data class MybatisMarker(
    val targets: Collection<PsiElement>?
) {
    companion object {

        val NOT_EXISTS = MybatisMarker(null)

    }
}