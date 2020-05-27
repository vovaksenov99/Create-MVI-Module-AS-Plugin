package model

import data.file.templates
import java.io.File

fun getTemplates(): MutableList<FileTypeDescription> {
    val rez = mutableListOf<FileTypeDescription>()
    templates.forEach {
        val file = File(it.name)
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
            rez.add(FileTypeDescription(name, fileType, content, name, defaultPath))
        }
    }
    return rez
}

data class FileTypeDescription(var displayName: String, val description: FileType, var defaultTemplate: String, val defaultFileName: String, var defaultPath: String)

enum class FileType(val displayName: String, val extension: String) {
    KOTLIN("Kotlin", "kt"),
    XML("XML", "xml"),
    GRADLE("Gradle file", "kts"),
    GITIGNORE("Gitignore", "gitignore");

    override fun toString() = displayName
}