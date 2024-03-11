package com.math3249.dialysis.navigation

import androidx.navigation.NavController
import com.math3249.dialysis.authentication.GoogleAuthUiClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Navigator(
    private val navController: NavController,
    private val googleAuthUiClient: GoogleAuthUiClient
) {
    private fun toSignIn() {
        runBlocking {
            launch {
                googleAuthUiClient.signOut()
                navController.navigate(Screen.AppStartScreen.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        }
    }
    fun onNavigateEvent(event: NavigateEvent) {
        when (event) {
            is NavigateEvent.ToStart -> toStart()
            is NavigateEvent.ToDialysisEntryScreen -> toDialysisEntryScreen()
            is NavigateEvent.ToMedicationSaveScreen -> toMedicationSaveScreen()
            is NavigateEvent.ToPrevious -> navController.navigateUp()
            is NavigateEvent.ToFluidBalanceHistory -> toFluidBalanceHistory()
            is NavigateEvent.ToUpdateFluidBalanceLimit -> toUpdateFluidBalanceLimit()
            is NavigateEvent.ToFirstTimeSignIn -> toFirstTimeSignIn()
            is NavigateEvent.ToSignIn -> toSignIn()
            is NavigateEvent.ToFluidBalance -> navController.navigate(Screen.FluidBalanceScreen.route)
            is NavigateEvent.ToDialysis -> navController.navigate(Screen.DialysisScreen.route)
            is NavigateEvent.ToMedications -> navController.navigate(Screen.MedicationScreen.route)
            is NavigateEvent.ToBloodPressure -> navController.navigate(Screen.BloodPressureScreen.route)
            else -> return
        }
    }

    private fun toFirstTimeSignIn() = navController.navigate(Screen.FirstTimeSignInScreen.route)

    private fun toUpdateFluidBalanceLimit() = navController.navigate(Screen.FluidBalanceUpdateFluidLimit.route)
    private fun toFluidBalanceHistory() {
        navController.navigate(Screen.FluidBalanceHistoryScreen.route)
    }

    private fun toMedicationSaveScreen() {
        navController.navigate(Screen.MedicationSaveScreen.route)
    }

    private fun toStart() {
        navController.navigate(Screen.StartScreen.route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }

    private fun toDialysisEntryScreen() {
        navController.navigate(Screen.DialysisEntryScreen.route)
    }
}