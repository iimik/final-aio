package org.ifinalframework.plugins.aio.mybatis.xml.model

import com.intellij.psi.PsiElement

/**
 * Criterion
 *
 * @author iimik
 */
data class SimpleCriterion(
    val andOr: AndOr,
    val type: CriterionType,
    val prefix: String?,
    val param: PsiElement,
    val required: Boolean = false
) : Criterion {
    override fun type(): CriterionType {
        return type
    }
}