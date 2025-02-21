package org.ifinalframework.plugins.aio.mybatis.inspection;

import com.intellij.codeInspection.LocalQuickFix


/**
 * GenericQuickFix
 *
 * @author iimik
 * @since 0.0.4
 **/
abstract class GenericQuickFix:LocalQuickFix {
    override fun getFamilyName(): String {
        return this.javaClass.simpleName
    }
}