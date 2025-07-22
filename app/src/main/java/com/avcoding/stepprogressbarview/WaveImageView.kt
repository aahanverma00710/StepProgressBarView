package com.avcoding.stepprogressbarview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.sin

class WaveImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val wavePath = Path()
    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shapePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var imageBitmap: Bitmap? = null
    private var foregroundBitmap: Bitmap? = null
    private var pendingDrawable: Drawable? = null

    private var waveAmplitude = 20f
    private var waveLength = 300f
    private var waveSpeed = 8f

    private var waveShift = 0f

    private var currentProgress = 0.3f
    private var targetProgress = 0.3f

    private var waveAnimator: ValueAnimator? = null
    private var progressAnimator: ValueAnimator? = null

    init {
        startWaveAnimation()
        setupPaints()
    }

    private fun setupPaints() {
        wavePaint.isAntiAlias = true
        foregroundPaint.isAntiAlias = true
        shapePaint.isAntiAlias = true
        shapePaint.style = Paint.Style.FILL
    }

    fun setImageResource(resId: Int) {
        pendingDrawable = ContextCompat.getDrawable(context, resId)
        invalidate()
    }

    fun setImageBitmap(bitmap: Bitmap) {
        this.imageBitmap = bitmap
        createForegroundBitmap(bitmap)
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
        val newProgress = percent.coerceIn(0f, 1f)
        currentProgress = newProgress
        targetProgress = newProgress
        invalidate()
    }

    fun setProgressAnimated(percent: Float, duration: Long = 800) {
        progressAnimator?.cancel()
        targetProgress = percent.coerceIn(0f, 1f)
        progressAnimator = ValueAnimator.ofFloat(currentProgress, targetProgress).apply {
            this.duration = duration
            interpolator = android.view.animation.DecelerateInterpolator()
            addUpdateListener {
                currentProgress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun createForegroundBitmap(bitmap: Bitmap) {
        foregroundBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        // Extract only the dark pixels (eyes, smile, etc.)
        for (i in pixels.indices) {
            val pixel = pixels[i]
            val alpha = Color.alpha(pixel)

            if (alpha > 0) {
                val brightness = getBrightness(pixel)
                // Only keep very dark pixels (eyes, smile, etc.)
                if (brightness < 0.2f) {
                    pixels[i] = pixel
                } else {
                    pixels[i] = Color.TRANSPARENT
                }
            }
        }

        foregroundBitmap!!.setPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
    }

    private fun getBrightness(color: Int): Float {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return (r * 0.299f + g * 0.587f + b * 0.114f) / 255f
    }

    private fun startWaveAnimation() {
        waveAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            repeatCount = ValueAnimator.INFINITE
            duration = 1200
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
            val bitmap = pendingDrawable!!.toBitmap(w, h)
            setImageBitmap(bitmap)
            pendingDrawable = null
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        imageBitmap?.let { bitmap ->
            // Step 1: Create a shape mask from the original image
            val shapeMask = createShapeMask(bitmap)

            // Step 2: Draw the progress fill with wave animation using the image
            if (currentProgress > 0) {
                val saveCount = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)

                // Draw the wave fill using the image bitmap
                buildWavePath()
                canvas.save()
                canvas.clipPath(wavePath)
                canvas.drawBitmap(bitmap, null, Rect(0, 0, width, height), wavePaint)
                canvas.restore()

                // Clip to the shape mask
                val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
                }
                canvas.drawBitmap(shapeMask, null, Rect(0, 0, width, height), maskPaint)

                canvas.restoreToCount(saveCount)
            }

            // Step 3: Draw the foreground elements (eyes, smile) on top
            foregroundBitmap?.let { fgBitmap ->
                canvas.drawBitmap(fgBitmap, null, Rect(0, 0, width, height), foregroundPaint)
            }
        }
    }

    private fun createShapeMask(bitmap: Bitmap): Bitmap {
        val maskBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val maskCanvas = Canvas(maskBitmap)

        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        // Create a mask where non-transparent pixels become white
        for (i in pixels.indices) {
            val pixel = pixels[i]
            val alpha = Color.alpha(pixel)

            if (alpha > 50) {
                val brightness = getBrightness(pixel)
                // Include all non-dark pixels in the shape mask
                if (brightness > 0.2f) {
                    pixels[i] = Color.WHITE
                } else {
                    pixels[i] = Color.TRANSPARENT
                }
            } else {
                pixels[i] = Color.TRANSPARENT
            }
        }

        maskBitmap.setPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return maskBitmap
    }

    private fun buildWavePath() {
        wavePath.reset()
        val levelY = height * (1f - currentProgress)

        val twoPi = (Math.PI * 2).toFloat()
        val stepX = 3f

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