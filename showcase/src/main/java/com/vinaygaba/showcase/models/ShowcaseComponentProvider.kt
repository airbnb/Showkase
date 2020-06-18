package com.vinaygaba.showcase.models

import com.vinaygaba.showcase.annotation.models.ShowcaseCodegenMetadata

interface ShowcaseComponentProvider {
    fun getShowcaseComponents(): List<ShowcaseCodegenMetadata>
}
