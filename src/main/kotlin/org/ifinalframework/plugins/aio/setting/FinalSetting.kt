package org.ifinalframework.plugins.aio.setting

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

/**
 * FinalPersistentStateComponent
 *
 * @author iimik
 */
@Service(Service.Level.PROJECT)
@State(
    name = "org.ifinalframework.plugins.aio.setting.FinalSetting",
    storages = [Storage("Final-AIO.xml")]
)
class FinalSetting : SerializablePersistentStateComponent<FinalProperties>(FinalProperties()) {

    companion object {
        fun getInstance(project: Project): FinalSetting = project.service<FinalSetting>()
    }


}