package org.ifinalframework.plugins.aio.spring

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.api.constans.SpringAnnotations
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getUastParentOfType


/**
 * SpringUtils
 *
 * @author iimik
 * @since 0.0.25
 **/
object SpringUtils {
    /**
     * 判断一个元素是不是FeignClient
     */
    fun isFeign(element: PsiElement): Boolean {
        val klass = element.getUastParentOfType<UClass>() ?: return false
        return klass.hasAnnotation(SpringAnnotations.FEIGN_CLIENT)
    }

    fun isController(element: PsiElement): Boolean {
        val klass = element.getUastParentOfType<UClass>() ?: return false
        return klass.hasAnnotation(SpringAnnotations.REST_CONTROLLER)
    }

    fun isControllerMethod(element: PsiElement): Boolean {
        val method = getUMethod(element) ?: return false
        val klass = method.getUastParentOfType<UClass>() ?: return false
        SpringAnnotations.REQUEST_MAPPINGS.any { method.hasAnnotation(it) } ?: return false
        return klass.hasAnnotation(SpringAnnotations.REST_CONTROLLER)
    }

    /**
     * 判断一个元素是不是Feign Client的方法
     * 1. 它是一个方法
     * 2. 有`@RequestMapping`注解
     * 3. 类上有`@FeignClient`注解
     */
    fun isFeignMethod(element: PsiElement): Boolean {
        val method = getUMethod(element) ?: return false
        val klass = method.getUastParentOfType<UClass>() ?: return false
        SpringAnnotations.REQUEST_MAPPINGS.any { method.hasAnnotation(it) } ?: return false
        return klass.hasAnnotation(SpringAnnotations.FEIGN_CLIENT)
    }

    private fun getUMethod(element: PsiElement): UMethod? {
        return when (element) {
            is UMethod -> element
            else -> element.getUastParentOfType<UMethod>()
        }
    }
}