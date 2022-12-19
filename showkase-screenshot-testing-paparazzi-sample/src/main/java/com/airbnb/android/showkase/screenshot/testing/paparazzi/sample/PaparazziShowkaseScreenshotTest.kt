package com.airbnb.android.showkase.screenshot.testing.paparazzi.sample

import app.cash.paparazzi.Paparazzi
import org.junit.Rule

interface PaparazziShowkaseScreenshotTest {
    @get:Rule
    val paparazzi: Paparazzi
}