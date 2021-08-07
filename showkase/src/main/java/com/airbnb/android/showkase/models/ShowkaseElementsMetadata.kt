package com.airbnb.android.showkase.models

data class ShowkaseElementsMetadata(
    val componentList: List<ShowkaseBrowserComponent> = listOf(),
    val colorList: List<ShowkaseBrowserColor> = listOf(),
    val typographyList: List<ShowkaseBrowserTypography> = listOf(),
)
