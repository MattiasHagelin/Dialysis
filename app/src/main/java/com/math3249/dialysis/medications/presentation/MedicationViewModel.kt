package com.math3249.dialysis.medications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.BaseApp
import com.math3249.dialysis.medications.data.IMedication
import com.math3249.dialysis.medications.data.Medication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MedicationViewModel(
    private val repository: IMedication
): ViewModel() {
    private val _startDate = MutableStateFlow(LocalDate.now())
    private val _time = MutableStateFlow(LocalTime.now())
    private val _expanded = MutableStateFlow(false)
    private val _selectedValue = MutableStateFlow("")
    private val _dose = MutableStateFlow("")
    private val _name = MutableStateFlow("")
    private val _interval = MutableStateFlow("")
    private val _paused = MutableStateFlow(false)
    private val _needPrep = MutableStateFlow(false)
    private val _prepDesc = MutableStateFlow("")

    val startDate = _startDate.asStateFlow()
    val time = _time.asStateFlow()
    val expanded = _expanded.asStateFlow()
    val selectedValue = _selectedValue.asStateFlow()
    val dose = _dose.asStateFlow()
    val name = _name.asStateFlow()
    val interval = _interval.asStateFlow()
    val paused = _paused.asStateFlow()
    val needPrep = _needPrep.asStateFlow()
    val prepDesc = _prepDesc.asStateFlow()

    fun setPrepDesc(value: String) {
        _prepDesc.value = value
    }
    fun setNeedPrep(value: Boolean) {
        _needPrep.value = value
    }

    fun setPaused(value: Boolean) {
        _paused.value = value
    }
    fun setInterval(value: String){
        _interval.value = value
    }
    fun setName(value: String) {
        _name.value = value
    }
    fun setDose(value: String) {
        _dose.value = value
    }
    fun setSelectedValue(value: String) {
        _selectedValue.value = value
    }
    fun showMenu(show: Boolean) {
        _expanded.value = show
    }
    fun setStartDate(date: LocalDate) {
        _startDate.value = date
    }

    fun formatDate(date: LocalDate): String {
        return DateTimeFormatter
            .ofPattern("dd-MM-yyyy")
            .format(date)
    }

    fun formatTime(time: LocalTime): String {
        return DateTimeFormatter
            .ofPattern("HH:mm")
            .format(time)
    }

    fun setTime(time: LocalTime) {
        _time.value = time
    }

    fun saveMedication() {
        viewModelScope.launch {
            val medication = Medication(
                name = _name.value,
                unit = "",
                strength = "",
                interval = "",
                time = formatTime(_time.value),
                dose = _dose.value,
                paused = false,
                needPrep = false,
                startDate = formatDate(_startDate.value),
                checkTimestamp = formatTime(LocalTime.now()),
                key = ""
            )
            repository.createMedication(medication, BaseApp.groupKey)
        }
    }
}