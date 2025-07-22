package com.avcoding.stepprogressbarview

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class WaveActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wave)
        val waveImageView = findViewById<WaveImageView>(R.id.waveImageView)
        /*waveImageView.setImageResource(R.drawable.ic_ecstasy)
        waveImageView.setImageResource(R.drawable.ic_ecstasy)
      *//*  waveImageView.setWaveAmplitude(40f)
        waveImageView.setWaveLength(300f)
        waveImageView.setWaveSpeed(8f)*//*
        waveImageView.setProgress(0.7f)*/

        waveImageView.setImageResource(R.drawable.ic_ecstasy)

// Fine-tune the separation if needed
       // waveImageView.setColorTolerance(100) // Lower = more strict, Higher = more lenient
        waveImageView.setWaveAmplitude(25f) // Wave height
        waveImageView.setWaveLength(250f)   // Wave frequency
        waveImageView.setWaveSpeed(10f)     // Wave animation speed
// Customize wave colors
      //  waveImageView.setWaveColor(Color.parseColor("#FF6B35"))
        waveImageView.setBackgroundColor(Color.TRANSPARENT)
        waveImageView.setProgressAnimated(.5f,1500)

// Animate progress
   //     waveImageView.setProgressAnimated(0.8f, 1500)
        val waveImageView1 = findViewById<WaveImageView1>(R.id.waveImageView1)
        waveImageView1.setImageResource(R.drawable.ic_ecstasy)
    /*      waveImageView.setWaveAmplitude(40f)
          waveImageView.setWaveLength(300f)
          waveImageView.setWaveSpeed(8f)*/
        waveImageView1.setProgress(0.5f)

    }
}