package com.math3249.dialysis.data.model

data class Medicine(
    val key: String,
    val name: String,
    val dose: String,
    val time: String,
    val interval: String,
    val strength: String,
    val unit: String,
    val checkTimestamp: String,
    val paused: Boolean,
    val startDate: String,
    val needPrep: Boolean
)
