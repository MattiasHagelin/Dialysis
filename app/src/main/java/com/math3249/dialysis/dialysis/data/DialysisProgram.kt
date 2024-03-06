package com.math3249.dialysis.dialysis.data

data class DialysisProgram(
    val name: String = "",
    val time: String = "",
    val noOfCycles: Int = 0,
    val dialysisFluids: List<String> = listOf()
)
