package com.airbnb.android.showkase.screenshot.testing

import android.graphics.Color
import androidx.annotation.FloatRange
import kotlin.math.pow

/**
 * Copied from the AndroidX Screenshot Testing open source code that's also used by Google internally.
 * Modified as per
 * Available here -
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:test/screenshot/screenshot/src/main/java/androidx/test/screenshot/matchers/MSSIMMatcher.kt
 *
 * Image comparison using Structural Similarity Index, developed by Wang, Bovik, Sheikh, and
 * Simoncelli. Details can be read in their paper:
 * https://ece.uwaterloo.ca/~z70wang/publications/ssim.pdf
 */
class BitmapMatcher(
    @FloatRange(from = 0.0, to = 1.0) private val threshold: Double = 0.995
) {

    companion object {
        // These values were taken from the publication
        private const val CONSTANT_L = 254.0
        private const val CONSTANT_K1 = 0.00001
        private const val CONSTANT_K2 = 0.00003
        private val CONSTANT_C1 = (CONSTANT_L * CONSTANT_K1).pow(2.0)
        private val CONSTANT_C2 = (CONSTANT_L * CONSTANT_K2).pow(2.0)
        private const val WINDOW_SIZE = 10
    }

    fun compareBitmaps(
        expected: IntArray,
        given: IntArray,
        width: Int,
        height: Int
    ): SimilarityMatchResult {
        val similarityScore = calculateSimilarity(expected, given, width, height)

        val stats = "Minimum Similarity threshold: $threshold, Actual similarity score: " + "%.3f".format(similarityScore)

        return SimilarityMatchResult(
            matches = similarityScore >= threshold,
            similarityScore = similarityScore,
            threshold = threshold
        )
    }

    private fun calculateSimilarity(
        ideal: IntArray,
        given: IntArray,
        width: Int,
        height: Int
    ) = calculateSimilarity(ideal, given, 0, width, width, height)

    private fun calculateSimilarity(
        ideal: IntArray,
        given: IntArray,
        offset: Int,
        stride: Int,
        width: Int,
        height: Int
    ): Double {
        var similarityTotal = 0.0
        var windows = 0
        var currentWindowY = 0
        while (currentWindowY < height) {
            val windowHeight = computeWindowSize(currentWindowY, height)
            var currentWindowX = 0
            while (currentWindowX < width) {
                val windowWidth = computeWindowSize(currentWindowX, width)
                val start: Int =
                    indexFromXAndY(currentWindowX, currentWindowY, stride, offset)
                if (isWindowWhite(ideal, start, stride, windowWidth, windowHeight) &&
                    isWindowWhite(given, start, stride, windowWidth, windowHeight)
                ) {
                    currentWindowX += WINDOW_SIZE
                    continue
                }
                windows++
                val means =
                    getMeans(ideal, given, start, stride, windowWidth, windowHeight)
                val meanX = means[0]
                val meanY = means[1]
                val variances = getVariances(
                    ideal, given, meanX, meanY, start, stride,
                    windowWidth, windowHeight
                )
                val varX = variances[0]
                val varY = variances[1]
                val stdBoth = variances[2]
                val similarity = calculateSimilarity(meanX, meanY, varX, varY, stdBoth)
                similarityTotal += similarity
                currentWindowX += WINDOW_SIZE
            }
            currentWindowY += WINDOW_SIZE
        }
        if (windows == 0) {
            return 1.0
        }
        return similarityTotal / windows.toDouble()
    }

    /**
     * Compute the size of the window. The window defaults to WINDOW_SIZE, but
     * must be contained within dimension.
     */
    private fun computeWindowSize(coordinateStart: Int, dimension: Int): Int {
        return if (coordinateStart + WINDOW_SIZE <= dimension) {
            WINDOW_SIZE
        } else {
            dimension - coordinateStart
        }
    }

    private fun isWindowWhite(
        colors: IntArray,
        start: Int,
        stride: Int,
        windowWidth: Int,
        windowHeight: Int
    ): Boolean {
        for (y in 0 until windowHeight) {
            for (x in 0 until windowWidth) {
                if (colors[indexFromXAndY(x, y, stride, start)] != Color.WHITE) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * This calculates the position in an array that would represent a bitmap given the parameters.
     */
    private fun indexFromXAndY(x: Int, y: Int, stride: Int, offset: Int): Int {
        return x + y * stride + offset
    }

    private fun calculateSimilarity(muX: Double, muY: Double, sigX: Double, sigY: Double, sigXY: Double): Double {
        var similarity = (2 * muX * muY + CONSTANT_C1) * (2 * sigXY + CONSTANT_C2)
        val denom = ((muX * muX + muY * muY + CONSTANT_C1) * (sigX + sigY + CONSTANT_C2))
        similarity /= denom
        return similarity
    }

    /**
     * This method will find the mean of a window in both sets of pixels. The return is an array
     * where the first double is the mean of the first set and the second double is the mean of the
     * second set.
     */
    private fun getMeans(
        pixels0: IntArray,
        pixels1: IntArray,
        start: Int,
        stride: Int,
        windowWidth: Int,
        windowHeight: Int
    ): DoubleArray {
        var avg0 = 0.0
        var avg1 = 0.0
        for (y in 0 until windowHeight) {
            for (x in 0 until windowWidth) {
                val index: Int = indexFromXAndY(x, y, stride, start)
                avg0 += getIntensity(pixels0[index])
                avg1 += getIntensity(pixels1[index])
            }
        }
        avg0 /= windowWidth * windowHeight.toDouble()
        avg1 /= windowWidth * windowHeight.toDouble()
        return doubleArrayOf(avg0, avg1)
    }

    /**
     * Finds the variance of the two sets of pixels, as well as the covariance of the windows. The
     * return value is an array of doubles, the first is the variance of the first set of pixels,
     * the second is the variance of the second set of pixels, and the third is the covariance.
     */
    private fun getVariances(
        pixels0: IntArray,
        pixels1: IntArray,
        mean0: Double,
        mean1: Double,
        start: Int,
        stride: Int,
        windowWidth: Int,
        windowHeight: Int
    ): DoubleArray {
        var var0 = 0.0
        var var1 = 0.0
        var varBoth = 0.0
        for (y in 0 until windowHeight) {
            for (x in 0 until windowWidth) {
                val index: Int = indexFromXAndY(x, y, stride, start)
                val v0 = getIntensity(pixels0[index]) - mean0
                val v1 = getIntensity(pixels1[index]) - mean1
                var0 += v0 * v0
                var1 += v1 * v1
                varBoth += v0 * v1
            }
        }
        var0 /= windowWidth * windowHeight - 1.toDouble()
        var1 /= windowWidth * windowHeight - 1.toDouble()
        varBoth /= windowWidth * windowHeight - 1.toDouble()
        return doubleArrayOf(var0, var1, varBoth)
    }

    /**
     * Gets the intensity of a given pixel in RGB using luminosity formula
     *
     * l = 0.21R' + 0.72G' + 0.07B'
     *
     * The prime symbols dictate a gamma correction of 1.
     */
    private fun getIntensity(pixel: Int): Double {
        val gamma = 1.0
        var l = 0.0
        l += 0.21f * (Color.red(pixel) / 255f.toDouble()).pow(gamma)
        l += 0.72f * (Color.green(pixel) / 255f.toDouble()).pow(gamma)
        l += 0.07f * (Color.blue(pixel) / 255f.toDouble()).pow(gamma)
        return l
    }
}

data class SimilarityMatchResult(
    val matches: Boolean,
    val similarityScore: Double,
    val threshold: Double
)