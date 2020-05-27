package model

import java.io.Serializable

private const val DEFAULT_BASE_ACTIVITY_CLASS = "androidx.appcompat.app.AppCompatActivity"
private const val DEFAULT_BASE_FRAGMENT_CLASS = "androidx.fragment.app.Fragment"

private fun defaultScreenElements() =
        getTemplates()
                .map {
                    it.toScreenElement()
                }
                .toMutableList()

fun FileTypeDescription.toScreenElement() = ScreenElement(displayName, description, defaultTemplate, defaultFileName, defaultPath)

fun ScreenElement.toFileTypeDescription() = FileTypeDescription(name, fileType, template, fileNameTemplate, defaultPath)

data class Settings(var screenElements: MutableList<ScreenElement> = defaultScreenElements(),
                    var activityBaseClass: String = DEFAULT_BASE_ACTIVITY_CLASS,
                    var fragmentBaseClass: String = DEFAULT_BASE_FRAGMENT_CLASS) : Serializable