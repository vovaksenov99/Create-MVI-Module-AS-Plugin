package ui.newscreen

import com.intellij.openapi.module.ModuleManager
import data.file.CurrentPath
import data.file.FileCreator
import data.file.PackageExtractor
import data.file.WriteActionDispatcher
import data.repository.CreateModuleRepository
import data.repository.ModuleRepository
import model.AndroidComponent
import java.io.File
import java.util.*
import java.util.logging.Handler

class NewScreenPresenter(private val view: NewScreenView,
                         private val fileCreator: FileCreator,
                         private val packageExtractor: PackageExtractor,
                         private val writeActionDispatcher: WriteActionDispatcher,
                         private val moduleRepository: ModuleRepository,
                         private val createModuleRepository: CreateModuleRepository,
                         private val currentPath: CurrentPath?) {

    fun onLoadView() {
        view.showPackage(packageExtractor.extractFromCurrentPath())
    }

    fun onOkClick(packageName: String, screenName: String, androidComponent: AndroidComponent, moduleName: String, rootPath: String) {
        writeActionDispatcher.dispatch {
            fileCreator.createScreenFiles(packageName, screenName, androidComponent, moduleName, rootPath)
        }
        Timer("SettingUp", false).schedule(object : TimerTask() {
            override fun run() {
                NewScreenDialog.runGradle("build", rootPath)

            }

        }, 1000)
        view.close()
    }

}