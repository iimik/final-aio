package org.ifinalframework.plugins.aio.datasource.service

import com.intellij.database.dataSource.DatabaseConnectionManager
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.database.dialects.mysql.model.MysqlModel
import com.intellij.database.model.basic.BasicTableOrView
import com.intellij.database.view.DatabaseView
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

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

    fun getTables(prefix: String?): List<BasicTableOrView>{
        val localDataSourceManager = LocalDataSourceManager.getInstance(project)
        val dataSources = localDataSourceManager.dataSources

        val tables = mutableListOf<BasicTableOrView>()

        for (dataSource in dataSources) {
            val model = dataSource.model
            if(model is MysqlModel){
                tables.addAll(model.root.schemas.flatMap { it.tables })
            }
        }

        if(prefix != null && prefix.isNotEmpty()){
            return tables.stream()
                .filter { it.name.startsWith(prefix) }
                .toList()
        }


        return tables
    }

    fun getColumns(table: String): List<String>{
        val localDataSourceManager = LocalDataSourceManager.getInstance(project)
        for (dataSource in localDataSourceManager.dataSources) {
            println("")
        }
        return emptyList();
    }
}