package org.ifinalframework.plugins.aio.datasource.model

import com.intellij.database.model.basic.BasicTableOrView
import com.intellij.openapi.components.service
import org.ifinalframework.plugins.aio.datasource.service.ShardingTableService

/**
 * Table
 * 对使用了分库分表的表进行处理
 *
 * @author iimik
 */
data class Table(
    /**
     * 逻辑表名
     */
    val logicTable: String,
    /**
     * 物理表
     */
    val actualTables: List<BasicTableOrView>

) {
    fun isShardingTable(): Boolean {
        return actualTables.size > 1
    }

    fun getMinShardingSuffix(): String {

        val shardingTableService = service<ShardingTableService>()
        val firstOrNull = actualTables.firstOrNull { shardingTableService.isShardingTable(it.name) }
        if (firstOrNull == null) {
            return ""
        }

        return firstOrNull.name.substringAfterLast("_")
    }

    fun getMaxShardingSuffix(): String {
        val shardingTableService = service<ShardingTableService>()
        val firstOrNull = actualTables.reversed().firstOrNull { shardingTableService.isShardingTable(it.name) }
        if (firstOrNull == null) {
            return ""
        }
        return firstOrNull.name.substringAfterLast("_")
    }

    override fun toString(): String {
        val sb = StringBuilder(logicTable)
        if (isShardingTable()) {
            val minShardingSuffix = getMinShardingSuffix()
            val maxShardingSuffix = getMaxShardingSuffix()
            if (minShardingSuffix.isNotEmpty() && maxShardingSuffix.isNotEmpty()) {
                sb.append("_")
                    .append(getMinShardingSuffix())
                    .append("~")
                    .append(getMaxShardingSuffix())
            }
        }

        return sb.toString()
    }
}