package org.ifinalframework.plugins.aio.mybatis.inspection;

import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import org.ifinalframework.plugins.aio.intellij.GenericQuickFix
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.xml.generator.*
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getContainingUClass
import java.util.stream.Collectors


/**
 * StatementNotExistsQuickFix
 *
 * @issue 43
 * @author iimik
 * @since 0.0.10
 **/
class StatementNotExistsQuickFix(val method: UMethod) : GenericQuickFix() {

    private val statementGenerators: Map<String, StatementGenerator> = listOf(
        InsertStatementGenerator(),
        UpdateStatementGenerator(),
        DeleteStatementGenerator(),
        SelectStatementGenerator()
    ).stream().collect(Collectors.toMap({ it.toString().lowercase() }, { it }))


    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        method.getContainingUClass() ?: return


        val generators = findStatementGenerators(project)

        if (generators.size == 1) {
            val generator = generators.first()
            generator.generate(project, method)
        } else {
            JBPopupFactory.getInstance().createListPopup(
                object : BaseListPopupStep<StatementGenerator>(
                    "[ Statement type for method: " + method.name + "]",
                    generators,
                    AllIcons.Mybatis.JVM
                ) {
                    override fun onChosen(selectedValue: StatementGenerator, finalChoice: Boolean): PopupStep<*>? {
                        WriteCommandAction.runWriteCommandAction(project) {
                            selectedValue.generate(project, method)
                        }
                        return PopupStep.FINAL_CHOICE
                    }
                }
            ).showInFocusCenter()
        }
    }

    private fun findStatementGenerators(project: Project): List<StatementGenerator> {

        val statementMethodCompletion = project.service<MyBatisProperties>().statementMethodCompletion
        if (statementMethodCompletion.filterWithRegex) {
            if (method.name.matches(Regex(statementMethodCompletion.insertMethodRegex))) {
                return listOf(statementGenerators["insert"]!!)
            } else if (method.name.matches(Regex(statementMethodCompletion.deleteMethodRegex))) {
                return listOf(statementGenerators["delete"]!!)
            } else if (method.name.matches(Regex(statementMethodCompletion.updateMethodRegex))) {
                return listOf(statementGenerators["update"]!!)
            } else if (method.name.matches(Regex(statementMethodCompletion.selectMethodRegex))) {
                return listOf(statementGenerators["select"]!!)
            }
        }


        return statementGenerators.values.toList()
    }

    override fun getName(): String {
        return I18N.message("MyBatis.StatementNotExistsQuickFix.name")
    }


}