package com.airbnb.android.showkase.models

data class ShowkaseElementsMetadata(
    val components: List<ShowkaseBrowserComponent> = listOf(),
    val colors: List<ShowkaseBrowserColor> = listOf(),
    val typographyMap: List<ShowkaseBrowserTypography> = listOf(),
)
