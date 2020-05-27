package data.file

import data.repository.SettingsRepository
import model.*
import util.toSnakeCase
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption


interface FileCreator {

    fun createScreenFiles(packageName: String, featureName: String, androidComponent: AndroidComponent, rootDirectory: String)
    fun createFile(path: String, name: String, fileContent: String)
}

class FileCreatorImpl(private val settingsRepository: SettingsRepository) : FileCreator {

    override fun createScreenFiles(packageName: String, featureName: String, androidComponent: AndroidComponent, rootDirectory: String) {
        val featureModuleName = "feature_${featureName.split("[A-Z]".toRegex()).joinToString("_").toLowerCase()}"
        settingsRepository.loadSettings().apply {
            screenElements.forEach {
                val file = it.toFileTypeDescription()
                val path = file.defaultPath.replace(PACKAGE_PATH, packageName.split(".").joinToString("/")).replace(FEATURE_NAME, featureModuleName)
                file.defaultPath = "$rootDirectory/$featureModuleName" + (if (path.isNotEmpty()) "/" else "") + path
                file.defaultTemplate = file.defaultTemplate.replaceVariables(featureName, featureModuleName, packageName)
                file.displayName = file.displayName.replaceVariables(featureName, featureModuleName, packageName)
                createFile(file)
            }
        }
    }

    fun String.replaceVariables(featureName: String, featureModuleName: String, moduleName: String) =
            replace(Variable.NAME.value, featureName.capitalize())
                    .replace(Variable.NAME_SNAKE_CASE.value, featureName.toSnakeCase())
                    .replace(Variable.NAME_LOWER_CASE.value, featureName.decapitalize())
                    .replace(Variable.FEATURE_MODULE_NAME.value, featureModuleName)
                    .replace(Variable.MODULE_PACKAGE.value, moduleName)

    override fun createFile(path: String, name: String, fileContent: String) {
        val filePath = "$path/${name}"
        Files.write(Paths.get(filePath), fileContent.toByteArray(), StandardOpenOption.CREATE_NEW)
    }

    private fun createFile(file: FileTypeDescription) {
        File(file.defaultPath).mkdirs()
        createFile(file.defaultPath, "${file.displayName}.${file.description.extension}", file.defaultTemplate)
    }

    private fun Settings.getAndroidComponentBaseClass(androidComponent: AndroidComponent) = when (androidComponent) {
        AndroidComponent.ACTIVITY -> activityBaseClass
        AndroidComponent.FRAGMENT -> fragmentBaseClass
    }
}