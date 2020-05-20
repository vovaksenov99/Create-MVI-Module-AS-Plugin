package ui.newscreen

import com.intellij.openapi.ui.ComboBox
import model.AndroidComponent
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField


class NewScreenPanel : JPanel() {

    val nameTextField = JTextField()
    val packageTextField = JTextField()
    val moduleTextField = JTextField()

    val androidComponentComboBox = ComboBox<AndroidComponent>(AndroidComponent.values())

    init {
        layout = GridLayout(0, 2)
        add(JLabel("Module name:"))
        add(moduleTextField)
        add(JLabel("Package:"))
        add(packageTextField)
        add(JLabel("Name:"))
        add(nameTextField)
        add(JLabel("Android Component:"))
        add(androidComponentComboBox)
    }

    override fun getPreferredSize() = Dimension(350, 110)
}
