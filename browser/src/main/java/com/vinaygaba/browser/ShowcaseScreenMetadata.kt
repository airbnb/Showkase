package com.vinaygaba.browser

import androidx.compose.Model

enum class ShowcaseCurrentScreen {
    GROUPS,
    GROUP_COMPONENTS
}

@Model
data class ShowcaseScreenMetadata(
    var currentScreen: ShowcaseCurrentScreen = ShowcaseCurrentScreen.GROUPS,
    var currentGroup: String? = null
)
