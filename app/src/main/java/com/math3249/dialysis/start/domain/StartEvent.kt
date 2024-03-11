package com.math3249.dialysis.start.domain

sealed class StartEvent {
    data object SignOut: StartEvent()
    data object SeeFluidBalance: StartEvent()
    data object SeeDialysis: StartEvent()
    data object SeeMedications: StartEvent()
    data object SeeBloodPressure: StartEvent()
    data object Share: StartEvent()
    data class UpdateShareToEmail(val value: String): StartEvent()

}