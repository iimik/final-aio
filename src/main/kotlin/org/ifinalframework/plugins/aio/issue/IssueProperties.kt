package org.ifinalframework.plugins.aio.issue

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil


/**
 * IssueProperties
 *
 * @author iimik
 * @since 0.0.1
 **/
@Service(Service.Level.PROJECT)
@State(
    name = "org.ifinalframework.plugins.aio.issue.IssueProperties",
    storages = [Storage("final-aio.xml")]
)
data class IssueProperties(
    /**
     * @key 标签名称
     * @value URL格式
     */
    val jiraIssue: JiraIssueProperties = JiraIssueProperties(),
) : PersistentStateComponent<IssueProperties> {
    override fun getState(): IssueProperties? {
        return this
    }

    override fun loadState(properties: IssueProperties) {
        XmlSerializerUtil.copyBean<IssueProperties>(properties, this)
    }

    data class JiraIssueProperties(
        /**
         * 服务地址
         */
        var serverUrl: String? = null,
        /**
         * 项目编码
         */
        var projectCode: String? = null
    )
}

