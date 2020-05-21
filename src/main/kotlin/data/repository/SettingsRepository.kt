package data.repository

import com.intellij.openapi.project.Project
import data.ModuleGeneratorComponent
import model.Settings

interface SettingsRepository {
    fun loadSettings(): Settings
    fun update(settings: Settings)
}

class SettingsRepositoryImpl(private val project: Project) : SettingsRepository {

    override fun loadSettings() = ModuleGeneratorComponent.getInstance(project).settings

    override fun update(settings: Settings) = ModuleGeneratorComponent.getInstance(project).run {
        this.settings = settings
    }
}