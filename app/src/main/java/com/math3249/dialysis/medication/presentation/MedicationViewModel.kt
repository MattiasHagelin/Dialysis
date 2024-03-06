package com.math3249.dialysis.medication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.medication.data.IMedication
import com.math3249.dialysis.medication.data.Medication
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.session.ISessionCache
import com.math3249.dialysis.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MedicationViewModel(
    private val repository: IMedication,
    private val sessionCache: ISessionCache
): ViewModel() {
    private val _medications = MutableStateFlow<MutableList<Medication>?>(mutableListOf())
    private val _medication = MutableStateFlow(Medication())
    private val _state = MutableStateFlow(MedicationUiState())
    private val session: Session

    val medications = _medications.asStateFlow()
    val medication = _medication.asStateFlow()
    val state = _state.asStateFlow()

    init {
        session = sessionCache.getActiveSession() ?: Session(
            userId = "",
            email = "",
            groupId = ""
        )
        getMedications()
    }
    private fun  getMedications() {
        viewModelScope.launch {
            repository.getMedications(session.groupId).collect { result ->
                when {
                    result.isSuccess -> {
                        _medications.value = result.getOrNull()
                    }
                    result.isFailure -> {
                        result.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }
    fun getMedication(key: String) {
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
        }
    }

    fun updateEdit(edit: Boolean) {
        _state.value = _state.value.copy(
            edit = edit
        )
    }
    fun updateName(name: String) {
        _state.value = _state.value.copy(
            name = name
        )
    }

    fun updateDose(dose: String) {
        _state.value = _state.value.copy(
            dose = dose
        )
    }

    fun updateUnit(unit: String) {
        _state.value = _state.value.copy(
            unit = unit
        )
    }

    fun updateStrength(strength: String) {
        _state.value = _state.value.copy(
            strength = strength
        )
    }

    fun updateInterval(interval: String) {
        _state.value = _state.value.copy(
            interval = interval
        )
    }
    fun updateTime(time: LocalTime) {
        _state.value = _state.value.copy(
            time = time
        )
    }
    fun updateStartDate(startDate: LocalDate) {
        _state.value = _state.value.copy(
            startDate = startDate
        )
    }
    fun updatePaused(paused: Boolean) {
        _state.value = _state.value.copy(
            paused = paused
        )
    }
    fun updateNeedPrep(needPrep: Boolean) {
        _state.value = _state.value.copy(
            needPrep = needPrep
        )
    }
    fun updatePrepDesc(prepDesc: String) {
        _state.value = _state.value.copy(
            prepDescription = prepDesc
        )
    }
    fun updateExpanded(expanded: Boolean) {
        _state.value = _state.value.copy(
            expanded = expanded
        )
    }
    fun updateSelectedValue(selectedValue: String) {
        _state.value = _state.value.copy(
            selectedValue = selectedValue
        )
    }

    fun updateMedication(key: String) {
        viewModelScope.launch {
            repository.updateMedication(
                Medication(
                    key = key,
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
        }
    }

    fun saveMedication() {
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
        }
    }
}