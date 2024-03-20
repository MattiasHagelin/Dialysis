package com.math3249.dialysis.navigation

sealed class Screen(val route: String) {
    data object FluidBalanceUpdateFluidLimit: Screen("fluid_balance_update_fluid_limit")
    data object SignInScreen : Screen("sign_in")
    data object  AppStartScreen: Screen("app_start")
    data object FirstTimeSignInScreen: Screen("first_sign_in")
    data object StartScreen: Screen("start")
    data object DialysisScreen : Screen("dialysis")
    data object DialysisOverviewScreen: Screen("dialysis_overview")
    data object DialysisEntryScreen: Screen("dialysis_entry")
    data object FluidBalanceScreen: Screen("fluid_balance")
    data object FluidBalanceDayScreen: Screen("fluid_balance_day")
    data object FluidBalanceHistoryScreen: Screen("fluid_balance_history")
    data object MedicationScreen: Screen("medication")
    data object MedicationOverview: Screen("medication_overview")
    data object MedicationSaveScreen: Screen("save_medication")
    data object MedicationPaused: Screen("pause_medication")
    data object BloodPressureScreen: Screen("blood_pressure")
    data object BloodPressureOverview: Screen("blood_pressure_overview")
}