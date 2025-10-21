package org.ifinalframework.plugins.aio.tasks

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.psi.service.DocService


/**
 * Issue 服务
 *
 * 提取注释中的issue，支持以下格式的注释：
 *
 * - 文档注释
 *    - @jira 编号 [描述]
 *    - @issue 编号 [描述]
 * - 行注释
 *    - @jira 编号 [描述]
 *    - @issue 编号 [描述]
 *    - #编号 [描述]
 *
 * @jira 1
 * @issue 10
 * @author iimik
 * @since 0.0.1
 * @see MarkdownTaskDocService
 **/
@Service
class JvmTaskDocService : TaskDocService {

    private val docService = service<DocService>()

    override fun getTaskDoc(element: PsiElement): TaskDoc? {
        // @issue 10 docTag issue
        getDocTagIssue(element)?.let { return it }
        // #10 line issue
        getLineIssue(element)?.let { return it }
        return null
    }

    /**
     * 文档注释
     */
    private fun getDocTagIssue(element: PsiElement): TaskDoc? {
        val name = docService.getTagName(element) ?: return null
        val value = docService.getTagValue(element) ?: return null
        return parseDocTagIssue(name, value)

    }

    /**
     * 行内注释
     */
    private fun getLineIssue(element: PsiElement): TaskDoc? {
        val comment = docService.getLineComment(element)?.trim() ?: return null
        return parseLineIssue(comment)
    }

    private fun parseDocTagIssue(name: String, value: String): TaskDoc? {
        val issueType = TaskUtils.getTaskType(name) ?: return null
        val code = value.substringBefore(" ")
        val description = value.substringAfter(" ")
        return TaskDoc(name, code, description, issueType.icon)
    }

    private fun parseLineIssue(comment: String): TaskDoc? {
        val comment = comment.removePrefix("//").trimStart()
        val tagName = parseLineIssueType(comment) ?: return null
        val issueType = TaskUtils.getTaskType(tagName) ?: return null
        val code = parseLineIssueCode(comment) ?: return null
        val description = comment.substringAfter(code).trim()
        return TaskDoc(tagName, code, description, issueType.icon)
    }

    private fun parseLineIssueType(comment: String): String? {
        return if (comment.startsWith("#")) {
            "issue"
        } else if (comment.startsWith("@")) {
            return comment.substringBefore(" ").trimStart('@')
        } else null
    }

    private fun parseLineIssueCode(comment: String): String? {
        return if (comment.startsWith("#")) {
            comment.substringAfter("#").trimStart().substringBefore(" ")
        } else if (comment.startsWith("@")) {
            comment.substringAfter(" ").trimStart().substringBefore(" ")
        } else null
    }


}