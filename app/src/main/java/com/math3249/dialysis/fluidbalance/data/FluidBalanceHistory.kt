package com.math3249.dialysis.fluidbalance.data

data class FluidBalanceHistory(
    val drunkTimeStamp: String = "",
    val drunkVolume: Int = 0,
    val volumeUnit: String = "ml",
    val fluidType: String = ""
)
