package data.file

import data.repository.SettingsRepository
import model.AndroidComponent
import model.FileType
import model.Settings
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption


interface FileCreator {

    fun createScreenFiles(packageName: String, screenName: String, androidComponent: AndroidComponent, moduleName: String, rootDirectory: String)
    fun createFile(path: String, name: String, fileContent: String)
}

class FileCreatorImpl(private val settingsRepository: SettingsRepository) : FileCreator {

    private var createdModuleDirs = mapOf<String, String>()

    override fun createScreenFiles(packageName: String, screenName: String, androidComponent: AndroidComponent, moduleName: String, rootDirectory: String) {
        createModuleFolders(rootDirectory, moduleName, packageName)
        settingsRepository.loadSettings().apply {
            val baseClass = getAndroidComponentBaseClass(androidComponent)
            screenElements.forEach {
                val file = File(it.fileName(screenName, packageName, androidComponent.displayName, baseClass), it.body(screenName, packageName, androidComponent.displayName, baseClass), it.fileType)
                val fileDir = when (it.fileType) {
                    FileType.KOTLIN -> createdModuleDirs["package"]
                    FileType.LAYOUT_XML -> createdModuleDirs["layout"]
                    FileType.GRADLE -> createdModuleDirs["module"]
                    FileType.ANDROID_MANIFEST -> createdModuleDirs["main"]
                }
                createFile(fileDir ?: throw IllegalStateException("Path for ${it.name} doesn't exist"), file)
            }
        }
        Files.write(Paths.get("$rootDirectory/settings.gradle"), "\ninclude ':$moduleName'".toByteArray(), StandardOpenOption.APPEND)
    }

    override fun createFile(path: String, name: String, fileContent: String) {
        val filePath = "$path/${name}"
        Files.write(Paths.get(filePath), fileContent.toByteArray(), StandardOpenOption.CREATE_NEW)
    }

    private fun createFile(path: String, file: File) {
        createFile(path, "${file.name}.${file.fileType.extension}", file.content)
    }

    private fun createModuleFolders(rootDirectory: String, moduleName: String, packageName: String) {
        createdModuleDirs = mapOf(
                "module" to "$rootDirectory/$moduleName",
                "src" to "$rootDirectory/$moduleName/src",
                "main" to "$rootDirectory/$moduleName/src/main",
                "kotlin" to "$rootDirectory/$moduleName/src/main/kotlin",
                "package" to "$rootDirectory/$moduleName/src/main/kotlin/$packageName",
                "res" to "$rootDirectory/$moduleName/src/main/res",
                "layout" to "$rootDirectory/$moduleName/src/main/res/layout"
        )
        createdModuleDirs.forEach { java.io.File(it.value).mkdir() }
    }

    private fun Settings.getAndroidComponentBaseClass(androidComponent: AndroidComponent) = when (androidComponent) {
        AndroidComponent.ACTIVITY -> activityBaseClass
        AndroidComponent.FRAGMENT -> fragmentBaseClass
    }
}