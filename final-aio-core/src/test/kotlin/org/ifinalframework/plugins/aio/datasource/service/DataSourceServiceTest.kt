package org.ifinalframework.plugins.aio.datasource.service

import com.intellij.openapi.components.service
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * DataSourceServiceTest
 *
 * @author iimik
 */
class DataSourceServiceTest: BasePlatformTestCase() {

    public fun testGetSchemas(){
        val dataSourceService = project.service<DataSourceService>()
        dataSourceService.getSchemas();
    }
}