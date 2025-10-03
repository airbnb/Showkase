package com.airbnb.android.showkase.annotation

/**
 * Configuration for how screenshots of the annotated Composable should be captured.
 */
sealed interface ScreenshotConfig {
    /**
     * A single screenshot will be captured of the initial composition.
     */
    object SingleStaticImage : ScreenshotConfig

    /**
     * An animated PNG will be captured of the Composable, using the values provided for
     * [ShowkaseComposable.captureDurationMillis] and [ShowkaseComposable.captureFramerate].
     */
    data class SingleAnimatedImage(
        val durationMillis: Int = 1000,
        val framerate: Int = 30,
    ) : ScreenshotConfig

    /**
     * Multiple static screenshots will be taken of the Composable, with the animation advanced to the
     * time offsets provided in [ShowkaseComposable.captureOffsetsMillis].
     */
    data class MultipleImagesAtOffsets(
        val offsetMillis: List<Int>,
    ) : ScreenshotConfig
}
