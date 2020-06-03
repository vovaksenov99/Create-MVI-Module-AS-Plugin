package model

import java.io.Serializable

private const val UNNAMED_ELEMENT = "UnnamedElement"

data class ScreenElement(var name: String, var fileType: FileType, var template: String, var fileNameTemplate: String, var path: String) : Serializable {

    override fun toString() = if (fileType == FileType.FOLDER) name else "$name.${fileType.extension}"

    companion object {
        fun getDefault() = ScreenElement(UNNAMED_ELEMENT, FileType.KOTLIN, "", "Kotlin", "")
    }

}