package org.ifinalframework.plugins.aio.mybatis.xml.generator.criterion

import org.ifinalframework.plugins.aio.mybatis.xml.model.Criterion
import org.ifinalframework.plugins.aio.mybatis.xml.model.CriterionType

/**
 * CriterionGenerators
 * 
 * @author iimik
 */
class CriterionGenerators : CriterionGenerator<Criterion> {

    private val generators = mutableMapOf<CriterionType, CriterionGenerator<Criterion>>()

    init {

        generators[CriterionType.EQUAL] = CommonCriterionGenerator.INSTANCE
        generators[CriterionType.NOT_EQUAL] = CommonCriterionGenerator.INSTANCE

        generators[CriterionType.BETWEEN] = BetweenCriterionGenerator.INSTANCE as CriterionGenerator<Criterion>
        generators[CriterionType.NOT_BETWEEN] = BetweenCriterionGenerator.INSTANCE as CriterionGenerator<Criterion>

        generators[CriterionType.IN] = InCriterionGenerator.INSTANCE as CriterionGenerator<Criterion>
        generators[CriterionType.NOT_IN] = InCriterionGenerator.INSTANCE as CriterionGenerator<Criterion>
    }

    override fun generate(criterion: Criterion): String {
        return generators[criterion.type()]!!.generate(criterion)
    }
}