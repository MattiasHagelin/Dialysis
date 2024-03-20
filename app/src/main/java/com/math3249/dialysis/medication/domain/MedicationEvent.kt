package com.math3249.dialysis.medication.domain

import com.math3249.dialysis.medication.data.Medication
import java.time.LocalDate
import java.time.LocalTime

sealed class MedicationEvent {
    data object Clear: MedicationEvent()
    data object Add: MedicationEvent()
    data class Edit(val value: Medication): MedicationEvent()
    data class Completed(val value: Medication): MedicationEvent()
    data object Medications: MedicationEvent()
    data object CreateMedication: MedicationEvent()
    data object UpdateMedication: MedicationEvent()
    data class UpdateMedicationState(val value: Medication): MedicationEvent()
    data class UpdateSelectedUnit(val value: String): MedicationEvent()
    data class UpdateTime(val value: LocalTime): MedicationEvent()
    data class UpdateStartDate(val value: LocalDate): MedicationEvent()
    data class RemoveMedication(val value: String): MedicationEvent()
    data class TogglePause(val value: Medication): MedicationEvent()
}