package com.math3249.dialysis.util

import java.time.format.DateTimeFormatter

class DateTimeHelper {

    companion object {
        fun formatIsoDateTimeString(isoDateTime: String, pattern: String): String {
            return DateTimeFormatter
                .ofPattern(pattern)
                .format(
                    DateTimeFormatter
                        .ISO_DATE_TIME
                        .parse(isoDateTime)
                )
        }
    }
}