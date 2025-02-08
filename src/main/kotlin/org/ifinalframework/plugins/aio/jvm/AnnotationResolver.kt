package org.ifinalframework.plugins.aio.jvm

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.application.annotation.ReadAction
import org.ifinalframework.plugins.aio.jvm.java.JavaAnnotationResolver
import org.ifinalframework.plugins.aio.jvm.kotlin.KotlinAnnotationResolver
import org.ifinalframework.plugins.aio.spi.annotation.LanguageSpi


/**
 * AnnotationResolver
 * @issue 19
 * @author iimik
 * @since 0.0.2
 **/
@LanguageSpi<AnnotationResolver<out PsiElement>>(
    JavaAnnotationResolver::class,
    KotlinAnnotationResolver::class
)
@ReadAction
interface AnnotationResolver<A : PsiElement> {
    fun resolve(annotation: A): Map<String, Any?>
}