package com.avcoding.stepprogressbarview

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TimeBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_bar)
        val segments = listOf(
            TimeSegment(60, "1m", Color.parseColor("#29B6F6")),
            TimeSegment(210, "3m 30s", Color.parseColor("#0D47A1")),
            TimeSegment(10, "10s", Color.parseColor("#B3E5FC"))
        )

        val bar = TimeBar(
            totalLabel = "4m 10s",
            segments = segments
        )

        findViewById<VerticalTimeBarView>(R.id.timeBarView1).setTimeBar(bar)


        val segments1 = listOf(
            TimeSegment(60, "1m", Color.parseColor("#29B6F6")),
            TimeSegment(190, "3m 10s", Color.parseColor("#0D47A1")),
            TimeSegment(20, "20s", Color.parseColor("#B3E5FC"))
        )

        val bar1 = TimeBar(
            totalLabel = "4m 10s",
            segments = segments1
        )

        findViewById<VerticalTimeBarView>(R.id.timeBarView2).setTimeBar(bar1)
    }
}