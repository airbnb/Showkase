package com.airbnb.android.showkase.models

sealed interface ShowkaseBrowserElement {
    val group: String
    val name: String
    val kDoc: String
}
