package com.vinaygaba.showcase.models

import androidx.compose.Model

internal enum class ShowcaseCurrentScreen {
    GROUPS,
    GROUP_COMPONENTS,
    COMPONENT_DETAIL,
}

@Model
internal object ShowcaseBrowserScreenMetadata {
    var currentScreen: ShowcaseCurrentScreen =
        ShowcaseCurrentScreen.GROUPS
    var currentGroup: String? = null
    var currentComponent: String? = null
}
