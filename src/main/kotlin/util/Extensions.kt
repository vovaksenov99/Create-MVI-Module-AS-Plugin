package util

import com.intellij.ui.LanguageTextField
import model.Variable
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

const val DEFAULT_PACKAGE = "sample.touchin.com"
const val DEFAULT_FEATURE_MODULE_NAME = "feature_sample"
const val DEFAULT_FEATURE_NAME = "sample"

fun JTextField.addTextChangeListener(onChange: (String) -> Unit) =
        object : DocumentListener {
            override fun changedUpdate(e: DocumentEvent?) = onChange(text)
            override fun insertUpdate(e: DocumentEvent?) = onChange(text)
            override fun removeUpdate(e: DocumentEvent?) = onChange(text)
        }.apply { document.addDocumentListener(this) }

fun LanguageTextField.addTextChangeListener(onChange: (String) -> Unit) =
        object : com.intellij.openapi.editor.event.DocumentListener {
            override fun documentChanged(event: com.intellij.openapi.editor.event.DocumentEvent) = onChange(text)
        }.apply { document.addDocumentListener(this) }

fun String.toSnakeCase() = replace(Regex("([^_A-Z])([A-Z])"), "$1_$2").toLowerCase()

fun String.toClassName() = this.split("_").joinToString(separator = "") { it.capitalize() }

fun String.toMethodName() = this.toClassName().decapitalize()

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

fun String.replaceVariables(featureName: String, featureModuleName: String, packageName: String) =
        replace(Variable.NAME.value, featureName.toClassName())
                .replace(Variable.NAME_SNAKE_CASE.value, featureName.toSnakeCase())
                .replace(Variable.NAME_LOWER_CASE.value, featureName.toMethodName())
                .replace(Variable.FEATURE_MODULE_NAME.value, featureModuleName)
                .replace(Variable.MODULE_PACKAGE.value, packageName)


fun String.replaceVariablesDefault() =
        replaceVariables(DEFAULT_FEATURE_NAME, DEFAULT_FEATURE_MODULE_NAME, DEFAULT_PACKAGE)
