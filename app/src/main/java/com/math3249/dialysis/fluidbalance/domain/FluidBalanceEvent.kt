package com.math3249.dialysis.fluidbalance.domain

sealed class FluidBalanceEvent {
    data class UpdateGivenVolume(val value: String) : FluidBalanceEvent()
    data object UpdateFluidLimit: FluidBalanceEvent()
    data object UpdateDrunkVolume : FluidBalanceEvent()
    data class UpdateShowDialog(val value: Boolean): FluidBalanceEvent()
    data class UpdateOtherText(val value: String): FluidBalanceEvent()
    data class UpdateEditFluidLimit(val value: String): FluidBalanceEvent()
    data class UpdateSelectedFluid(val value: Int): FluidBalanceEvent()
    data object ClearHistory: FluidBalanceEvent()
    data object Reset: FluidBalanceEvent()

}