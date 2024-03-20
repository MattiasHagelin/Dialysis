package com.math3249.dialysis.navigation

sealed class NavigateEvent {
    data object ToStart : NavigateEvent()
    data object ToDialysisEntryScreen : NavigateEvent()
    data object ToMedicationSaveScreen : NavigateEvent()
//    data object ToPrevious : NavigateEvent()
    data class ToPrevious(val route: String) : NavigateEvent()
    data object ToFluidBalanceHistory : NavigateEvent()
    data object ToUpdateFluidBalanceLimit : NavigateEvent()
    data object ToSignInScreen : NavigateEvent()
    data object ToFirstTimeSignIn : NavigateEvent()
    data object ToSignIn : NavigateEvent()
    data object ToFluidBalance : NavigateEvent()
    data object ToDialysis : NavigateEvent()
    data object ToMedications : NavigateEvent()
    data object ToPausedMedication: NavigateEvent()
    data object ToBloodPressure : NavigateEvent()

}