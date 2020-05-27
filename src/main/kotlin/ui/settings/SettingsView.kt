package ui.settings

import model.FileTypeDescription
import model.ScreenElement

interface SettingsView {
    fun setUpListeners()
    fun addScreenElement(screenElement: ScreenElement)
    fun selectScreenElement(index: Int)
    fun showName(name: String)
    fun addTextChangeListeners()
    fun removeTextChangeListeners()
    fun updateScreenElement(index: Int, screenElement: ScreenElement)
    fun removeScreenElement(index: Int)
    fun showScreenElements(screenElements: List<ScreenElement>)
    fun clearScreenElements()
    fun showSampleCode(text: String)
    fun showTemplate(template: String)
    fun showActivityBaseClass(text: String)
    fun showFragmentBaseClass(text: String)
    fun addBaseClassTextChangeListeners()
    fun removeBaseClassTextChangeListeners()
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
    fun setFileNameUnchangeable(text: String = "")
}