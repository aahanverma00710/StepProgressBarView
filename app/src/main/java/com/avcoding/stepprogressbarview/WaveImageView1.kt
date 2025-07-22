package com.avcoding.stepprogressbarview

import android.graphics.drawable.Drawable
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.sin

class WaveImageView1 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val wavePath = Path()
    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    private var imageBitmap: Bitmap? = null
    private var pendingDrawable: Drawable? = null

    private var waveAmplitude = 30f
    private var waveLength = 400f
    private var waveSpeed = 5f

    private var waveShift = 0f

    private var currentProgress = 0.5f
    private var targetProgress = 0.5f

    private var waveAnimator: ValueAnimator? = null
    private var progressAnimator: ValueAnimator? = null

    init {
        startWaveAnimation()
    }

    fun setImageResource(resId: Int) {
        pendingDrawable = ContextCompat.getDrawable(context, resId)
        invalidate()
    }

    fun setImageBitmap(bitmap: Bitmap) {
        this.imageBitmap = bitmap
        invalidate()
    }

    fun setWaveAmplitude(amplitude: Float) {
        waveAmplitude = amplitude
    }

    fun setWaveLength(length: Float) {
        waveLength = length
    }

    fun setWaveSpeed(speed: Float) {
        waveSpeed = speed
    }

    fun setProgress(percent: Float) {
        currentProgress = percent.coerceIn(0f, 1f)
        targetProgress = currentProgress
        invalidate()
    }

    fun setProgressAnimated(percent: Float, duration: Long = 1000) {
        progressAnimator?.cancel()
        targetProgress = percent.coerceIn(0f, 1f)
        progressAnimator = ValueAnimator.ofFloat(currentProgress, targetProgress).apply {
            this.duration = duration
            addUpdateListener {
                currentProgress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun startWaveAnimation() {
        waveAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            repeatCount = ValueAnimator.INFINITE
            duration = 2000
            addUpdateListener {
                waveShift += waveSpeed
                invalidate()
            }
            start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (pendingDrawable != null && w > 0 && h > 0) {
            imageBitmap = pendingDrawable!!.toBitmap(w, h)
            pendingDrawable = null
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        imageBitmap?.let { bitmap ->
            val saveCount = canvas.saveLayer(0f,0f,width.toFloat(),height.toFloat(),null)

            // Draw wave path as clipping mask
            buildWavePath()
            canvas.drawPath(wavePath, wavePaint)

            // Now draw the image inside that mask
            wavePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, null, Rect(0,0,width,height), wavePaint)
            wavePaint.xfermode = null

            canvas.restoreToCount(saveCount)
        }
    }

    private fun buildWavePath() {
        wavePath.reset()
        val levelY = height * (1f - currentProgress)

        val twoPi = (Math.PI * 2).toFloat()
        val stepX = 5

        wavePath.moveTo(0f, height.toFloat())
        wavePath.lineTo(0f, levelY)

        var x = 0f
        while (x <= width) {
            val y = (waveAmplitude * sin(twoPi * (x + waveShift) / waveLength)) + levelY
            wavePath.lineTo(x, y)
            x += stepX
        }

        wavePath.lineTo(width.toFloat(), height.toFloat())
        wavePath.close()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        waveAnimator?.cancel()
        progressAnimator?.cancel()
    }
}
