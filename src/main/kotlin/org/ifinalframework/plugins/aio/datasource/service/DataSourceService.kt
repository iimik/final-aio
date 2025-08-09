package org.ifinalframework.plugins.aio.datasource.service

import com.intellij.database.dataSource.DatabaseConnectionManager
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.database.view.DatabaseView
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager

/**
 * DataSourceService
 *
 * @author iimik
 */
@Service(Service.Level.PROJECT)
class DataSourceService(
    private val project: Project
) {
    public fun getSchemas(): List<String>{

        val databaseView = DatabaseView.getDatabaseView(project)

        val activeConnections = DatabaseConnectionManager.getInstance().activeConnections

        val localDataSourceManager = LocalDataSourceManager.getInstance(project)
        val dataSources = localDataSourceManager.dataSources
        for (connection in activeConnections) {
            val remoteConnection = connection.remoteConnection
            val statement = remoteConnection.createStatement()
            statement.executeQuery("SELECT * FROM information_schema.TABLES;")

        }
        return emptyList();
    }
}