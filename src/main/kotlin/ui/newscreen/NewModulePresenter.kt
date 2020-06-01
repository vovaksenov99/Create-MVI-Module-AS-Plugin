package ui.newscreen

import data.file.FileCreator
import data.file.WriteActionDispatcher
import model.ModuleTypes
import org.jetbrains.kotlin.utils.addToStdlib.firstNotNullResult
import java.io.File

class NewModulePresenter(
        private val view: NewModuleView,
        private val fileCreator: FileCreator,
        private val writeActionDispatcher: WriteActionDispatcher
) {

    companion object {

        const val DELAY_GRADLE_SYNC = 2000L

    }

    fun onLoadView(rootDirectory: String?) {
        view.showPackage(getPackageName(rootDirectory))
    }

    private fun checkPackageName(path: String): String? {
        var path = path
        var rez = mutableListOf<String>()
        if (File(path).exists()) {
            while (File(path).list()?.size == 1) {
                val dir = File(path).listFiles()?.firstOrNull() ?: return null
                path += "/${dir.name}"
                rez.add(dir.name)
            }
            return rez.joinToString(".")
        }
        return null
    }

    fun getPackageName(rootDirectory: String?): String {
        return listOf(
                "$rootDirectory/app/src/main/java",
                "$rootDirectory/app/src/main/kotlin"
        ).map {
            checkPackageName(it)
        }.firstNotNullResult { it } ?: "com.touchin.sample"
    }

    fun onOkClick(packageName: String, featureName: String, moduleTypes: ModuleTypes, rootDirectory: String) {
        writeActionDispatcher.dispatch {
            fileCreator.createScreenFiles(packageName, featureName, moduleTypes, rootDirectory)
        }
        view.close()
    }

}