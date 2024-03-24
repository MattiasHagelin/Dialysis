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

    fun takeWithFoodRecurrences(medication: Medication): Int {
        var count = 0
        if (medication.withBreakfast)
            count++
        if (medication.withLunch)
            count++
        if (medication.withDinner)
            count++
        return count
    }

    fun whichMeals(medication: Medication): MutableList<Meals> {
        val list = mutableListOf<Meals>()
        if (medication.withBreakfast)
            list.add(Meals.BREAKFAST)
        if (medication.withLunch)
            list.add(Meals.LUNCH)
        if (medication.withDinner)
            list.add(Meals.DINNER)
        return list
    }

    fun takeWithFoodCategory(medication: Medication): Int {
        return if (medication.withBreakfast)
            R.string.breakfast
        else if (medication.withLunch)
            R.string.lunch
        else
            R.string.dinner
    }
    fun recalculateRecurrence(medication: Medication): MutableList<LocalTime>  {
        val newTimes = mutableListOf<LocalTime>()
        if (!medication.takeWithFood) {
            for (i in 1 ..< calculateNumberOfRecurrenceEach24Hour(medication.recurrence)) {
                newTimes.add(LocalTime
                    .parse(medication.time)
                    .plusHours(medication
                        .recurrence.filter {
                            it.isDigit()
                        }
                        .toLong() * i
                    )
                )

            }
        }
        return newTimes
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
                            .format(newTime),
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
                            LocalTime.of(8, 30)//TODO get time from settings
                        ),
                    category = R.string.breakfast,
                    withDinner = false,
                    withLunch = false
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
                            LocalTime.of(12, 30)//TODO get time from settings
                        ),
                    category = R.string.lunch,
                    withBreakfast = false,
                    withDinner = false
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
                            LocalTime.of(17, 30)//TODO get time from settings
                        ),
                    category = R.string.dinner,
                    withBreakfast = false,
                    withLunch = false
                )
            )
        }
    }
}

enum class Meals(
    val stringResourceId: Int,
    val time: String
) {
    BREAKFAST(R.string.breakfast, "08:30"),
    LUNCH(R.string.lunch, "12:30"),
    DINNER(R.string.dinner, "17:30")
}