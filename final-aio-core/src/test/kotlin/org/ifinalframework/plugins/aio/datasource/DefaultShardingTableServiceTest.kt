package org.ifinalframework.plugins.aio.datasource

import com.intellij.openapi.components.service
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.ifinalframework.plugins.aio.datasource.service.ShardingTableService
import org.junit.jupiter.api.Test

/**
 * DefaultShardingPredicateTest
 *
 * @author iimik
 */
class DefaultShardingTableServiceTest : BasePlatformTestCase() {
    @Test
    fun testShardingPredicate() {
        val service = service<ShardingTableService>()
        assertTrue(service.isShardingTable("a_1"))
        assertTrue(service.isShardingTable("a_01"))
        assertTrue(service.isShardingTable("a_202503"))
        assertFalse(service.isShardingTable("a"))
        assertFalse(service.isShardingTable("a_"))


        assertEquals("a", service.getLogicTable("a_1"))
        assertEquals("a", service.getLogicTable("a_01"))
        assertEquals("a", service.getLogicTable("a_202503"))
        assertEquals("a", service.getLogicTable("a"))
        assertEquals("a_", service.getLogicTable("a_"))
    }
}