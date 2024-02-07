package com.math3249.dialysis.data.model

import java.time.LocalDate

data class DialysisEntry(
    val key : String = "",
    var morningWeight: String = "",
    var eveningWeight: String = "",
    var ultraFiltration: String = "",
    var date: String = LocalDate.now().toString()
)
