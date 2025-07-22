package com.avcoding.stepprogressbarview

data class TimeSegment(
    val durationInSeconds: Int,
    val label: String,
    val color: Int
)

data class TimeBar(
    val totalLabel: String,
    val segments: List<TimeSegment>
)