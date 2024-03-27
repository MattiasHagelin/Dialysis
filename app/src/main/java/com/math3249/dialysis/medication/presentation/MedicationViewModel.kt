package com.math3249.dialysis.medication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.medication.data.IMedication
import com.math3249.dialysis.medication.data.Medication
import com.math3249.dialysis.medication.data.MedicationEvent
import com.math3249.dialysis.ui.components.model.Category
import com.math3249.dialysis.util.DateTimeHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MedicationViewModel(
    private val repository: IMedication
): ViewModel() {
    private val _state = MutableStateFlow(MedicationUiState())
    private val _medications = MutableStateFlow(mutableListOf<Medication>())

    val state = _state.asStateFlow()

    init {
        getMedications()
    }

    fun onEvent(
        event: MedicationEvent
    ) {
        when(event) {
            is MedicationEvent.UpdateMedication -> updateMedication()
            is MedicationEvent.UpdateMedicationState -> {
                _state.update {
                    it.copy(medication = event.value)
                }
            }
            is MedicationEvent.CreateMedication -> saveMedication()
            is MedicationEvent.Clear -> clearMedicationSate()
            is MedicationEvent.Add -> newMedication()
            is MedicationEvent.Edit -> prepareEditState(event.value)
            is MedicationEvent.TogglePause -> togglePauseMedication(event.value)
            is MedicationEvent.Completed -> completeMedication(event.value)
            is MedicationEvent.UndoCompleted -> undoCompleted(event.value)
            is MedicationEvent.UpdateSelectedUnit -> _state.update { it.copy(selectedUnit = event.value) }
            is MedicationEvent.UpdateMedicationMethod -> selectUpdateMedicationMethod(event.value)
            is MedicationEvent.RemoveMedicationMethod -> selectRemoveMethod(event.value)
        }
    }

    private fun selectRemoveMethod(value: Int) {
        when (value) {
            0 -> removeMedication()
            1 -> removeRecurrentMedication()
            else -> {}
        }
    }

    private fun selectUpdateMedicationMethod(value: Int) {
        when (value) {
            0 -> updateMedication()
            1 -> updateRecurrentMedication()
            else -> {}
        }
    }

    private fun togglePauseMedication(medication: Medication) {
        viewModelScope.launch {
            repository.updateMedication(
                medication.copy(paused = !medication.paused)
            )
        }
    }

    private fun removeMedication() {
        viewModelScope.launch {
            repository.deleteMedication(_state.value.medication.key)
        }
    }

    private fun removeRecurrentMedication() {
        viewModelScope.launch {
            repository.deleteRecurrentMedications(
                _medications.value
                    .filter {
                        it.recurrenceId == _state.value.medication.recurrenceId
                    }
            )
        }
    }

    private fun newMedication() {
        clearMedicationSate()
    }


    private fun  getMedications() {
        viewModelScope.launch {
            repository.getMedications().collect { result ->
                when {
                    result.isSuccess -> {
                        val medications = result.getOrThrow()
                        getActiveMedications(medications)
                        getCompletedMedications(medications)
                        getPausedMedications(medications)
                        _medications.value = medications
//                        createNotifications()
                    }
                    result.isFailure -> {
                        result.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }

    private fun getPausedMedications(medications: MutableList<Medication>) {
        _state.update { state ->
            state.copy(
                pausedMedications = medications
                    .filter {
                        it.paused
                    }
                    .sortedBy {
                        it.time
                    }
            )
        }
    }

    private fun getActiveMedications(result: MutableList<Medication>) {
        _state.update { state ->
            state.copy(
                medications = result
                    .filter { medication ->
                        !medication.paused &&
                                !DateTimeHelper.hasPassed(medication.lastCompleted)
                    }
                    .sortedBy { it.time }
                    .groupBy { medication ->
                        medication.category
                    }
                    .map { mappedMedication ->
                        Category(
                            name = "",
                            nameAsInt = mappedMedication.key,
                            items = mappedMedication.value
                        )
                    }
            )
        }
    }

    private fun getCompletedMedications(result: MutableList<Medication>) {
        _state.update { state ->
            state.copy(
                completedMedications = result
                    .filter { medication ->
                        !medication.paused &&
                                DateTimeHelper.isToday(medication.lastCompleted)
                    }
                    .sortedBy { it.time }
                    .groupBy { it.category }
                    .map { mappedMed ->
                        Category(
                            name = "",
                            nameAsInt = mappedMed.key,
                            items = mappedMed.value
                        )
                    }
            )
        }
    }

    private fun prepareEditState(medication: Medication) {
        _state.update { it.copy(
            medication = medication/*,
            time = LocalTime.parse(medication.time),
            startDate = LocalDate.from(
                DateTimeFormatter
                    .ofPattern(Constants.DATE_PATTERN)
                    .parse(medication.startDate)
            ),
            selectedUnit = medication.unit*/
        ) }
    }
    private fun clearMedicationSate() {
        _state.value = MedicationUiState()
    }
    private fun updateRecurrentMedication() {
        viewModelScope.launch {
            repository.updateRecurrentMedications(
                _state.value.medication,
                _medications.value
                    .filter {
                        it.recurrenceId == _state.value.medication.recurrenceId
                                && it.key != _state.value.medication.key
                    }
            )
        }
    }
    private fun updateMedication() {
        viewModelScope.launch {
            repository.updateMedication(_state.value.medication)
            clearMedicationSate()
        }
    }

    private fun saveMedication() {
        viewModelScope.launch {
            repository.createMedication(
                _state.value.medication
            )
            clearMedicationSate()
        }
    }

    private fun completeMedication(medication: Medication) {
        viewModelScope.launch {
            repository.updateMedication(medication.copy(
                lastCompleted = DateTimeHelper
                    .dateTimeAsISOString(
                        LocalDateTime.now()
                    )
                )
            )
        }
    }
    private fun undoCompleted(medication: Medication) {
        viewModelScope.launch {
            repository.updateMedication(medication.copy(
                lastCompleted = DateTimeHelper
                    .dateTimeAsISOString(
                        LocalDateTime.now().minusDays(1)
                    )
                )
            )
        }
    }
}