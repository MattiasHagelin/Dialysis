package com.math3249.dialysis.fluidbalance.domain

sealed class FluidBalanceEvent {
    data class UpdateGivenVolume(val value: String) : FluidBalanceEvent()
    data object UpdateFluidLimit: FluidBalanceEvent()
    data object UpdateDrunkVolume : FluidBalanceEvent()
    data class UpdateShowDialog(val value: Boolean): FluidBalanceEvent()
    data class UpdateOtherText(val value: String): FluidBalanceEvent()
    data class UpdateEditFluidLimit(val value: String): FluidBalanceEvent()
    data class UpdateSelectedFluid(val value: String): FluidBalanceEvent()
    data class UpdateSelectedText(val value: String): FluidBalanceEvent()
    data object ClearHistory: FluidBalanceEvent()
    data object Back: FluidBalanceEvent()
    data object SeeHistory: FluidBalanceEvent()
    data object EditFluidLimit: FluidBalanceEvent()
    data object Reset: FluidBalanceEvent()
    data object SignOut: FluidBalanceEvent()

}