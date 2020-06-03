package model

import data.file.featureTemplates
import data.file.testTemplates
import java.io.File

fun getTemplates(): MutableMap<ModuleType, MutableList<FileTypeDescription>> {
    val rez = mutableMapOf<ModuleType, MutableList<FileTypeDescription>>()
    ModuleType.values().forEach { type ->
        val template = when (type) {
            ModuleType.FEATURE -> featureTemplates
            ModuleType.TEST -> testTemplates
        }
        val files = mutableListOf<FileTypeDescription>()
        template.forEach {
            val file = File(it.name)
            if (File("${it.defaultPath}/${it.name}").extension == "") {
                files.add(FileTypeDescription(it.name, FileType.FOLDER, "", it.name, it.defaultPath))
            } else {
                val extension = file.extension
                val defaultPath = it.defaultPath
                val name = file.nameWithoutExtension
                val content = it.template.trimIndent()
                val fileType = when (extension) {
                    "kt" -> FileType.KOTLIN
                    "xml" -> FileType.XML
                    "kts" -> FileType.GRADLE
                    "gradle" -> FileType.GRADLE
                    "gitignore" -> FileType.GITIGNORE
                    else -> null
                }
                fileType?.let {
                    files.add(FileTypeDescription(name, fileType, content, name, defaultPath))
                }
            }
        }
        rez[type] = files
    }
    return rez
}

data class FileTypeDescription(var displayName: String, val description: FileType, var defaultTemplate: String, val defaultFileName: String, var defaultPath: String)

enum class FileType(val displayName: String, val extension: String) {
    KOTLIN("Kotlin", "kt"),
    XML("XML", "xml"),
    GRADLE("Gradle file", "kts"),
    GITIGNORE("Gitignore", "gitignore"),
    FOLDER("Folder", "");

    override fun toString() = displayName
}