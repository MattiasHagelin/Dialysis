package com.math3249.dialysis.fluidbalance.data

data class FluidBalance(
    val fluidLimit: Int = 650,
    val drunkVolume: Int = 0,
    val lastDrunkTime: String = "",
    val lastDrunkVolume: String = "",
    val volumeUnit: String = ""
)
