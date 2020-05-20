package ui.newscreen

import data.file.FileCreator
import data.file.WriteActionDispatcher
import model.AndroidComponent
import java.util.*

class NewScreenPresenter(
        private val view: NewScreenView,
        private val fileCreator: FileCreator,
        private val writeActionDispatcher: WriteActionDispatcher
) {

    companion object {

        const val DELAY_GRADLE_SYNC = 2000L

    }

    fun onLoadView() {
        view.showPackage("com.touchin.sample")
    }

    fun onOkClick(packageName: String, screenName: String, androidComponent: AndroidComponent, moduleName: String, rootDirectory: String) {
        writeActionDispatcher.dispatch {
            fileCreator.createScreenFiles(packageName, screenName, androidComponent, moduleName, rootDirectory)
        }
        view.close()
    }

}