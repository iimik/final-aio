package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiType

/**
 * TestGenerator
 *
 * @author iimik
 */
interface TestGenerator {
    fun generate(project: Project, psiType: PsiType, prefix: String?, name: String): String
}