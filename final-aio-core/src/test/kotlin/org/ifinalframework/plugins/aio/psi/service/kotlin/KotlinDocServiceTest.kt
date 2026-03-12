package org.ifinalframework.plugins.aio.psi.service.kotlin

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.kotlin.psi.KtClass
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * KotlinDocServiceTest
 *
 * @issue 10
 * @author iimik
 * @since 0.0.1
 */
class KotlinDocServiceTest : BasePlatformTestCase() {

    private val service = KotlinDocService()

    private fun getKtClass(): KtClass {
        val psiFile = myFixture.configureByFile("KotlinDocServiceTest.kt")
        val ktClass = psiFile.children.firstOrNull { it is KtClass }
        return ktClass as KtClass;
    }

    @Test
    fun testGetSummary() {
        val ktClass = getKtClass()
        val summary = service.getSummary(ktClass!!)
        Assertions.assertEquals("KotlinDocServiceTest", summary)
        Assertions.assertNotNull(ktClass)
    }

    @Test
    fun testGetTagName() {
        val ktClass = getKtClass()
        val docComment = ktClass.docComment
        Assertions.assertNotNull(ktClass)
    }

    @Test
    fun testGetTagValueByTag(){
        val ktClass = getKtClass()
        val value = service.findTagValueByTag(ktClass, "issue")
        Assertions.assertNotNull(value)
    }

    override fun getTestDataPath(): String {
        return "src/test/kotlin/" + this::class.java.packageName.replace('.', '/')
    }

}