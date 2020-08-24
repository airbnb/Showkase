package com.airbnb.android.showkase.models

internal data class ShowkaseProviderElements(
    val components: Map<String, List<ShowkaseBrowserComponent>> = mapOf(),
    val colors: Map<String, List<ShowkaseBrowserColor>> = mapOf(),
    val typographyMap: Map<String, List<ShowkaseBrowserTypography>> = mapOf(),
)
