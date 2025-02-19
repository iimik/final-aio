package org.ifinalframework.plugins.aio.mybatis


/**
 * MybatisAnnotations
 *
 * @author iimik
 * @since 0.0.4
 * @see [annotations](https://github.com/mybatis/mybatis-3/tree/master/src/main/java/org/apache/ibatis/annotations)
 **/
object MybatisAnnotations {
    val INSERT = "org.apache.ibatis.annotations.Insert"
    val DELETE = "org.apache.ibatis.annotations.Delete"
    val UPDATE = "org.apache.ibatis.annotations.Update"
    val SELECT = "org.apache.ibatis.annotations.Select"

    val INSERT_PROVIDER = "org.apache.ibatis.annotations.InsertProvider"
    val DELETE_PROVIDER = "org.apache.ibatis.annotations.DeleteProvider"
    val UPDATE_PROVIDER = "org.apache.ibatis.annotations.UpdateProvider"
    val SELECT_PROVIDER = "org.apache.ibatis.annotations.SelectProvider"

    val ALL_STATEMENTS = setOf(INSERT, DELETE, UPDATE, SELECT, INSERT_PROVIDER, DELETE_PROVIDER, UPDATE_PROVIDER, SELECT_PROVIDER)
}