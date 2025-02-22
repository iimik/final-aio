package org.ifinalframework.plugins.aio.service

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.ifinalframework.plugins.aio.mybatis.JavaMapper
import org.ifinalframework.plugins.aio.mybatis.KotlinMapper

/**
 * PsiServiceTest
 *
 * @author iimik
 * @since 0.0.4
 */
class PsiServiceTest : BasePlatformTestCase() {

    fun testFindClass() {
        val javaMapper = project.getService(PsiService::class.java).findClass(JavaMapper::class.qualifiedName!!)
        val kotlinMapper = project.getService(PsiService::class.java).findClass(KotlinMapper::class.qualifiedName!!)
        assertNotNull(javaMapper)
        assertNotNull(kotlinMapper)
    }
}