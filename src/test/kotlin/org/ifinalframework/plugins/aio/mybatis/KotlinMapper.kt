package org.ifinalframework.plugins.aio.mybatis

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.InsertProvider


/**
 * KotlinMapper
 *
 * @author iimik
 * @since 0.0.4
 **/
interface KotlinMapper {

    fun insert(any:Any): Int {
        return insert()
    }

    fun insert(): Int

    @Insert("")
    fun insertWithInsert(): Int

    @InsertProvider
    fun insertWithProvider(): Int
    fun delete(): Int

    fun update(): Int

    fun select(): Int

    fun selectResultMaps(): List<ResultMap>

}