package org.ifinalframework.plugins.aio.datasource.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.database.dataSource.DatabaseConnectionManager
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.database.view.DatabaseView
import com.intellij.sql.database.SqlDataSourceManager
import com.intellij.util.ProcessingContext
import org.ifinalframework.plugins.aio.R

/**
 * DataSourceJavaDocCompletionContributor
 *
 * @author iimik
 */
class DataSourceDocCompletionProvider: CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val project = parameters.position.project
        val localDataSourceManager = LocalDataSourceManager.getInstance(project)
//        val dataSources = localDataSourceManager.dataSources
//        val databaseConnectionManager = DatabaseConnectionManager.getInstance()


        val dataSources = SqlDataSourceManager.getInstance(project).dataSources

    }
}