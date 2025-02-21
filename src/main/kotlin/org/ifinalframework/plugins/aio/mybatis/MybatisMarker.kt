package org.ifinalframework.plugins.aio.mybatis

import com.intellij.psi.PsiElement


/**
 * MybatisMarker
 *
 * @author iimik
 * @since 0.0.4
 **/
data class MybatisMarker(
    val targets: List<PsiElement>?
) {
    companion object {

        val NOT_EXISTS = MybatisMarker(null)

    }
}