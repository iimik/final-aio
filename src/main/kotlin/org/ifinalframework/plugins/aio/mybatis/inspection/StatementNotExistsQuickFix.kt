package org.ifinalframework.plugins.aio.mybatis.inspection;

import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.intellij.GenericQuickFix
import org.ifinalframework.plugins.aio.resource.I18N
import org.jetbrains.uast.UMethod


/**
 * StatementNotExistsQuickFix
 *
 * @author iimik
 * @since 0.0.4
 **/
class StatementNotExistsQuickFix(method: UMethod) : GenericQuickFix() {
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        return I18N.message("MyBatis.StatementNotExistsQuickFix.name")
    }
}