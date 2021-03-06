package ui.settings

import model.FileTypeDescription
import model.ScreenElement

interface SettingsView {
    fun setUpListeners()
    fun addScreenElement(screenElement: ScreenElement)
    fun selectScreenElement(index: Int)
    fun showPath(name: String)
    fun addTextChangeListeners()
    fun removeTextChangeListeners()
    fun updateScreenElement(index: Int, screenElement: ScreenElement)
    fun removeScreenElement(index: Int)
    fun showScreenElements(screenElements: List<ScreenElement>)
    fun clearScreenElements()
    fun showSampleCode(text: String)
    fun showTemplate(template: String)
    fun showFileTypeDescription(fileTypeDescription: FileTypeDescription)
    fun showCodeTextFields(FileTypeDescription: FileTypeDescription)
    fun swapToKotlinTemplateListener(addListener: Boolean)
    fun swapToXmlTemplateListener(addListener: Boolean)
    fun showFileNameTemplate(text: String)
    fun showFileNameSample(text: String)
    fun showHelp()
    fun swapToGradleTemplateListener(addListener: Boolean)
    fun swapToAndroidManifestTemplateListener(addListener: Boolean)
    fun setScreenElementDetailsEnabled(isEnabled: Boolean)
}