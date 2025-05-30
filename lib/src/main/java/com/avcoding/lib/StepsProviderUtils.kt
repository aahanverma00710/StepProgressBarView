package com.avcoding.lib

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable


data class ProgressViewConfig(
    val totalSteps: Int,
    val labelSuffix: String = "m",
    val labelTextSizeSp: Float = 12f,
    val labelTextColor: Int = Color.BLACK,
    val labelTypeface: Typeface? = null,
    val barHeightDp: Int = 8,
    val setRoundedCorners: Boolean = true,
    val roundRadius: Int = 4,
    val thumbDrawable: Drawable? = null,
    val barColor: Pair<Int, Int>
)

fun StepProgressBarView.setUp(config: ProgressViewConfig, onStepChangeListener: (Float) -> Unit) {
    if (config.totalSteps <= 0) {
        throw IllegalStateException("Steps can't be below or equal to 0")
    }

    if (config.setRoundedCorners && config.roundRadius < 0) {
        throw IllegalStateException("Corner Radius can' can't be below  0")
    }

    if (config.barHeightDp <= 0) {
        throw IllegalStateException("Bar height can't be zero")
    }
    if (config.thumbDrawable == null) {
        throw IllegalStateException("Bar thumb can't be null")
    }

    totalSteps = config.totalSteps
    labelSuffix = config.labelSuffix
    labelTextSizeSp = config.labelTextSizeSp

    setBarHeight(config.barHeightDp)
    setThumbDrawable(config.thumbDrawable)

    setBarColors(filledColor = config.barColor.first, unfilledColor = config.barColor.second)
    if (config.setRoundedCorners) {
        setRoundedCorners(enabled = true, radiusDp = config.roundRadius)
    }

    labelTextColor = config.labelTextColor

    if (config.labelTypeface != null) {
        labelTypeface = config.labelTypeface
    }
    setOnStepChangeListener {
        onStepChangeListener.invoke(it)
    }

}