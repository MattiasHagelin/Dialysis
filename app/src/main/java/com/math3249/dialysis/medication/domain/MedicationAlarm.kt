package com.math3249.dialysis.medication.domain

import java.time.LocalDateTime

data class MedicationAlarm(
    val id: Int,
    val time: LocalDateTime,
    val message: String
)
