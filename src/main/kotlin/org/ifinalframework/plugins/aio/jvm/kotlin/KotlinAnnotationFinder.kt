package org.ifinalframework.plugins.aio.jvm.kotlin;

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.jvm.AnnotationFinder
import org.jetbrains.kotlin.idea.util.findAnnotation
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtDeclaration


/**
 * KotlinAnnotationFinder
 *
 * @author iimik
 * @since 0.0.2
 **/
class KotlinAnnotationFinder : AnnotationFinder {
    override fun findAnnotation(element: PsiElement, annotationName: String): PsiElement? {
        return when (element) {
            is KtDeclaration -> element.findAnnotation(
                FqName(annotationName)
            )
            else -> null
        }
    }
}