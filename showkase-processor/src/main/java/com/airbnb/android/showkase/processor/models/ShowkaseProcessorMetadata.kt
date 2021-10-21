package com.airbnb.android.showkase.processor.models

internal data class ShowkaseProcessorMetadata(
    val components: Set<ShowkaseMetadata> = setOf(),
    val colors: Set<ShowkaseMetadata> = setOf(),
    val typography: Set<ShowkaseMetadata> = setOf(),
)