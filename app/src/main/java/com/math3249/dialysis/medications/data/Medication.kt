package com.math3249.dialysis.medications.data

data class Medication(
    val key: String = "",
    val name: String = "",
    val dose: String = "",
    val time: String = "00:00",
    val interval: String = "",
    val strength: String = "",
    val unit: String = "",
    val checkTimestamp: String = "00:00",
    val paused: Boolean = false,
    val startDate: String = "2000-01-01",
    val needPrep: Boolean = false,
    val prepDescription: String = ""
)
