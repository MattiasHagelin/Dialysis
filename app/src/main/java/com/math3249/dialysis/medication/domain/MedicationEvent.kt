package com.math3249.dialysis.medication.domain

import com.math3249.dialysis.medication.data.Medication
import java.time.LocalDate
import java.time.LocalTime

sealed class MedicationEvent {
    data object Clear: MedicationEvent()
    data object SignOut: MedicationEvent()
    data object Back: MedicationEvent()
    data object Add: MedicationEvent()
    data class Edit(val value: String): MedicationEvent()
    data object Medications: MedicationEvent()
    data object UpdateMedication: MedicationEvent()
    data object CreateMedication: MedicationEvent()
    data object GetMedication: MedicationEvent()
    data class UpdateName(val value: String): MedicationEvent()
    data class UpdateDose(val value: String): MedicationEvent()
    data class UpdateInterval(val value: String): MedicationEvent()
    data class UpdateStrength(val value: String): MedicationEvent()
    data class UpdateUnit(val value: String): MedicationEvent()
    data class UpdateLastChecked(val value: String): MedicationEvent()
    data class UpdatePrepDescription(val value: String): MedicationEvent()
    data class UpdateSelectedValue(val value: String): MedicationEvent()
    data class UpdateExpanded(val value: Boolean): MedicationEvent()
    data class UpdateNeedPrep(val value: Boolean): MedicationEvent()
    data class UpdatePaused(val value: Boolean): MedicationEvent()
    data class Pause(val value: Medication): MedicationEvent()
    data class UpdateTime(val value: LocalTime): MedicationEvent()
    data class UpdateStartDate(val value: LocalDate): MedicationEvent()
    data class UpdateMedicationKey(val value: String): MedicationEvent()
    data class RemoveMedication(val value: String): MedicationEvent()

}