package model

import java.io.Serializable

private const val UNNAMED_ELEMENT = "UnnamedElement"

data class ScreenElement(var name: String, var fileType: FileType, var template: String, var fileNameTemplate: String, var defaultPath: String) : Serializable {

    override fun toString() = name

    companion object {
        fun getDefault() = ScreenElement(UNNAMED_ELEMENT, FileType.KOTLIN, "", "Kotlin", "")
    }

}