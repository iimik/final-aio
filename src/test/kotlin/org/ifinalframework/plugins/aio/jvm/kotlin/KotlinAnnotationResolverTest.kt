package org.ifinalframework.plugins.aio.jvm.kotlin

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import org.ifinalframework.plugins.aio.jvm.AnnotationForResolver
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassBody
import org.jetbrains.kotlin.psi.KtNamedFunction

/**
 * KotlinAnnotationResolverTest
 *
 * @author iimik
 * @since 0.0.2
 */
class KotlinAnnotationResolverTest : BasePlatformTestCase() {

    private var resolver = KotlinAnnotationResolver()
    private val functionMap = mutableMapOf<String, KtNamedFunction>()

    override fun setUp() {
        super.setUp()
        val classBody = getKtClass().children.first { it is KtClassBody } as KtClassBody
        classBody.functions.forEach { function ->
            functionMap[function.name!!] = function
        }
    }

    @AnnotationForResolver.StringValue("Hello World!")
    fun testStringValue() {
        TestCase.assertEquals("Hello World!", getValue())
    }

    private fun getFun(): KtNamedFunction {
        val methodName = qualifiedTestMethodName.substringAfterLast(".")
        return functionMap[methodName]!!
    }

    private fun getValue(): Any? {
        val map = resolver!!.resolve(getFun().annotationEntries[0])
        return map["value"]
    }

    private fun getKtClass(): KtClass {
        val psiFile = myFixture.configureByFile(this::class.simpleName + ".kt")
        val ktClass = psiFile.children.firstOrNull { it is KtClass }
        return ktClass as KtClass;
    }

    override fun getTestDataPath(): String {
        return "src/test/kotlin/" + this::class.java.packageName.replace('.', '/')
    }
}