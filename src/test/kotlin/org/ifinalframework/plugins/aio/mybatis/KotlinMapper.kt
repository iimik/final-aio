package org.ifinalframework.plugins.aio.mybatis;


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

    fun delete(): Int

    fun update(): Int

    fun select(): Int

}