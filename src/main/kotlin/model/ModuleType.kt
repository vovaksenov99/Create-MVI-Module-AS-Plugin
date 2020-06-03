package model

enum class ModuleType(val displayName: String) {
    FEATURE("Feature module"),
    TEST("Test module");

    override fun toString() = displayName
}