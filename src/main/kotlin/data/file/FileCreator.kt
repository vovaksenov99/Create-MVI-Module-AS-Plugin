package data.file

import data.repository.SettingsRepository
import data.repository.SourceRootRepository
import model.AndroidComponent
import model.FileType
import model.Settings
import ui.newscreen.NewScreenDialog
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption


private const val LAYOUT_DIRECTORY = "layout"

interface FileCreator {

    fun createScreenFiles(packageName: String, screenName: String, androidComponent: AndroidComponent, moduleName: String, rootDirectory: String)
}

class FileCreatorImpl(private val settingsRepository: SettingsRepository,
                      private val sourceRootRepository: SourceRootRepository) : FileCreator {

    override fun createScreenFiles(packageName: String, screenName: String, androidComponent: AndroidComponent, moduleName: String, rootDirectory: String) {
        java.io.File("$rootDirectory/$moduleName").mkdir()
        java.io.File("$rootDirectory/$moduleName/src").mkdir()
        java.io.File("$rootDirectory/$moduleName/src/main").mkdir()
        java.io.File("$rootDirectory/$moduleName/src/main/kotlin").mkdir()
        java.io.File("$rootDirectory/$moduleName/src/main/kotlin/$packageName").mkdir()
        java.io.File("$rootDirectory/$moduleName/src/main/res").mkdir()
        java.io.File("$rootDirectory/$moduleName/src/main/res/layout").mkdir()
        java.io.File("$rootDirectory/$moduleName/build.gradle").createNewFile()
            settingsRepository.loadSettings().apply {
                val baseClass = getAndroidComponentBaseClass(androidComponent)
                screenElements.forEach {
                    if (it.fileType == FileType.LAYOUT_XML) {
                        val file = File(it.fileName(screenName, packageName, androidComponent.displayName, baseClass), it.body(screenName, packageName, androidComponent.displayName, baseClass), it.fileType)
                        java.io.File("$rootDirectory/$moduleName/src/${file.name}.xml").createNewFile()
                        val writer = PrintWriter("$rootDirectory/$moduleName/src/${file.name}.xml", "UTF-8")
                        writer.println(file.content)
                        writer.close()
                    } else {
                        val file = File(it.fileName(screenName, packageName, androidComponent.displayName, baseClass), it.body(screenName, packageName, androidComponent.displayName, baseClass), it.fileType)
                        java.io.File("$rootDirectory/$moduleName/src/${file.name}.kt").createNewFile()
                        val writer = PrintWriter("$rootDirectory/$moduleName/src/${file.name}.kt", "UTF-8")
                        writer.println(file.content)
                        writer.close()
                    }
                }
            }

        try {
            Files.write(Paths.get("$rootDirectory/settings.gradle"), "\ninclude ':$moduleName'".toByteArray(), StandardOpenOption.APPEND)
        } catch (e: IOException) {
            //exception handling left as an exercise for the reader
        }

    }

    private fun findCodeSubdirectory(packageName: String, module: String): Directory? = sourceRootRepository.findCodeSourceRoot(module)?.run {
        var subdirectory = directory
        packageName.split(".").forEach {
            subdirectory = subdirectory.findSubdirectory(it) ?: subdirectory.createSubdirectory(it)
        }
        return subdirectory
    }

    private fun getRootDirectory(packageName: String, module: String): Directory? =
            sourceRootRepository.findRootDirectory(module)?.run {
                var subdirectory = directory
                packageName.split(".").forEach {
                    subdirectory = subdirectory.findSubdirectory(it) ?: subdirectory.createSubdirectory(it)
                }
                return subdirectory
            }

    private fun findResourcesSubdirectory(module: String) = sourceRootRepository.findResourcesSourceRoot(module).directory.run {
        findSubdirectory(LAYOUT_DIRECTORY) ?: createSubdirectory(LAYOUT_DIRECTORY)
    }

    private fun Settings.getAndroidComponentBaseClass(androidComponent: AndroidComponent) = when (androidComponent) {
        AndroidComponent.ACTIVITY -> activityBaseClass
        AndroidComponent.FRAGMENT -> fragmentBaseClass
    }
}