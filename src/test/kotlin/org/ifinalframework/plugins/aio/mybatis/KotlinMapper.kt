package org.ifinalframework.plugins.aio.mybatis

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.InsertProvider
import org.apache.ibatis.annotations.Param


/**
 * KotlinMapper
 *
 * @author iimik
 * @since 0.0.4
 **/
interface KotlinMapper {

    /**
     * insert
     */
    fun insert(any:Any): Int {
        return insert("")
    }

    /**
     * 插入
     */
    fun insert(@Param("p1")name: String, age: Int, list: List<String>, resultMap: ResultMap): Int

    @Insert("")
    fun insertWithInsert(): Int

    @InsertProvider
    fun insertWithProvider(): Int
    fun delete(): Int

    fun update(): Int

    fun select(): Int

    fun selectSingle(resultMap: ResultMap): ResultMap

    fun selectResultMaps(): List<ResultMap>

    fun insert3():Int

    fun test()

}