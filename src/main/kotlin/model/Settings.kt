package model

import java.io.Serializable

private fun defaultScreenElements() =
        getTemplates()
                .map {
                    it.toScreenElement()
                }
                .toMutableList()

fun FileTypeDescription.toScreenElement() = ScreenElement(displayName, description, defaultTemplate, defaultFileName, defaultPath)

fun ScreenElement.toFileTypeDescription() = FileTypeDescription(name, fileType, template, fileNameTemplate, path)

data class Settings(var screenElements: MutableList<ScreenElement> = defaultScreenElements()) : Serializable