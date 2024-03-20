package com.math3249.dialysis.fluidbalance.data

import com.math3249.dialysis.R

data class FluidBalanceHistory(
    val drunkTimeStamp: String = "",
    val drunkVolume: String = "",
    val volumeUnit: String = "ml",
    val fluidType: Int = R.string.water,
    val extraText: String = ""
)
