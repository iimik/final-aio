package org.ifinalframework.plugins.aio.mybatis

import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.uast.UClass
import org.jetbrains.uast.toUElement

/**
 * MyBatisUtilsTest
 *
 * @author iimik
 */
class MyBatisUtilsTest : BasePlatformTestCase() {
    fun testIsMapper() {

        assertTrue(MyBatisUtils.isMapper(getClasses("mapper/KotlinMapper.kt")[0]))
        assertTrue(MyBatisUtils.isMapper(getClasses("mapper/JavaMapper.java")[0]))
        // 单元测试读不到ann
//        assertTrue(MyBatisUtils.isMapper(getClasses("mapper/JavaMapperWithAnnotation.java")[0]))
    }

    private fun getClasses(filePath: String): List<PsiElement> {
        val psiFile = myFixture.configureByFile(filePath)
        val toList = psiFile.children.map { it.toUElement() }.filterNotNull().filter { it is UClass }.toList()
        return toList as List<PsiElement>
    }

    override fun getTestDataPath(): String? {
        return "src/test/resources/mybatis"
    }

}