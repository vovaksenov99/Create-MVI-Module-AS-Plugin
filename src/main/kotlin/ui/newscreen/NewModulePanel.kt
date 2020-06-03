package ui.newscreen

import com.intellij.openapi.ui.ComboBox
import model.ModuleType
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField


class NewModulePanel : JPanel() {

    val featureNameTextField = JTextField()
    val packageTextField = JTextField()

    val moduleTypeComboBox = ComboBox(ModuleType.values())

    init {
        layout = GridLayout(0, 2)
        add(JLabel("Package:"))
        add(packageTextField)
        add(JLabel("Feature name:"))
        add(featureNameTextField)
        add(JLabel("Module type:"))
        add(moduleTypeComboBox)
    }

    override fun getPreferredSize() = Dimension(400, 110)
}
