package org.ifinalframework.plugins.aio.datasource.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.openapi.components.service
import com.intellij.util.ProcessingContext
import org.ifinalframework.plugins.aio.datasource.service.DataSourceService

/**
 * DataSourceJavaDocCompletionContributor
 *
 * @author iimik
 */
class DataSourceDocCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val project = parameters.position.project

        val sourceService = project.service<DataSourceService>()

        print("")

    }
}