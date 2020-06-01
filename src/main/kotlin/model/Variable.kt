package model

enum class Variable(val value: String, val description: String) {
    NAME("%screenName%", "Name of the screen, e.g. ScreenName"),
    NAME_SNAKE_CASE("%screenNameSnakeCase%", "Name of the screen written in snake case, e.g. screen_name"),
    NAME_LOWER_CASE("%screenNameLowerCase%", "Name of the screen written in camel case starting with lower case, e.g. screenName"),
    FEATURE_MODULE_NAME("%featureModuleName%", "Feature module name e.g feature_login_screen. \"feature_\" added automatically"),
    MODULE_PACKAGE("%packageName%", "Package name e.g com.touchin.test"),
}