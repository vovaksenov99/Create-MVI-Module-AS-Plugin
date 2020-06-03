package ui.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.LanguageTextField
import data.repository.SettingsRepositoryImpl
import model.FileType
import model.FileTypeDescription
import model.ModuleType
import model.ScreenElement
import ui.help.HelpDialog
import util.addTextChangeListener
import java.awt.event.ActionListener
import javax.swing.JComponent
import javax.swing.event.DocumentListener

class SettingsViewImpl(private val project: Project) : Configurable, SettingsView {

    private var panel: SettingsPanel? = null
    private val presenter = SettingsPresenter(this, SettingsRepositoryImpl(project))
    private var filePathDocumentListener: DocumentListener? = null
    private var templateDocumentListener: com.intellij.openapi.editor.event.DocumentListener? = null
    private var fileNameDocumentListener: DocumentListener? = null

    private val fileTypeActionListener: ActionListener = ActionListener {
        onPanel {
            presenter.onFileTypeSelect(FileType.values()[fileTypeComboBox.selectedIndex])
        }
    }

    private val moduleTypeActionListener: ActionListener = ActionListener {
        onPanel {
            presenter.onModuleTypeSelect(ModuleType.values()[moduleTypeComboBox.selectedIndex])
        }
    }

    private var currentTemplateTextField: LanguageTextField? = null
    private var currentSampleTextField: LanguageTextField? = null

    override fun isModified() = presenter.isModified

    override fun getDisplayName() = "Module Generator Plugin"

    override fun apply() = presenter.onApplySettings()

    override fun reset() = presenter.onResetSettings()

    override fun createComponent(): JComponent? {
        panel = SettingsPanel(project)
        presenter.onLoadView()
        onPanel {
            create(presenter::onHelpClick)
            currentTemplateTextField = codePanel.kotlinTemplateTextField
            currentSampleTextField = codePanel.kotlinSampleTextField
        }
        presenter.onViewCreated()
        return panel
    }

    override fun setUpListeners() = onPanel {
        toolbarDecorator.apply {
            setAddAction { presenter.onAddClick() }
            setRemoveAction { presenter.onDeleteClick(screenElementsList.selectedIndex) }
            setMoveDownAction { presenter.onMoveDownClick(screenElementsList.selectedIndex) }
            setMoveUpAction { presenter.onMoveUpClick(screenElementsList.selectedIndex) }
        }
        moduleTypeComboBox.addActionListener(moduleTypeActionListener)
        screenElementsList.addListSelectionListener {
            if (!it.valueIsAdjusting) presenter.onScreenElementSelect(screenElementsList.selectedIndex)
        }
    }

    override fun addScreenElement(screenElement: ScreenElement) = onPanel {
        screenElementsListModel.add(screenElement)
    }

    override fun selectScreenElement(index: Int) = onPanel {
        screenElementsList.selectedIndex = index
    }

    override fun showPath(path: String) = onPanel {
        pathTextField.text = path
    }

    override fun addTextChangeListeners() = onPanel {
        templateDocumentListener = currentTemplateTextField?.addTextChangeListener(presenter::onTemplateChange)
        fileNameDocumentListener = fileNameTextField.addTextChangeListener(presenter::onFileNameChange)
        filePathDocumentListener = pathTextField.addTextChangeListener(presenter::onPathChange)
        fileTypeComboBox.addActionListener(fileTypeActionListener)
    }

    override fun removeTextChangeListeners() = onPanel {
        filePathDocumentListener?.let { pathTextField.document.removeDocumentListener(it) }
        templateDocumentListener?.let { currentTemplateTextField?.document?.removeDocumentListener(it) }
        fileNameDocumentListener?.let { fileNameTextField.document.removeDocumentListener(it) }
        filePathDocumentListener = null
        templateDocumentListener = null
        fileNameDocumentListener = null
        fileTypeComboBox.removeActionListener(fileTypeActionListener)
    }

    override fun updateScreenElement(index: Int, screenElement: ScreenElement) = onPanel {
        screenElementsListModel.setElementAt(screenElement, index)
    }

    override fun removeScreenElement(index: Int) = onPanel { screenElementsListModel.remove(index) }

    override fun showScreenElements(screenElements: List<ScreenElement>) = onPanel {
        screenElements.forEach { screenElementsListModel.add(it) }
    }

    override fun clearScreenElements() = onPanel { screenElementsListModel.removeAll() }

    override fun showSampleCode(text: String) {
        currentSampleTextField?.text = text
    }

    override fun showTemplate(template: String) {
        currentTemplateTextField?.text = template
    }

    override fun showFileTypeDescription(fileTypeDescription: FileTypeDescription) = onPanel {
        fileTypeComboBox.selectedIndex = fileTypeDescription.description.ordinal
    }

    override fun showCodeTextFields(fileTypeDescription: FileTypeDescription) = onPanel { codePanel.show(fileTypeDescription) }

    override fun swapToKotlinTemplateListener(addListener: Boolean) = onPanel {
        templateDocumentListener?.let { currentTemplateTextField?.document?.removeDocumentListener(it) }
        currentTemplateTextField = codePanel.kotlinTemplateTextField
        currentSampleTextField = codePanel.kotlinSampleTextField
        if (addListener) templateDocumentListener = currentTemplateTextField?.addTextChangeListener(presenter::onTemplateChange)
    }

    override fun swapToGradleTemplateListener(addListener: Boolean) = onPanel {
        templateDocumentListener?.let { currentTemplateTextField?.document?.removeDocumentListener(it) }
        currentTemplateTextField = codePanel.gradleTemplateTextField
        currentSampleTextField = codePanel.gradleSampleTextField
        if (addListener) templateDocumentListener = currentTemplateTextField?.addTextChangeListener(presenter::onTemplateChange)
    }

    override fun swapToAndroidManifestTemplateListener(addListener: Boolean) = onPanel {
        templateDocumentListener?.let { currentTemplateTextField?.document?.removeDocumentListener(it) }
        currentTemplateTextField = codePanel.androidManifestTemplateTextField
        currentSampleTextField = codePanel.androidManifestSampleTextField
        if (addListener) templateDocumentListener = currentTemplateTextField?.addTextChangeListener(presenter::onTemplateChange)
    }

    override fun swapToXmlTemplateListener(addListener: Boolean) = onPanel {
        templateDocumentListener?.let { currentTemplateTextField?.document?.removeDocumentListener(it) }
        currentTemplateTextField = codePanel.xmlTemplateTextField
        currentSampleTextField = codePanel.xmlSampleTextField
        if (addListener) templateDocumentListener = currentTemplateTextField?.addTextChangeListener(presenter::onTemplateChange)
    }

    override fun showFileNameTemplate(text: String) = onPanel {
        fileNameTextField.text = text
    }

    override fun showFileNameSample(text: String) = onPanel {
        fileNameSampleLabel.text = text
    }

    override fun showHelp() = HelpDialog().show()

    override fun setScreenElementDetailsEnabled(isEnabled: Boolean) = onPanel { setScreenElementDetailsEnabled(isEnabled) }

    private inline fun onPanel(function: SettingsPanel.() -> Unit) = panel?.run { function() } ?: Unit
}