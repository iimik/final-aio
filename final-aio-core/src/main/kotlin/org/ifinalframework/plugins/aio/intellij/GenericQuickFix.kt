package org.ifinalframework.plugins.aio.intellij;

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