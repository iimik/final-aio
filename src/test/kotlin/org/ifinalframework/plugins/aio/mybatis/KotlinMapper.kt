package org.ifinalframework.plugins.aio.mybatis;

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.InsertProvider


/**
 * KotlinMapper
 *
 * @author iimik
 * @since 0.0.4
 **/
interface KotlinMapper {

    fun insert(): Int
    
    fun defaultInsert(): Int {
        return insert()
    }

    @Insert("")
    fun insertWithInsert(): Int

    @InsertProvider
    fun insertWithProvider(): Int

    fun delete(): Int

    fun update(): Int

    fun select(): Int

}