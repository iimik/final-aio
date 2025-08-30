package org.ifinalframework.plugins.aio.mybatis.xml.generator

import org.ifinalframework.plugins.aio.mybatis.xml.dom.Insert
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.jetbrains.uast.UMethod

class InsertStatementGenerator : AbstractStatementGenerator<Insert>() {
    override fun generateStatement(mapper: Mapper, method: UMethod): Insert {
        return mapper.addInsert()
    }

    override fun getDisplayText(): String {
        return "Insert"
    }
}