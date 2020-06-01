package ui.settings

import data.repository.SettingsRepository
import model.*
import util.replaceVariablesDefault
import util.swap

class SettingsPresenter(private val view: SettingsView,
                        private val settingsRepository: SettingsRepository) {

    val screenElements = mutableListOf<ScreenElement>()
    var currentSelectedScreenElement: ScreenElement? = null
    var isModified = false
    lateinit var initialSettings: Settings

    fun onLoadView() {
        initialSettings = settingsRepository.loadSettings()
        resetToInitialSettings()
        view.setUpListeners()
        view.showScreenElements(screenElements)
        view.addBaseClassTextChangeListeners()
    }

    fun onViewCreated() = view.setScreenElementDetailsEnabled(false)

    private fun resetToInitialSettings() {
        screenElements.clear()
        initialSettings.screenElements.mapTo(screenElements) { it.copy() }
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
            view.showPath(selectedElement.path)
            view.showFileTypeDescription(selectedElement.toFileTypeDescription())
            handleFileTypeSelection(selectedElement, false)
            view.showTemplate(selectedElement.template)
            updateSampleCode(selectedElement)
            view.addTextChangeListeners()
            view.setScreenElementDetailsEnabled(true)
        } else {
            currentSelectedScreenElement = null
            view.removeTextChangeListeners()
            view.showPath("")
            view.showTemplate("")
            view.showSampleCode("")
            view.showFileNameTemplate("")
            view.showFileNameSample("")
            view.setScreenElementDetailsEnabled(false)
        }
    }

    fun onPathChange(path: String) {
        currentSelectedScreenElement?.let {
            it.path = path
            isModified = true
        }
    }

    private fun updateSampleFileName(screenElement: ScreenElement) {
        val fileName = screenElement.name.replaceVariablesDefault()
        val fileExtension = screenElement.fileType.extension
        view.showFileNameSample("$fileName.$fileExtension")
    }

    fun onApplySettings() {
        initialSettings = Settings(screenElements.toMutableList())
        resetToInitialSettings()
        settingsRepository.update(initialSettings)
        isModified = false
    }

    fun onResetSettings() {
        resetToInitialSettings()
        view.clearScreenElements()
        view.showScreenElements(screenElements)
        view.removeBaseClassTextChangeListeners()
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
            view.showSampleCode(screenElement.template.replaceVariablesDefault())

    fun onActivityBaseClassChange(text: String) {
        currentSelectedScreenElement?.let {
            updateSampleCode(it)
            updateSampleFileName(it)
        }
        isModified = true
    }

    fun onFragmentBaseClassChange(text: String) {
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
    }

    fun onFileNameChange(fileName: String) {
        currentSelectedScreenElement?.let { screenElement ->
            screenElement.fileNameTemplate = fileName
            screenElement.name = fileName
            view.updateScreenElement(screenElements.indexOf(screenElement), screenElement)
            updateSampleCode(screenElement)
            updateSampleFileName(screenElement)
            isModified = true
        }
    }

    fun onHelpClick() = view.showHelp()
}