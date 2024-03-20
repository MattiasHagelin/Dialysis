package com.math3249.dialysis.medication.domain

import com.math3249.dialysis.R
import com.math3249.dialysis.medication.data.Medication
import com.math3249.dialysis.other.Constants
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class RecurrenceHelper {

    private fun calculateNumberOfRecurrenceEach24Hour(interval: String): Int {
        if (interval.isNotEmpty()) {
            val hours = interval.filter { it.isDigit() }.toIntOrNull()
            return if (hours != null) {
                24 / hours
            } else {
                0
            }
        } else {
            return 0
        }
    }


    fun createMedicationForEveryRecurrenceOver24Hours(medication: Medication): List<Medication> {
        val list = mutableListOf<Medication>()
        val uuid = UUID.randomUUID().toString()
        if (medication.takeWithFood) {
            withFoodRecurrence(medication.copy(recurrenceId = uuid), list)
        } else {
            list.add(medication
                .copy(
                    recurrenceId = uuid,
                    category = LocalTime.parse(medication.time).toSecondOfDay()
                )
            )
            for (i in 1..<calculateNumberOfRecurrenceEach24Hour(medication.recurrence)) {
                val newTime =
                        LocalTime.parse(medication.time)
                            .plusHours(medication
                                .recurrence.filter {
                                    it.isDigit()
                                }
                                .toLong() * i
                            )
                list.add(
                    medication.copy(
                        time = DateTimeFormatter
                            .ofPattern(Constants.TIME_24_H)
                            .format(newTime
                            ),
                        category = newTime.toSecondOfDay(),
                        recurrenceId = uuid
                    )
                )
            }
        }
        return list.toList()
    }

    private fun withFoodRecurrence(medication: Medication, list: MutableList<Medication>) {
        if (medication.withBreakfast) {
            list.add(
                medication.copy(
                    time = DateTimeFormatter
                        .ofPattern(
                            Constants.TIME_24_H
                        )
                        .format(
                            LocalTime.of(8, 30)
                        ),
                    category = R.string.breakfast
                )
            )
        }
        if (medication.withLunch) {
            list.add(
                medication.copy(
                    time = DateTimeFormatter
                        .ofPattern(
                            Constants.TIME_24_H
                        )
                        .format(
                            LocalTime.of(12, 30)
                        ),
                    category = R.string.lunch
                )
            )
        }
        if (medication.withDinner) {
            list.add(
                medication.copy(
                    time = DateTimeFormatter
                        .ofPattern(
                            Constants.TIME_24_H
                        )
                        .format(
                            LocalTime.of(17, 30)
                        ),
                    category = R.string.dinner
                )
            )
        }
    }
}