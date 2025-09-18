package org.ifinalframework.plugins.aio.tasks

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.logger
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.ifinalframework.plugins.aio.resource.AllIcons


/**
 * Markdown Issue 服务
 *
 * 提取Markdown文件中的Issue，格式`(#18)`
 *
 * @issue 18
 * @author iimik
 * @since 0.0.2
 * @see JvmTaskDocService
 **/
@Service
class MarkdownTaskDocService : TaskDocService {

    override fun getTaskDoc(element: PsiElement): TaskDoc? {
        if (element is LeafPsiElement && element.elementType.toString().equals("Markdown:Markdown:TEXT", true)) {
            val input = element.text
            logger<MarkdownTaskDocService>().info("input: $input")
            if (Regex("^#\\d+").matches(input)) {
                return TaskDoc("issue", input.trimStart('#'), "", AllIcons.Issues.ISSUE)
            }
        }
        return null
    }

}