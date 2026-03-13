package org.ifinalframework.plugins.aio.mybatis.xml.generator.criterion

/**
 * CriterionGenerator
 * 
 * @author iimik
 * @see CommonCriterionGenerator
 * @see BetweenCriterionGenerator
 * @see InCriterionGenerator
 */
interface CriterionGenerator<in Criterion> {
    fun generate(criterion: Criterion): String
}


