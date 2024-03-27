package com.math3249.dialysis.util

import com.math3249.dialysis.other.Constants
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

class DateTimeHelper {

    companion object {
        fun dateTimeFromTimeString(time: String): LocalDateTime {
            var ldt = LocalDateTime.of(LocalDate.now(), LocalTime.parse(time))
            val now = LocalDateTime.now()
            if (now >= ldt)
                ldt = ldt.plusDays(1)
            return ldt
        }
        fun formatIsoDateTimeString(isoDateTime: String, pattern: String): String {
            return DateTimeFormatter
                .ofPattern(pattern)
                .format(
                    DateTimeFormatter
                        .ISO_DATE_TIME
                        .parse(isoDateTime)
                )
        }

        fun dateTimeAsISOString(timeStamp: TemporalAccessor): String {
            return DateTimeFormatter
                .ISO_DATE_TIME
                .format(timeStamp)
        }

        fun hasPassed(date: String?): Boolean {
            return if (date == null) {
                false
            } else {
                DateTimeFormatter
                    .ofPattern(Constants.DATE_PATTERN)
                    .format(LocalDateTime.now()) <=
                formatIsoDateTimeString(date, Constants.DATE_PATTERN)
            }
        }

        fun isToday(date: String?): Boolean {
            return if (date == null) {
                false
            } else {
                DateTimeFormatter
                    .ofPattern(Constants.DATE_PATTERN)
                    .format(LocalDateTime.now()) ==
                        formatIsoDateTimeString(date, Constants.DATE_PATTERN)
            }
        }

        fun localTimeOfSecondsOfDayOrNull(value: Int): LocalTime? {
            return try {
                LocalTime.ofSecondOfDay(value.toLong())
            } catch (e: Exception) {
                null
            }
        }
    }
}