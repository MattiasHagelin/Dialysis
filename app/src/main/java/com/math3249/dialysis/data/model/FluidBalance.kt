package com.math3249.dialysis.data.model

data class FluidBalance(
    var fluidLimit: Int = 650,
    var consumedFluid: Int = 0,
    var date: String = ""
)
