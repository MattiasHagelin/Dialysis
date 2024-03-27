package com.math3249.dialysis.medication.data

import com.math3249.dialysis.other.Constants
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Medication(
    val key: String = "",
    val category: Int = 0,
    val name: String = "",
    val dose: String = "",
    val time: String = "00:00",
    val recurrence: String = "",
    val recurrenceId: String = "",
    val strength: String = "",
    val unit: String = "",
    val lastCompleted: String? = null,
    val startDate: String = DateTimeFormatter.ofPattern(Constants.DATE_PATTERN).format(LocalDate.now()),
    val paused: Boolean = false,
    val takeWithFood: Boolean = false,
    val withBreakfast: Boolean = false,
    val withLunch: Boolean = false,
    val withDinner: Boolean = false,
    val withOther: Boolean = false,
    val doNotTakeWithFood: Boolean = false,
    val comment: String = ""
)
