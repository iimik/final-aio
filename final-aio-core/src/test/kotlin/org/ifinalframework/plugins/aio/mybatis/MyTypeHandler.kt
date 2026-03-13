package org.ifinalframework.plugins.aio.mybatis

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class MyTypeHandler:BaseTypeHandler<Any>() {
    override fun setNonNullParameter(ps: PreparedStatement?, i: Int, parameter: Any?, jdbcType: JdbcType?) {
        TODO("Not yet implemented")
    }

    override fun getNullableResult(rs: ResultSet?, columnName: String?): Any {
        TODO("Not yet implemented")
    }

    override fun getNullableResult(rs: ResultSet?, columnIndex: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getNullableResult(cs: CallableStatement?, columnIndex: Int): Any {
        TODO("Not yet implemented")
    }
}