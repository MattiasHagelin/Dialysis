package com.math3249.dialysis.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MedicationViewModel(
//    private val medicationsRepository: MedicationsRepository
): ViewModel() {
    private val _startDate = MutableStateFlow(LocalDate.now())
    private val _time = MutableStateFlow(LocalTime.now())
    private val _expanded = MutableStateFlow(false)
    private val _selectedValue = MutableStateFlow("")
    private val _dose = MutableStateFlow("")
    private val _name = MutableStateFlow("")

    val startDate = _startDate.asStateFlow()
    val time = _time.asStateFlow()
    val expanded = _expanded.asStateFlow()
    val selectedValue = _selectedValue.asStateFlow()
    val dose = _dose.asStateFlow()
    val name = _name.asStateFlow()

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
}