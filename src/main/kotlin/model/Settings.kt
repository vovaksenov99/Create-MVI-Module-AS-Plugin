package model

import java.io.Serializable

fun defaultScreenElements() =
        getTemplates()
                .mapValues { mapElement ->
                    mapElement.value.map { it.toScreenElement() }.toMutableList()
                }.toMutableMap()

fun FileTypeDescription.toScreenElement() = ScreenElement(displayName, description, defaultTemplate, defaultFileName, defaultPath)

fun ScreenElement.toFileTypeDescription() = FileTypeDescription(name, fileType, template, fileNameTemplate, path)

data class Settings(var screenElements: MutableMap<ModuleType, MutableList<ScreenElement>> = defaultScreenElements()) : Serializable