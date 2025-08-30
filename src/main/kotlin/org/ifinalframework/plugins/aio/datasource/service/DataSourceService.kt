package org.ifinalframework.plugins.aio.datasource.service

import com.intellij.database.dataSource.DatabaseConnectionManager
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.database.dialects.mysql.model.MysqlModel
import com.intellij.database.model.ObjectKind
import com.intellij.database.model.basic.BasicTableOrView
import com.intellij.database.view.DatabaseView
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.datasource.model.Table

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

    fun getTables(prefix: String?): List<Table>{
        val localDataSourceManager = LocalDataSourceManager.getInstance(project)
        val dataSources = localDataSourceManager.dataSources
        val shardingTableService = service<ShardingTableService>()

        var basicTableOrViews = mutableListOf<BasicTableOrView>()

        for (dataSource in dataSources) {
            val schemes = HashSet<String>()
            val root = dataSource.introspectionScope.root
            root.groups.filter { it.kind == ObjectKind.SCHEMA }
                .filter { it.children != null }
                .forEach {
                    it.children?.forEach { child ->
                        child.naming.names.forEach { name ->
                            schemes.add(name.name)
                        }
                    }
                }
            val model = dataSource.model
            if(model is MysqlModel){
                model.root.schemas.filter { schemes.contains(it.name) }
                    .forEach {
                        basicTableOrViews.addAll(it.tables)
                    }
            }
        }

        if(prefix != null && prefix.isNotEmpty()){
            basicTableOrViews =  basicTableOrViews.stream()
                .filter { it.name.startsWith(prefix) }
                .toList()
        }

        val map = basicTableOrViews.groupBy { shardingTableService.getLogicTable(it.name) }

        val tables = map.entries
            .map { Table(it.key, it.value) }
            .toList()

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