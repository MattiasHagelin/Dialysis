package com.math3249.dialysis.medication.data

sealed class MedicationEvent {
    data object Clear: MedicationEvent()
    data object Add: MedicationEvent()
    data class Edit(val value: Medication): MedicationEvent()
    data class Completed(val value: Medication): MedicationEvent()
    data class UndoCompleted(val value: Medication): MedicationEvent()
    data object CreateMedication: MedicationEvent()
    data object UpdateMedication: MedicationEvent()
    data class UpdateMedicationMethod(val value: Int): MedicationEvent()
    data class UpdateMedicationState(val value: Medication): MedicationEvent()
    data class UpdateSelectedUnit(val value: String): MedicationEvent()
    data class RemoveMedicationMethod(val value: Int): MedicationEvent()
    data class TogglePause(val value: Medication): MedicationEvent()
}