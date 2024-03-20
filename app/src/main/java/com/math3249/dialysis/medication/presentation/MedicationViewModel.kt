package com.math3249.dialysis.medication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.medication.data.IMedication
import com.math3249.dialysis.medication.data.Medication
import com.math3249.dialysis.medication.domain.MedicationEvent
import com.math3249.dialysis.medication.domain.RecurrenceHelper
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.session.ISessionCache
import com.math3249.dialysis.session.Session
import com.math3249.dialysis.ui.components.model.Category
import com.math3249.dialysis.util.DateTimeHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MedicationViewModel(
    private val repository: IMedication,
    sessionCache: ISessionCache
): ViewModel() {
    private val _state = MutableStateFlow(MedicationUiState())
    private val session: Session

    val state = _state.asStateFlow()

    init {
        session = sessionCache.getActiveSession() ?: Session(
            userId = "",
            email = "",
            groupId = ""
        )
        getMedications()
    }

    fun onEvent(
        event: MedicationEvent
    ) {
        when(event) {
            is MedicationEvent.UpdateMedication -> updateMedication()
            is MedicationEvent.UpdateMedicationState -> _state.update { it.copy(medication = event.value) }
            is MedicationEvent.CreateMedication -> saveMedication()
            is MedicationEvent.Clear -> clearMedicationSate()
            is MedicationEvent.Add -> newMedication()
            is MedicationEvent.Edit -> prepareEditState(event.value)
            is MedicationEvent.TogglePause -> togglePauseMedication(event.value)
            is MedicationEvent.Completed -> completeMedication(event.value)
            is MedicationEvent.UpdateSelectedUnit -> _state.update { it.copy(selectedUnit = event.value) }
            is MedicationEvent.UpdateStartDate -> _state.update { it.copy(startDate = event.value) }
            is MedicationEvent.UpdateTime -> _state.update { it.copy(time = event.value) }
            else -> return
        }
    }

    private fun togglePauseMedication(medication: Medication) {
        viewModelScope.launch {
            repository.updateMedication(
                medication.copy(paused = !medication.paused),
                session.groupId)
        }
    }

    private fun removeMedication(key: String) {
        viewModelScope.launch {
            repository.deleteMedication(key, session.groupId)
        }
    }

    private fun newMedication() {
        clearMedicationSate()
    }


    private fun  getMedications() {
        viewModelScope.launch {
            repository.getMedications(session.groupId).collect { result ->
                when {
                    result.isSuccess -> {
                        _state.update { it.copy(
                            medications = result.getOrThrow()
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
                            /*.sortedBy { medication ->
                                medication.time
                            }
                            .toMutableList()*/,
                            pausedMedications = result.getOrThrow()
                                .filter {
                                    it.paused
                                }
                                .sortedBy {
                                    it.time
                                }
                                .toMutableList()
                    )
                        }
                    }
                    result.isFailure -> {
                        result.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }
    private fun prepareEditState(medication: Medication) {
        _state.update { it.copy(
            medication = medication,
            time = LocalTime.parse(medication.time),
            startDate = LocalDate.from(
                DateTimeFormatter
                    .ofPattern(Constants.DATE_PATTERN)
                    .parse(medication.startDate)
            ),
            selectedUnit = medication.recurrence

        ) }
    }
    private fun clearMedicationSate() {
        _state.value = MedicationUiState()
    }
    private fun updateMedication() {
        viewModelScope.launch {
            repository.updateMedication(
                _state.value.medication,
//                Medication(
//                    key = _state.value.medicationKey,
//                    name = _state.value.name,
//                    dose = _state.value.dose,
//                    unit = _state.value.selectedUnit,
//                    strength = _state.value.strength,
//                    recurrence = _state.value.recurrence,
//                    startDate = DateTimeFormatter
//                        .ofPattern(Constants.DATE_PATTERN)
//                        .format(_state.value.startDate),
//                    time = DateTimeFormatter
//                        .ofPattern(Constants.TIME_24_H)
//                        .format(_state.value.time),
////                    lastCompleted = _state.value.checkTimestamp,
//                    paused = _state.value.paused,
//                    comment = _state.value.comment
//                ),
                session.groupId
            )
            clearMedicationSate()
        }
    }

    private fun saveMedication() {
        viewModelScope.launch {
            repository.createMedication(
                RecurrenceHelper()
                    .createMedicationForEveryRecurrenceOver24Hours(
                        _state.value.medication.copy(
                            startDate = DateTimeFormatter
                                .ofPattern(Constants.DATE_PATTERN)
                                .format(_state.value.startDate
                            ),
                            time = DateTimeFormatter
                                .ofPattern(Constants.TIME_24_H)
                                .format(_state.value.time
                            )
                        ),
//                        Medication(
//                            name = _state.value.name,
//                            unit = _state.value.selectedUnit,
//                            strength = _state.value.strength,
//                            recurrence = _state.value.recurrence,
//                            time = DateTimeFormatter
//                                .ofPattern(Constants.TIME_24_H)
//                                .format(_state.value.time),
//                            dose = _state.value.dose,
//                            paused = _state.value.paused,
//                            startDate = DateTimeFormatter
//                                .ofPattern(Constants.DATE_PATTERN)
//                                .format(_state.value.startDate),
//                            key = "",
//                            comment = _state.value.comment,
//                            lastCompleted = null,
//                            takeWithFood = _state.value.takeWithFood,
//                            withBreakfast = _state.value.withBreakfast,
//                            withDinner = _state .value.withDinner,
//                            withLunch = _state.value.withLunch,
//                            category = _state.value.category
//                        )
                    ),
                session.groupId
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
            ), session.groupId)
        }
    }
}