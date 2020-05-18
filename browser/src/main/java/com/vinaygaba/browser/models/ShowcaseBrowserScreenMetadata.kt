package com.vinaygaba.browser.models

import androidx.compose.Model

enum class ShowcaseCurrentScreen {
    GROUPS,
    GROUP_COMPONENTS,
    COMPONENT_DETAIL,
}

@Model
object ShowcaseBrowserScreenMetadata {
    var currentScreen: ShowcaseCurrentScreen =
        ShowcaseCurrentScreen.GROUPS
    var currentGroup: String? = null
    var currentComponent: String? = null
}
