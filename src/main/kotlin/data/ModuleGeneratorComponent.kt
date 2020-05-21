package data

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import model.Settings
import java.io.Serializable

@State(name = "ModuleGeneratorConfiguration",
        storages = [Storage(value = "moduleGeneratorConfiguration.xml")])
class ModuleGeneratorComponent : Serializable, PersistentStateComponent<ModuleGeneratorComponent> {

    companion object {
        fun getInstance(project: Project) = ServiceManager.getService(project, ModuleGeneratorComponent::class.java)
    }

    var settings: Settings = Settings()

    override fun getState(): ModuleGeneratorComponent = this

    override fun loadState(state: ModuleGeneratorComponent) {
        XmlSerializerUtil.copyBean(state, this)
    }
}