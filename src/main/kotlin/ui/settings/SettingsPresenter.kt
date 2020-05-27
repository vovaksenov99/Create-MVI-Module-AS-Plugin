package ui.settings

import data.repository.SettingsRepository
import model.*
import util.swap

const val SAMPLE_SCREEN_NAME = "Sample"
const val SAMPLE_PACKAGE_NAME = "com.sample.touch"
const val SAMPLE_ANDROID_COMPONENT = "Activity"

class SettingsPresenter(private val view: SettingsView,
                        private val settingsRepository: SettingsRepository) {

    val screenElements = mutableListOf<ScreenElement>()
    var currentSelectedScreenElement: ScreenElement? = null
    var isModified = false
    lateinit var initialSettings: Settings
    lateinit var currentActivityBaseClass: String
    lateinit var currentFragmentBaseClass: String

    fun onLoadView() {
        initialSettings = settingsRepository.loadSettings()
        resetToInitialSettings()
        view.setUpListeners()
        view.showScreenElements(screenElements)
        view.showActivityBaseClass(currentActivityBaseClass)
        view.showFragmentBaseClass(currentFragmentBaseClass)
        view.addBaseClassTextChangeListeners()
    }

    fun onViewCreated() = view.setScreenElementDetailsEnabled(false)

    private fun resetToInitialSettings() {
        screenElements.clear()
        initialSettings.screenElements.mapTo(screenElements) { it.copy() }
        currentActivityBaseClass = initialSettings.activityBaseClass
        currentFragmentBaseClass = initialSettings.fragmentBaseClass
    }

    fun onAddClick() {
        val newScreenElement = ScreenElement.getDefault()
        screenElements.add(newScreenElement)
        view.addScreenElement(newScreenElement)
        view.selectScreenElement(screenElements.size - 1)
        isModified = true
    }

    fun onDeleteClick(index: Int) {
        screenElements.removeAt(index)
        view.removeScreenElement(index)
        isModified = true
    }

    fun onScreenElementSelect(index: Int) {
        if (index in 0 until screenElements.size) {
            val selectedElement = screenElements[index]
            currentSelectedScreenElement = selectedElement
            view.removeTextChangeListeners()
            view.showName(selectedElement.name)
            view.showFileTypeDescription(selectedElement.toFileTypeDescription())
            handleFileTypeSelection(selectedElement, false)
            view.showTemplate(selectedElement.template)
            updateSampleCode(selectedElement)
            view.addTextChangeListeners()
            view.setScreenElementDetailsEnabled(true)
            setBaseAndroidFilesFieldPrefs(selectedElement)
        } else {
            currentSelectedScreenElement = null
            view.removeTextChangeListeners()
            view.showName("")
            view.showTemplate("")
            view.showSampleCode("")
            view.showFileNameTemplate("")
            view.showFileNameSample("")
            view.setScreenElementDetailsEnabled(false)
        }
    }

    fun onNameChange(name: String) {
        currentSelectedScreenElement?.let {
            it.name = name
            view.updateScreenElement(screenElements.indexOf(it), it)
            updateSampleCode(it)
            updateSampleFileName(it)
            isModified = true
        }
    }

    private fun updateSampleFileName(screenElement: ScreenElement) {
        val fileName = screenElement.name
        val fileExtension = screenElement.fileType.extension
        view.showFileNameSample("$fileName.$fileExtension")
    }

    fun onApplySettings() {
        initialSettings = Settings(screenElements.toMutableList(), currentActivityBaseClass, currentFragmentBaseClass)
        resetToInitialSettings()
        settingsRepository.update(initialSettings)
        isModified = false
    }

    fun onResetSettings() {
        resetToInitialSettings()
        view.clearScreenElements()
        view.showScreenElements(screenElements)
        view.removeBaseClassTextChangeListeners()
        view.showActivityBaseClass(currentActivityBaseClass)
        view.showFragmentBaseClass(currentFragmentBaseClass)
        view.addBaseClassTextChangeListeners()
        isModified = false
    }

    fun onMoveDownClick(index: Int) = onMoveClick(index, index + 1)

    fun onMoveUpClick(index: Int) = onMoveClick(index, index - 1)

    private fun onMoveClick(index: Int, destinationIndex: Int) {
        screenElements.swap(index, destinationIndex)
        view.updateScreenElement(index, screenElements[index])
        view.updateScreenElement(destinationIndex, screenElements[destinationIndex])
        view.selectScreenElement(destinationIndex)
        isModified = true
    }

    fun onTemplateChange(text: String) {
        currentSelectedScreenElement?.let {
            it.template = text
            updateSampleCode(it)
            isModified = true
        }
    }

    private fun updateSampleCode(screenElement: ScreenElement) =
            view.showSampleCode(screenElement.template)

    fun onActivityBaseClassChange(text: String) {
        currentActivityBaseClass = text
        currentSelectedScreenElement?.let {
            updateSampleCode(it)
            updateSampleFileName(it)
        }
        isModified = true
    }

    fun onFragmentBaseClassChange(text: String) {
        currentFragmentBaseClass = text
        currentSelectedScreenElement?.let {
            updateSampleCode(it)
            updateSampleFileName(it)
        }
        isModified = true
    }

    fun onFileTypeSelect(fileType: FileTypeDescription) {
        currentSelectedScreenElement?.let {
            it.fileType = fileType.description
            it.fileNameTemplate = fileType.displayName
            handleFileTypeSelection(it, true)
            view.showTemplate(fileType.defaultTemplate)
            isModified = true
        }
    }

    private fun handleFileTypeSelection(screenElement: ScreenElement, addListener: Boolean) {
        view.showCodeTextFields(screenElement.toFileTypeDescription())
        when (screenElement.fileType) {
            FileType.KOTLIN -> view.swapToKotlinTemplateListener(addListener)
            FileType.GRADLE -> view.swapToGradleTemplateListener(addListener)
            FileType.GITIGNORE -> view.swapToAndroidManifestTemplateListener(addListener)
            FileType.XML -> view.swapToXmlTemplateListener(addListener)
        }
        view.showFileNameTemplate(screenElement.fileNameTemplate)
        updateSampleFileName(screenElement)
        setBaseAndroidFilesFieldPrefs(screenElement)
    }

    fun setBaseAndroidFilesFieldPrefs(screenElement: ScreenElement) {
        when (screenElement.fileType) {
            FileType.GRADLE, FileType.GITIGNORE -> view.setFileNameUnchangeable(screenElement.fileType.name)
        }
    }

    fun onFileNameChange(fileName: String) {
        currentSelectedScreenElement?.let { screenElement ->
            setBaseAndroidFilesFieldPrefs(screenElement)
            screenElement.fileNameTemplate = fileName
            updateSampleFileName(screenElement)
            isModified = true
        }
    }

    fun onHelpClick() = view.showHelp()
}