package org.ifinalframework.plugins.aio.issue

import com.intellij.openapi.diagnostic.logger
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.ifinalframework.plugins.aio.application.condition.ConditionOnMarkdown
import org.springframework.stereotype.Component


/**
 * Markdown Issue 服务
 *
 * 提取Markdown文件中的Issue，格式`(#18)`
 *
 * @issue 18
 * @author iimik
 * @since 0.0.2
 * @see JvmIssueService
 **/
@Component
@ConditionOnMarkdown
class MarkdownIssueService : IssueService {

    override fun getIssue(element: PsiElement): Issue? {
        if (element is LeafPsiElement && element.elementType.toString().equals("Markdown:Markdown:TEXT", true)) {
            val input = element.text
            logger<MarkdownIssueService>().info("input: $input")
            if (Regex("^#\\d+").matches(input)) {
                return Issue(IssueType.ISSUE, input.trimStart('#'))
            }
        }
        return null
    }

}