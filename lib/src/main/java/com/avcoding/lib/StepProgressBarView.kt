package com.avcoding.lib

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.*
import kotlin.math.roundToInt

class StepProgressBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val fillView = View(context)
    private val trackView = View(context)
    private val thumbView = ImageView(context)
    private val labelLayout = LinearLayout(context)

    var totalSteps: Int = 5
        set(value) {
            field = value
            updateProgress(animated = false)
        }

    var currentStep: Float = 0f
        private set

    private var stepChangeListener: ((Float) -> Unit)? = null

    var labelSuffix: String = "m"
        set(value) {
            field = value
            updateLabels()
        }

    var labelTextSizeSp: Float = 12f
        set(value) {
            field = value
            updateLabels()
        }

    var labelTextColor: Int = Color.BLACK
        set(value) {
            field = value
            updateLabels()
        }

    var labelTypeface: Typeface? = null
        set(value) {
            field = value
            updateLabels()
        }

    init {
        setupLayout()
    }

    private fun setupLayout() {
        removeAllViews()

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }

        val barContainer = FrameLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 32.dp)
        }

        trackView.layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 8.dp).apply {
            gravity = Gravity.CENTER_VERTICAL
        }
        trackView.setBackgroundColor(Color.LTGRAY)
        barContainer.addView(trackView)

        fillView.layoutParams = FrameLayout.LayoutParams(0, 8.dp).apply {
            gravity = Gravity.CENTER_VERTICAL
        }
        fillView.setBackgroundColor(Color.RED)
        barContainer.addView(fillView)

        thumbView.layoutParams = FrameLayout.LayoutParams(24.dp, 24.dp).apply {
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }
        barContainer.addView(thumbView)

        container.addView(barContainer)

        labelLayout.orientation = LinearLayout.HORIZONTAL
        labelLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            topMargin = 4.dp
        }
        container.addView(labelLayout)

        addView(container)

        post {
            updateLabels()
            updateProgress(animated = false)
        }
    }

    private fun updateLabels() {
        labelLayout.removeAllViews()
        val barWidth = width.toFloat()
        if (barWidth == 0f) return
        for (i in 0..totalSteps) {
            val label = TextView(context).apply {
                text = "$i$labelSuffix"
                gravity = Gravity.CENTER
                textSize = labelTextSizeSp
                setTextColor(labelTextColor)
                typeface = labelTypeface ?: Typeface.DEFAULT
                layoutParams = if (i == 0 || i == totalSteps) {
                    LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        weight = 0f
                        gravity = if (i == 0) Gravity.START else Gravity.END
                    }
                } else {
                    LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
                }
            }
            labelLayout.addView(label)
            label.post {
                val stepRatio = i.toFloat() / totalSteps
                val labelWidth = label.width.toFloat()
                val posX = (barWidth * stepRatio) - labelWidth / 2f
                val clampedX = posX.coerceIn(0f, barWidth - labelWidth)
                label.x = clampedX
            }
        }
    }

    private fun updateProgress(progress: Float = currentStep, animated: Boolean = true) {
        val clamped = progress.coerceIn(0f, totalSteps.toFloat())
        if (animated && width > 0) {
            animateProgress(currentStep, clamped)
        } else {
            applyProgress(clamped)
        }
    }

    private fun applyProgress(progress: Float) {
        val ratio = progress / totalSteps
        val widthPx = width
        fillView.layoutParams.width = (widthPx * ratio).toInt()
        fillView.requestLayout()

        val thumbOffset = (widthPx * ratio) - thumbView.width / 2f
        thumbView.translationX = thumbOffset.coerceIn(0f, (widthPx - thumbView.width).toFloat())

        currentStep = progress
        stepChangeListener?.invoke(currentStep)
        //updateLabels()
    }

    private fun animateProgress(from: Float, to: Float) {
        ValueAnimator.ofFloat(from, to).apply {
            duration = 300
            addUpdateListener {
                applyProgress(it.animatedValue as Float)
            }
        }.start()
    }

    fun jumpToStep(step: Float, animated: Boolean = true) {
        updateProgress(step, animated)
    }

    fun setOnStepChangeListener(listener: (Float) -> Unit) {
        stepChangeListener = listener
    }

    fun setThumbDrawable(drawable: Drawable) {
        thumbView.setImageDrawable(drawable)
    }

    fun setBarColors(filledColor: Int, unfilledColor: Int) {
        fillView.setBackgroundColor(filledColor)
        trackView.setBackgroundColor(unfilledColor)
    }

    fun setBarHeight(dp: Int) {
        val px = dp.dp
        trackView.layoutParams.height = px
        fillView.layoutParams.height = px
        trackView.requestLayout()
        fillView.requestLayout()
    }

    fun setRoundedCorners(enabled: Boolean, radiusDp: Int = 4) {
        val radiusPx = radiusDp.dp.toFloat()
        val shape1 = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = if (enabled) radiusPx else 0f
            setColor((trackView.background as? ColorDrawable)?.color ?: Color.LTGRAY)
        }
        trackView.background = shape1

        val fillShape = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = if (enabled) radiusPx else 0f
            setColor((fillView.background as? ColorDrawable)?.color ?: Color.RED)
        }
        fillView.background = fillShape
    }

    private val Int.dp: Int get() = (this * resources.displayMetrics.density).toInt()
}
