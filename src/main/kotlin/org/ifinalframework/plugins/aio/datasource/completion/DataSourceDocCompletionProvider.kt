package org.ifinalframework.plugins.aio.datasource.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.database.dataSource.DatabaseConnectionManager
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.util.ProcessingContext

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
        val dataSources = localDataSourceManager.dataSources
        for (connection in DatabaseConnectionManager.getInstance().activeConnections) {
            val remoteConnection = connection.remoteConnection
            val statement = remoteConnection.createStatement()
            statement.executeQuery("SELECT * FROM information_schema.TABLES;")

        }
    }
}