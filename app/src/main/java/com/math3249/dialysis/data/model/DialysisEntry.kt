package com.math3249.dialysis.data.model

import java.time.LocalDate

data class DialysisEntry(
    val key : String = "",
    var weightAfter: String = "",
    var weightBefore: String = "",
    var ultraFiltration: String = "",
    var date: String = "",
    var program: DialysisProgram? = null
)
