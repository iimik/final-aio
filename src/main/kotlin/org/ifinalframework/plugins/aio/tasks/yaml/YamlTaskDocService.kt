package org.ifinalframework.plugins.aio.tasks.yaml

import com.intellij.openapi.components.Service
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.tasks.TaskDoc
import org.ifinalframework.plugins.aio.tasks.TaskDocService
import org.ifinalframework.plugins.aio.tasks.TaskUtils

/**
 * YamlTaskDocService
 * 
 * @author iimik
 */
@Service
class YamlTaskDocService: TaskDocService {
    override fun getTaskDoc(element: PsiElement): TaskDoc? {
        if(element !is PsiComment){
            return null
        }

        val value = element.text.removePrefix("#").trim()
        return parseLineIssue(value)
    }

    private fun parseLineIssue(comment: String): TaskDoc? {
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