package com.math3249.dialysis.medication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.medication.data.IMedication
import com.math3249.dialysis.medication.data.Medication
import com.math3249.dialysis.medication.domain.MedicationEvent
import com.math3249.dialysis.navigation.NavigateEvent
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.session.ISessionCache
import com.math3249.dialysis.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MedicationViewModel(
    private val repository: IMedication,
    private val onNavigateEvent: (NavigateEvent ) -> Unit,
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
            is MedicationEvent.CreateMedication -> saveMedication()
            is MedicationEvent.SignOut -> onNavigateEvent(NavigateEvent.ToSignIn)
            is MedicationEvent.Clear -> clearMedicationSate()
            is MedicationEvent.Back -> onNavigateEvent(NavigateEvent.ToPrevious)
            is MedicationEvent.Add -> newMedication()
            is MedicationEvent.Edit -> getMedication(event.value)
            is MedicationEvent.Pause -> pauseMedication(event.value)
            is MedicationEvent.GetMedication -> getMedication()
            is MedicationEvent.UpdateName -> _state.update { it.copy(name = event.value) }
            is MedicationEvent.UpdateDose -> _state.update { it.copy(dose = event.value) }
            is MedicationEvent.UpdateExpanded -> _state.update { it.copy(expanded = event.value) }
            is MedicationEvent.UpdateSelectedValue -> _state.update { it.copy(selectedValue = event.value) }
            is MedicationEvent.UpdateInterval -> _state.update { it.copy(interval = event.value) }
            is MedicationEvent.UpdatePaused -> _state.update { it.copy(paused = event.value) }
            is MedicationEvent.UpdateNeedPrep -> _state.update { it.copy(needPrep = event.value) }
            is MedicationEvent.UpdatePrepDescription -> _state.update { it.copy(prepDescription = event.value) }
            is MedicationEvent.UpdateStartDate -> _state.update { it.copy(startDate = event.value) }
            is MedicationEvent.UpdateTime -> _state.update { it.copy(time = event.value) }
            is MedicationEvent.UpdateMedicationKey -> _state.update { it.copy(medicationKey = event.value) }
            is MedicationEvent.RemoveMedication -> removeMedication(event.value)
            else -> return
        }
    }

    private fun pauseMedication(medication: Medication) {
        viewModelScope.launch {
            repository.updateMedication(medication.copy(paused = true), session.groupId)
        }
    }

    private fun removeMedication(key: String) {
        viewModelScope.launch {
            repository.deleteMedication(key, session.groupId)
        }
    }

    private fun newMedication() {
        clearMedicationSate()
        onNavigateEvent(NavigateEvent.ToMedicationSaveScreen)
    }


    private fun  getMedications() {
        viewModelScope.launch {
            repository.getMedications(session.groupId).collect { result ->
                when {
                    result.isSuccess -> {
                        _state.value = _state.value.copy(
                            medications = result.getOrThrow()
                                .filter {
                                    !it.paused
                                }
                                .sortedBy { medication ->
                                    medication.time
                                }
                                .toMutableList(),
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
                    result.isFailure -> {
                        result.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }
    private fun getMedication() {
        viewModelScope.launch {
            repository.getMedication(_state.value.medicationKey, session.groupId) {
                _state.value = _state.value.copy(
                    name = it.name,
                    dose = it.dose,
                    time = LocalTime.parse(it.time),
                    startDate = LocalDate.parse(
                        it.startDate,
                        DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    strength = it.strength,
                    unit = it.unit,
                    interval = it.interval,
                    checkTimestamp = it.checkTimestamp,
                    paused = it.paused,
                    needPrep = it.needPrep,
                    prepDescription = it.prepDescription
                )
            }
        }
    }
    private fun getMedication(key: String) {
        _state.update { it.copy(medicationKey = key) }
        viewModelScope.launch {
            repository.getMedication(key, session.groupId) {
                _state.value = _state.value.copy(
                    name = it.name,
                    dose = it.dose,
                    time = LocalTime.parse(it.time),
                    startDate = LocalDate.parse(
                        it.startDate,
                        DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    strength = it.strength,
                    unit = it.unit,
                    interval = it.interval,
                    checkTimestamp = it.checkTimestamp,
                    paused = it.paused,
                    needPrep = it.needPrep,
                    prepDescription = it.prepDescription
                )
            }
            onNavigateEvent(NavigateEvent.ToMedicationSaveScreen)
        }
    }
    private fun clearMedicationSate() {
        _state.value = _state.value.copy(
            name = "",
            dose = "",
            unit = "",
            strength = "",
            interval = "",
            startDate = LocalDate.now(),
            time = LocalTime.NOON,
            checkTimestamp = "",
            prepDescription = "",
            needPrep = false,
            paused = false,
            medicationKey = ""
        )
    }
    private fun updateMedication() {
        viewModelScope.launch {
            repository.updateMedication(
                Medication(
                    key = _state.value.medicationKey,
                    name = _state.value.name,
                    dose = _state.value.dose,
                    unit = _state.value.unit,
                    strength = _state.value.strength,
                    interval = _state.value.interval,
                    startDate = DateTimeFormatter
                        .ofPattern(Constants.DATE_PATTERN)
                        .format(_state.value.startDate),
                    time = DateTimeFormatter
                        .ofPattern(Constants.TIME_24_H)
                        .format(_state.value.time),
                    checkTimestamp = _state.value.checkTimestamp,
                    prepDescription = _state.value.prepDescription,
                    needPrep = _state.value.needPrep,
                    paused = _state.value.paused
                ),
                session.groupId
            )
            clearMedicationSate()
        }
    }

    private fun saveMedication() {
        viewModelScope.launch {
            val medication = Medication(
                name = _state.value.name,
                unit = _state.value.unit,
                strength = _state.value.strength,
                interval = _state.value.interval,
                time = DateTimeFormatter
                    .ofPattern(Constants.TIME_24_H)
                    .format(_state.value.time),
                dose = _state.value.dose,
                paused = _state.value.paused,
                needPrep = _state.value.needPrep,
                prepDescription = _state.value.prepDescription,
                startDate = DateTimeFormatter
                    .ofPattern(Constants.DATE_PATTERN)
                    .format(_state.value.startDate),
                key = ""
            )
            repository.createMedication(medication, session.groupId)
            clearMedicationSate()
        }
    }
}