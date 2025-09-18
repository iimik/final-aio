package org.ifinalframework.plugins.aio.tasks

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.tasks.TaskManager
import com.intellij.util.ProcessingContext

/**
 * IssueJavaDocCompletionProvider
 * @author iimik
 */
class IssueDocCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {

        val project = parameters.position.project
        val taskManager = TaskManager.getManager(project)
        val tasks = taskManager.cachedIssues


        if (tasks.isNullOrEmpty()) {
            return
        }

        val taskDocProcessor = service<TaskDocProcessor>()
        tasks.mapNotNull { taskDocProcessor.buildTaskDoc(it) }
            .map {
                LookupElementBuilder.create("${it.tag} ${it.code} ${it.summary}")
                    .withIcon(it.icon)
                    .withCaseSensitivity(false)
            }
            .forEach { result.addElement(it) }

    }
}