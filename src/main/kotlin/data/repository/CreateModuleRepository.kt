package data.repository

import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project

class CreateModuleRepository(private val project: Project) {

    fun getCurrentModule(module: String) = ModuleManager.getInstance(project).findModuleByName(module)

}