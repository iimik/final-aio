package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.datasource.model.Table

/**
 * 列名生成器
 *
 * 1. 通过`@column`注释读取
 * 2. 驼峰转下划线
 *
 * @author iimik
 */
interface ColumnGenerator {
    /**
     *
     */
    fun generate(project: Project, psiElement: PsiElement, table: Table?): String
}