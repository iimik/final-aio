package org.ifinalframework.plugins.aio.issue;

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.application.condition.ConditionOnJvm
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.util.SpiUtil
import org.springframework.stereotype.Component


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
 * @see MarkdownIssueService
 **/
@Component
@ConditionOnJvm
class JvmIssueService(
    private val docService: DocService,
) : IssueService {

    constructor() : this(SpiUtil.languageSpi<DocService>())

    override fun getIssue(element: PsiElement): Issue? {
        // @issue 10 docTag issue
        getDocTagIssue(element)?.let { return it }
        // #10 line issue
        getLineIssue(element)?.let { return it }
        return null
    }

    /**
     * 文档注释
     */
    private fun getDocTagIssue(element: PsiElement): Issue? {
        val name = docService.getTagName(element) ?: return null
        val value = docService.getTagValue(element) ?: return null
        return parseDocTagIssue(name, value)

    }

    /**
     * 行内注释
     */
    private fun getLineIssue(element: PsiElement): Issue? {
        val comment = docService.getLineComment(element)?.trim() ?: return null
        return parseLineIssue(comment);
    }

    private fun parseDocTagIssue(name: String, value: String): Issue? {
        val issueType = IssueType.ofNullable(name) ?: return null
        val code = value.substringBefore(" ")
        val description = value.substringAfter(" ")
        return Issue(issueType, code, description)
    }

    private fun parseLineIssue(comment: String): Issue? {
        val comment = comment.removePrefix("//").trimStart()
        val issueType = parseLineIssueType(comment) ?: return null
        val code = parseLineIssueCode(comment) ?: return null
        val description = comment.substringAfter(code).trim()
        return Issue(issueType, code, description)
    }

    private fun parseLineIssueType(comment: String): IssueType? {
        return if (comment.startsWith("#")) {
            IssueType.ISSUE
        } else if (comment.startsWith("@issue")) {
            IssueType.ISSUE
        } else if (comment.startsWith("@jira")) {
            IssueType.JIRA
        } else null
    }

    private fun parseLineIssueCode(comment: String): String? {
        return if (comment.startsWith("#")) {
            comment.substringAfter("#").trimStart().substringBefore(" ")
        } else if (comment.startsWith("@issue")) {
            comment.substringAfter("@issue").trimStart().substringBefore(" ")
        } else if (comment.startsWith("@jira")) {
            comment.substringAfter("@jira").trimStart().substringBefore(" ")
        } else null
    }


}