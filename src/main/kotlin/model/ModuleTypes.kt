package model

enum class ModuleTypes(val displayName: String) {
    FEATURE("Feature module");

    override fun toString() = displayName
}