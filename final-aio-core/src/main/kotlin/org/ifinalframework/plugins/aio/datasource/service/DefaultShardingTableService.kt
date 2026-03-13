package org.ifinalframework.plugins.aio.datasource.service

/**
 * DefaultShardingTableService
 *
 * @author iimik
 */
class DefaultShardingTableService : ShardingTableService {
    /**
     * 分表正则：表名_数字
     */
    private val shardingTable = Regex("[\\w_]+_\\d+$")
    override fun isShardingTable(table: String): Boolean {
        return shardingTable.matches(table)
    }

    override fun getLogicTable(table: String): String {
        return if (isShardingTable(table)) {
            table.substringBeforeLast("_")
        } else table
    }
}