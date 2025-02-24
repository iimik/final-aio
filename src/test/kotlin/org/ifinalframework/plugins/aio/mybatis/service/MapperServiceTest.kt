package org.ifinalframework.plugins.aio.mybatis.service

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * MapperServiceTest
 *
 * @author iimik
 * @since 0.0.4
 */
class MapperServiceTest : BasePlatformTestCase() {

    fun testFindMappers() {
        val mappers = project.getService(MapperService::class.java).findMappers()
        assertNotEmpty(mappers)
    }
}