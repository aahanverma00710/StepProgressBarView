package com.avcoding.stepprogressbarview

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.avcoding.lib.ProgressViewConfig
import com.avcoding.lib.StepProgressBarView
import com.avcoding.lib.setUp
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private val progress1: StepProgressBarView by lazy {
        findViewById(R.id.progressBar1)
    }

    private val progress2: StepProgressBarView by lazy {
        findViewById(R.id.progressBar2)
    }
    private var currentStep = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpClicks()
        setUpProgressBar()
    }


    private fun setUpClicks() {
        findViewById<Button>(R.id.btnIncrease).setOnClickListener{
            currentStep += .5f
            progress1.jumpToStep(abs(currentStep))
            progress2.jumpToStep(abs(currentStep))
        }

        findViewById<Button>(R.id.btnDecrease).setOnClickListener{
            currentStep -= .5f
            progress1.jumpToStep(abs(currentStep))
            progress2.jumpToStep(abs(currentStep))
        }
    }

    private fun setUpProgressBar() {
        val config1 = ProgressViewConfig(
            totalSteps = 5,
            labelSuffix = "m",
            barHeightDp = 8,
            setRoundedCorners = true,
            roundRadius = 8,
            thumbDrawable = ContextCompat.getDrawable(this@MainActivity,R.drawable.ic_launcher_background),
            barColor = Pair(Color.RED, Color.LTGRAY)

        )
        progress1.setUp(config1){

        }

        val config2 = ProgressViewConfig(
            totalSteps = 10,
            labelSuffix = "m",
            barHeightDp = 8,
            setRoundedCorners = true,
            roundRadius = 8,
            thumbDrawable = ContextCompat.getDrawable(this@MainActivity,R.drawable.ic_launcher_background),
            barColor = Pair(Color.RED, Color.LTGRAY))

        progress2.setUp(config2){

        }

    }

}