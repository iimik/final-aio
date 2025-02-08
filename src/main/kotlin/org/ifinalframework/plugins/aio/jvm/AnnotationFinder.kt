package org.ifinalframework.plugins.aio.jvm

import com.intellij.psi.PsiElement


/**
 * AnnotationFinder
 *
 * @author iimik
 * @since 0.0.2
 **/
interface AnnotationFinder {
    fun findAnnotation(element: PsiElement, annotationName: String): PsiElement?
}