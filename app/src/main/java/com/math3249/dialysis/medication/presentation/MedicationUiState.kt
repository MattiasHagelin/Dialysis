package com.math3249.dialysis.medication.presentation

import com.math3249.dialysis.medication.data.Medication
import com.math3249.dialysis.ui.components.model.Category
import java.time.LocalDate
import java.time.LocalTime

data class MedicationUiState(
    val medications: List<Category<Medication>> = listOf(),
    val completedMedications: List<Category<Medication>> = listOf(),
    val pausedMedications: List<Medication> = listOf(),
    val medication: Medication = Medication(),
    val time: LocalTime = LocalTime.NOON,
    val startDate: LocalDate = LocalDate.now(),
    val selectedUnit: String = "",
)
