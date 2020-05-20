package model

private val KOTLIN_DEFAULT_TEMPLATE = "package ${Variable.PACKAGE_NAME.value}\n\nclass ${Variable.NAME.value}${Variable.SCREEN_ELEMENT.value}"
private val GRADLE_DEFAULT_TEMPLATE = ""
private val ANDROID_MANIFEST_DEFAULT_TEMPLATE = ""
private const val LAYOUT_XML_DEFAULT_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "<FrameLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
        "    android:layout_width=\"match_parent\"\n" +
        "    android:layout_height=\"match_parent\">\n" +
        "\n" +
        "</FrameLayout>"
private val KOTLIN_DEFAULT_FILE_NAME = "${Variable.NAME.value}${Variable.SCREEN_ELEMENT.value}"
private val LAYOUT_XML_DEFAULT_FILE_NAME = "${Variable.ANDROID_COMPONENT_NAME_LOWER_CASE.value}_${Variable.NAME_SNAKE_CASE.value}"
private const val GRADLE_DEFAULT_FILE_NAME = "build"
private const val ANDROID_MANIFEST_DEFAULT_FILE_NAME = "AndroidManifest"


enum class FileType(val displayName: String, val extension: String, val defaultTemplate: String, val defaultFileName: String) {
    KOTLIN("Kotlin", "kt", KOTLIN_DEFAULT_TEMPLATE, KOTLIN_DEFAULT_FILE_NAME),
    LAYOUT_XML("Layout XML", "xml", LAYOUT_XML_DEFAULT_TEMPLATE, LAYOUT_XML_DEFAULT_FILE_NAME),
    GRADLE("Gradle file", "gradle", GRADLE_DEFAULT_TEMPLATE, GRADLE_DEFAULT_FILE_NAME),
    ANDROID_MANIFEST("Android manifest file", "xml", ANDROID_MANIFEST_DEFAULT_TEMPLATE, ANDROID_MANIFEST_DEFAULT_FILE_NAME);

    override fun toString() = displayName
}