package com.airbnb.showkase.models

interface ShowkaseComponentsProvider {
    fun getShowkaseComponents(): List<ShowkaseBrowserComponent>
}
