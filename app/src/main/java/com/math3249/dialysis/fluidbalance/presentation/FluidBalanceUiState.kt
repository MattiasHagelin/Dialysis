package com.math3249.dialysis.fluidbalance.presentation

import androidx.compose.ui.focus.FocusRequester
import com.math3249.dialysis.R
import com.math3249.dialysis.fluidbalance.data.FluidBalanceHistory
import com.math3249.dialysis.ui.components.model.Category

data class FluidBalanceUiState(
    val history: List<Category<FluidBalanceHistory>> = listOf(),
    val givenVolume: String = "",
    val remainingFluid: Int = 0,
    val fluidLimit: Int = 0,
    val drunkVolume: Int = 0,
    val volumeUnit: String = "",
    val focusRequester: FocusRequester? = null,
    val showDialog: Boolean = false,
    val lastDrunkTime: String = "",
    val lastDrunkVolume: String = "",
    val otherText: String = "",
    val editFluidLimit: String = "",
    val selectedFluid: Int = R.string.water
)
