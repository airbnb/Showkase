package com.airbnb.android.showkase.processor.utils

import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.lang.model.element.Element

internal fun Element.kotlinMetadata(): KotlinClassMetadata? {
    // https://github.com/JetBrains/kotlin/tree/master/libraries/kotlinx-metadata/jvm
    val kotlinMetadataAnnotation = getAnnotation(Metadata::class.java) ?: return null
    return KotlinClassMetadata.read(kotlinMetadataAnnotation)
}
