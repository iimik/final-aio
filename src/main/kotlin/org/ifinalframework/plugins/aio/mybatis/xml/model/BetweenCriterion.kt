package org.ifinalframework.plugins.aio.mybatis.xml.model

import com.intellij.psi.PsiElement

/**
 * BetweenCriterion
 * 
 * @author iimik
 */
class BetweenCriterion(
    val andOr: AndOr,
    val type: CriterionType,
    /**
     * 前缀，如`@Param`声明
     */
    val prefix: String?,
    /**
     * 起始参数
     */
    val start: PsiElement,
    /**
     * 结束参数
     */
    val end: PsiElement,
    /**
     * 是否必要
     */
    val required: Boolean = false
) : Criterion {
    override fun type(): CriterionType {
        return type
    }
}