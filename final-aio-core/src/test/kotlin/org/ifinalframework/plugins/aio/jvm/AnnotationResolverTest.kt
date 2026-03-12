package org.ifinalframework.plugins.aio.jvm

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import org.ifinalframework.plugins.aio.core.annotation.AnnotationAttributes
import org.ifinalframework.plugins.aio.util.SpiUtil
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassBody
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.junit.jupiter.api.Assertions
import java.util.stream.Stream

/**
 * KotlinAnnotationResolverTest
 *
 * @author iimik
 * @since 0.0.2
 */
class AnnotationResolverTest : BasePlatformTestCase() {

    private var resolver = SpiUtil.languageSpi<AnnotationResolver<PsiElement>>()
    private val functionMap = mutableMapOf<String, KtNamedFunction>()
    private val javaMethodMap = mutableMapOf<String, PsiMethod>()

    override fun setUp() {
        super.setUp()

        val classBody = getKtClass().children.first { it is KtClassBody } as KtClassBody
        classBody.functions.forEach { function ->
            functionMap[function.name!!] = function
        }

        val psiClass = myFixture.configureByFile("JavaAnnotationResolver.java").children.first { it is PsiClass }

        psiClass.children.filter { it is PsiMethod }.forEach { method ->
            javaMethodMap[(method as PsiMethod).name] = method
        }

    }

    fun testRequestMapping() {
        // @RequestMapping(method=[RequestMethod.GET]
        stream("singleMethod").map { it.getList<String>("method") }
            .forEach { Assertions.assertIterableEquals(listOf("GET"), it as MutableIterable<*>?) }
        // @RequestMapping(method=[RequestMethod.GET,POST]
        stream("multiMethod").map { it.getList<String>("method") }
            .forEach { Assertions.assertIterableEquals(listOf("GET","POST"), it as MutableIterable<*>?) }

    }

    private fun stream(method: String): Stream<AnnotationAttributes> {
        val kotlin = resolver.resolve(functionMap[method]!!.annotationEntries[0])
        val java = resolver.resolve(javaMethodMap[method]!!.annotations[0])
        return Stream.of(kotlin, java)
    }

    private fun getKtClass(): KtClass {
        val psiFile = myFixture.configureByFile("KotlinAnnotationResolver.kt")
        val ktClass = psiFile.children.firstOrNull { it is KtClass }
        return ktClass as KtClass;
    }

    override fun getTestDataPath(): String {
        return "src/test/resources/jvm"
    }
}