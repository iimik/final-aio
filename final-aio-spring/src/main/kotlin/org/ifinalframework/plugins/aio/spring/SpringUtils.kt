package org.ifinalframework.plugins.aio.spring

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.api.constans.SpringAnnotations
import org.jetbrains.kotlin.utils.addToStdlib.ifFalse
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
        return R.computeInRead {
            val method = getUMethod(element) ?: return@computeInRead false
            val klass = method.getUastParentOfType<UClass>() ?: return@computeInRead false
            SpringAnnotations.REQUEST_MAPPINGS.any { method.hasAnnotation(it) }.ifFalse { return@computeInRead false }
            return@computeInRead klass.hasAnnotation(SpringAnnotations.REST_CONTROLLER) || klass.hasAnnotation(
                SpringAnnotations.REQUEST_MAPPING)
        } ?: false

    }

    /**
     * 判断一个元素是不是Feign Client的方法
     * 1. 它是一个方法
     * 2. 有`@RequestMapping`注解
     * 3. 类上有`@FeignClient`注解
     */
    fun isFeignMethod(element: PsiElement): Boolean {
        return R.computeInRead {
            val method = getUMethod(element) ?: return@computeInRead false
            val klass = method.getUastParentOfType<UClass>() ?: return@computeInRead false
            SpringAnnotations.REQUEST_MAPPINGS.any { method.hasAnnotation(it) }.ifFalse { return@computeInRead false }
            return@computeInRead klass.hasAnnotation(SpringAnnotations.FEIGN_CLIENT)
        } ?: false

    }

    private fun getUMethod(element: PsiElement): UMethod? {
        return when (element) {
            is UMethod -> element
            else -> element.getUastParentOfType<UMethod>()
        }

    }
}