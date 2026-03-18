package org.ifinalframework.plugins.aio.spring

import com.intellij.psi.PsiElement


/**
 * HttpEndPoint
 *
 * @author iimik
 * @since 0.0.25
 **/
data class HttpEndPoint(
    val httpMethod: String,
    val path: String,
    val element: PsiElement
)
