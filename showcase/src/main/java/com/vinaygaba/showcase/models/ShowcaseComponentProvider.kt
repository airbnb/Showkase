package com.vinaygaba.showcase.models

interface ShowcaseComponentProvider {
    fun getShowcaseComponents(): List<ShowcaseCodegenMetadata>
}
