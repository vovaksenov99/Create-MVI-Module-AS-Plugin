package ui.settings

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.CollectionListModel
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.JBSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import model.FileType
import model.ModuleType
import model.ScreenElement
import java.awt.*
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.ListSelectionModel

class SettingsPanel(project: Project) : JPanel() {

    val pathTextField = JTextField()

    val screenElementsListModel = CollectionListModel<ScreenElement>()
    val screenElementsList = JBList(screenElementsListModel).apply {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
    }
    val toolbarDecorator: ToolbarDecorator = ToolbarDecorator.createDecorator(screenElementsList)

    val fileTypeComboBox = ComboBox(FileType.values())
    val moduleTypeComboBox = ComboBox(ModuleType.values())
    val fileNameTextField = JTextField()
    val fileNameSampleLabel = JLabel().apply { isEnabled = false }

    val codePanel = CodePanel(project)

    private lateinit var screenElementDetailsPanel: JPanel
    private val pathLabel = JLabel("Path:")
    private val fileNameLabel = JLabel("File Name:")
    private val fileTypeLabel = JLabel("File Type:")

    init {
        layout = BorderLayout()
    }

    fun create(onHelpClick: () -> Unit) {
        val screenElementsPanel = createScreenElementsPanel()
        screenElementDetailsPanel = createScreenElementDetailsPanel()

        val rightPanel = createSplitterRightPanel(screenElementDetailsPanel)

        addSplitter(screenElementsPanel, rightPanel)
        addCodePanel(onHelpClick)
    }

    private fun createScreenElementsPanel() = JPanel().apply {
        border = IdeBorderFactory.createTitledBorder("Elements", false)
        layout = GridBagLayout().apply {
        }
        add(moduleTypeComboBox, GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
            weighty = 0.0
            gridy = 0
        })
        add(toolbarDecorator.createPanel().apply {
        }, GridBagConstraints().apply {
            fill = GridBagConstraints.BOTH
            gridy = 1
            weightx = 1.0
            weighty = 1.0
        })
    }

    private fun createScreenElementDetailsPanel() = JPanel().apply {
        border = IdeBorderFactory.createTitledBorder("Element Details", false)
        layout = GridBagLayout()
        add(pathLabel, constraintsLeft(0, 0))
        add(pathTextField, constraintsRight(1, 0))
        add(fileNameLabel, constraintsLeft(0, 1))
        add(fileNameTextField, constraintsRight(1, 1))
        add(fileTypeLabel, constraintsLeft(0, 2))
        add(fileTypeComboBox, constraintsRight(1, 2).apply {
            gridwidth = 1
            fill = GridBagConstraints.NONE
            anchor = GridBagConstraints.LINE_START
        })
        add(fileNameSampleLabel, constraintsRight(2, 2).apply {
            gridwidth = 1
            weightx /= 2
            anchor = GridBagConstraints.LINE_END
            fill = GridBagConstraints.NONE
        })
    }

    private fun constraintsLeft(x: Int, y: Int) = GridBagConstraints().apply {
        weightx = 0.15
        gridx = x
        gridy = y
        insets = Insets(0, 8, 0, 0)
        fill = GridBagConstraints.HORIZONTAL
    }

    private fun constraintsRight(x: Int, y: Int) = GridBagConstraints().apply {
        weightx = 0.85
        gridx = x
        gridy = y
        gridwidth = 2
        fill = GridBagConstraints.HORIZONTAL
    }

    private fun addSplitter(leftPanel: JPanel, rightPanel: JPanel) {
        add(JBSplitter(0.3f).apply {
            firstComponent = leftPanel
            secondComponent = rightPanel
        }, BorderLayout.PAGE_START)
    }

    private fun createSplitterRightPanel(screenElementDetailsPanel: JPanel) =
            JPanel(GridLayout(0, 1)).apply {
                add(screenElementDetailsPanel)
            }

    private fun addCodePanel(onHelpClick: () -> Unit) {
        codePanel.create(onHelpClick)
        add(codePanel, BorderLayout.CENTER)
    }

    fun setScreenElementDetailsEnabled(isEnabled: Boolean) {
        screenElementDetailsPanel.isEnabled = isEnabled
        pathTextField.isEnabled = isEnabled
        fileNameTextField.isEnabled = isEnabled
        fileTypeComboBox.isEnabled = isEnabled
        pathLabel.isEnabled = isEnabled
        fileNameLabel.isEnabled = isEnabled
        fileTypeLabel.isEnabled = isEnabled
        codePanel.setCodePanelsEnabled(isEnabled)
    }
}
