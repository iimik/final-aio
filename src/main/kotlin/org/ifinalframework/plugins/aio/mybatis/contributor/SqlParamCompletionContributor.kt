package org.ifinalframework.plugins.aio.mybatis.contributor

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.ifinalframework.plugins.aio.mybatis.MapperUtils

/**
 * SQL 参数补全
 *
 * @issue 32
 * @author iimik
 * @since 0.0.7
 */
class SqlParamCompletionContributor : CompletionContributor() {

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (parameters.completionType != CompletionType.BASIC) {
            return
        }

        val position = parameters.position
        val topLevelFile = position.containingFile.context?.containingFile ?: return
        if (MapperUtils.isMybatisFile(topLevelFile)) {
            val shouldAddElement = shouldAddElement(position.containingFile, parameters.offset)
            if (shouldAddElement) {
                process(topLevelFile, result, position)
            }
        }
    }

    private fun process(xmlFile: PsiFile, result: CompletionResultSet, position: PsiElement) {
        val psiFile = position.containingFile
        val context = position.containingFile.context ?: return
        val idDomElement = MapperUtils.findParentIdDomElement(context) ?: return
        TestParamContributor.addElementForPsiParameter(position.project, result, idDomElement)
        result.stopHere()

//        val injectedLanguageManager = InjectedLanguageManager.getInstance(position.project)
//        val documentWindows = injectedLanguageManager.getCachedInjectedDocumentsInRange(psiFile, position.textRange)
//        val documentWindow = if (documentWindows.isEmpty()) null else documentWindows[0]
//        if (null != documentWindow) {
//            val offset = documentWindow.injectedToHost(position.textOffset)
//            val idDomElement: IdDomElement = MapperUtils.findParentIdDomElement(xmlFile.findElementAt(offset)) ?: return
//            TestParamContributor.addElementForPsiParameter(position.project, result, idDomElement)
//            result.stopHere()
//        }
    }


    private fun shouldAddElement(file: PsiFile, offset: Int): Boolean {
        val text = file.text
        for (i in offset - 1 downTo 1) {
            val c = text[i]
            if (c == '{' && text[i - 1] == '#') return true
        }
        return false
    }
}