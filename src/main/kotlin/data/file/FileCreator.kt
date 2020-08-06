package data.file

import data.repository.SettingsRepository
import model.*
import util.replaceVariables
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption


interface FileCreator {

    fun createScreenFiles(packageName: String, featureName: String, moduleType: ModuleType, rootDirectory: String)
    fun createFile(path: String, name: String, fileContent: String)
}

class FileCreatorImpl(private val settingsRepository: SettingsRepository) : FileCreator {

    override fun createScreenFiles(packageName: String, featureName: String, moduleType: ModuleType, rootDirectory: String) {
        val featureModuleName = "feature_${featureName.split("[A-Z]".toRegex()).joinToString("_").toLowerCase()}"
        settingsRepository.loadSettings().apply {
            screenElements[moduleType]?.forEach {
                val file = it.toFileTypeDescription()
                val path = file.defaultPath
                        .replace(Variable.MODULE_PACKAGE.value, packageName.split(".").joinToString("/"))
                        .replaceVariables(featureName, featureModuleName, packageName)
                file.defaultPath = "$rootDirectory/$featureModuleName" + (if (path.isNotEmpty()) "/" else "") + path
                if (file.description == FileType.FOLDER) {
                    File("${file.defaultPath}/${file.defaultFileName}/").mkdirs()
                } else {
                    file.defaultTemplate = file.defaultTemplate.replaceVariables(featureName, featureModuleName, packageName)
                    file.displayName = file.displayName.replaceVariables(featureName, featureModuleName, packageName)
                    createFile(file)
                }
            }
        }
    }

    override fun createFile(path: String, name: String, fileContent: String) {
        val filePath = "$path/${name}"
        Files.write(Paths.get(filePath), fileContent.toByteArray(), StandardOpenOption.CREATE_NEW)
    }

    private fun createFile(file: FileTypeDescription) {
        File(file.defaultPath).mkdirs()
        createFile(file.defaultPath, "${file.displayName}.${file.description.extension}", file.defaultTemplate)
    }

}