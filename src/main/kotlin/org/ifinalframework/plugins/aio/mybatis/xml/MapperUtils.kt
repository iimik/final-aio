package org.ifinalframework.plugins.aio.mybatis.xml

import com.intellij.database.util.common.isNotNullOrEmpty
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomUtil
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.ifinalframework.plugins.aio.psi.service.DocService

/**
 * MapperUtils
 *
 * @author iimik
 */
object MapperUtils {

    fun getTableName(project: Project, domElement: DomElement): String? {
        val mapper = DomUtil.getParentOfType(domElement, Mapper::class.java, true) ?: return null
        // 首先，查询有没有<sql id="table">这样的声明
        val myBatisProperties = project.service<MyBatisProperties>()
        val tableSqlFragmentIds = myBatisProperties.tableSqlFragment.ids.split(",")
        val tableSqls = mapper.getSqls().filter { tableSqlFragmentIds.contains(it.getId().value) }.toList()
        if (tableSqls.isNotEmpty()) {
            val tableName = tableSqls.get(0).getValue()
            if (tableName.isNotNullOrEmpty) {
                return tableName
            }
        }

        // 其次，根据<ResultMap type="class">进行推断
        val resultMaps = mapper.getResultMaps()
        if (resultMaps.isEmpty()) return null

        val resultMap = resultMaps[0]
        val entityClass = resultMap.getType().value ?: return null

        val docService = service<DocService>()
        val tableTag = docService.findTagValueByTag(entityClass, "table")
        if (tableTag.isNotNullOrEmpty) {
            return tableTag
        }

        return null
    }

}