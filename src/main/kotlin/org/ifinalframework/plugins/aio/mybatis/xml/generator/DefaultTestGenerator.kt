package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiType
import com.intellij.psi.impl.source.PsiClassReferenceType
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * DefaultTestGenerator
 *
 * @author iimik
 */

class DefaultTestGenerator : TestGenerator {
    private val TEST_COMPLETION_PLACE_HOLDER = "\${TARGET}"
    private val TEST_COMPLETION_START_PLACE_HOLDER = "\${START_TARGET}"
    private val TEST_COMPLETION_END_PLACE_HOLDER = "\${END_TARGET}"
    override fun generate(project: Project, psiType: PsiType, prefix: String?, name: String): String {
        val myBatisProperties = project.service<MyBatisProperties>()
        val testCompletion = myBatisProperties.testCompletion
        val param = Stream.of(prefix, name).filter { Objects.nonNull(it) }.collect(Collectors.joining("."))

        if (psiType is PsiClassReferenceType) {
            val className = psiType.reference.qualifiedName
            if ("java.lang.String" == className) {
                return testCompletion.stringType.replace(TEST_COMPLETION_PLACE_HOLDER, param)
            } else if ("java.util.List" == className || "java.util.Set" == className) {
                return testCompletion.collectionType.replace(TEST_COMPLETION_PLACE_HOLDER, param)
            }
        }

        return testCompletion.defaultType.replace(TEST_COMPLETION_PLACE_HOLDER, param)
    }
}