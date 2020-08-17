package com.airbnb.android.showkase.models

data class ShowkaseProviderElements(
    val components: Map<String, List<ShowkaseBrowserComponent>> = mapOf(),
    val colors: Map<String, List<ShowkaseBrowserColor>> = mapOf(),
)
