package org.ifinalframework.plugins.aio.javadoc

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext
import org.ifinalframework.plugins.aio.datasource.completion.DataSourceDocCompletionProvider
import org.ifinalframework.plugins.aio.issue.IssueDocCompletionProvider
import org.ifinalframework.plugins.aio.mybatis.completion.MyBatisDocCompletionProvider

/**
 * JavaDocCompletionContributor
 * @author iimik
 */
class DocCompletionProviders : CompletionProvider<CompletionParameters>() {

    private val providers = listOf(
        IssueDocCompletionProvider(),
        MyBatisDocCompletionProvider(),
        DataSourceDocCompletionProvider(),
    )

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        providers.forEach { it.addCompletionVariants(parameters, context, result) }
    }
}