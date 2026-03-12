package org.ifinalframework.plugins.aio.datasource.service

/**
 * ShardingPre
 *
 * @author iimik
 */
interface ShardingTableService {

    /**
     * 判断一个表是否是分表
     */
    fun isShardingTable(table: String): Boolean

    /**
     * 获取一个表的逻辑表名
     */
    fun getLogicTable(table: String): String

}

