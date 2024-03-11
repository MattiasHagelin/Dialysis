package com.math3249.dialysis.medication.presentation

import com.math3249.dialysis.medication.data.Medication
import java.time.LocalDate
import java.time.LocalTime

data class MedicationUiState(
    val medications: MutableList<Medication> = mutableListOf(),
    val pausedMedications: MutableList<Medication> = mutableListOf(),
    val name: String = "",
    val dose: String = "",
    val time: LocalTime = LocalTime.now(),
    val interval: String = "",
    val strength: String = "",
    val unit: String = "",
    val checkTimestamp: String? = null,
    val paused: Boolean = false,
    val startDate: LocalDate = LocalDate.now(),
    val needPrep: Boolean = false,
    val prepDescription: String = "",
    val expanded: Boolean = false,
    val selectedValue: String = "",
    val medicationKey: String = ""
)
