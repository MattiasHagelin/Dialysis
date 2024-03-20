package com.math3249.dialysis.medication.presentation

import com.math3249.dialysis.medication.data.Medication
import com.math3249.dialysis.ui.components.model.Category
import java.time.LocalDate
import java.time.LocalTime

data class MedicationUiState(
    val medications: List<Category<Medication>> = listOf(),
    val pausedMedications: List<Medication> = listOf(),
    val medication: Medication = Medication(),
//    val name: String = "",
//    val category: Int = 0,
//    val dose: String = "",
    val time: LocalTime = LocalTime.NOON,
//    val recurrence: String = "",
//    val strength: String = "",
//    val unit: String = "",
////    val checkTimestamp: String? = null,
//    val paused: Boolean = false,
    val startDate: LocalDate = LocalDate.now(),
//    val expanded: Boolean = false,
    val selectedUnit: String = "",
//    val medicationKey: String = "",
//    val takeWithFood: Boolean = false,
//    val withBreakfast: Boolean = false,
//    val withLunch: Boolean = false,
//    val withDinner: Boolean = false,
//    val withOther: Boolean = false,
//    val takeWithoutFood: Boolean = false,
//    val comment: String = "",
//    val showExtra: Boolean = false
)
