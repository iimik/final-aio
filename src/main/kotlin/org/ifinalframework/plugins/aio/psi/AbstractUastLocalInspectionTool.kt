package org.ifinalframework.plugins.aio.psi;

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.visitor.AbstractUastVisitor


/**
 * AbstractUastLocalInspectionTool
 *
 * @author iimik
 * @since 0.0.4
 **/
abstract class AbstractUastLocalInspectionTool : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        val manager = holder.manager
        return UastElementVisitor(object : AbstractUastVisitor() {
            override fun visitElement(node: UElement): Boolean {
                addDescriptors(checkElement(node, manager, isOnTheFly))
                return true
            }

            override fun visitClass(node: UClass): Boolean {
                addDescriptors(checkClass(node, manager, isOnTheFly))
                return true
            }

            override fun visitMethod(node: UMethod): Boolean {
                addDescriptors(checkMethod(node, manager, isOnTheFly))
                return true
            }



            fun addDescriptors(descriptors: Array<ProblemDescriptor>?) {
                descriptors?.forEach {
                    holder.registerProblem(it)
                }
            }


        })
    }

    open fun checkElement(element: UElement, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        return null
    }

    open fun checkClass(clazz: UClass, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        return null
    }

    open fun checkMethod(method: UMethod, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        return null
    }

}