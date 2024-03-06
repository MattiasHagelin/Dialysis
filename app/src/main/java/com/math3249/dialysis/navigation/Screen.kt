package com.math3249.dialysis.navigation

sealed class Screen(val route: String) {
    data object SignInScreen : Screen("sign_in")
    data object StartScreen: Screen("start")
    data object DialysisScreen : Screen("dialysis")
    data object DialysisOverviewScreen: Screen("dialysis_overview")
    data object FluidBalanceScreen: Screen("fluid_balance")
    data object MedicationScreen: Screen("medication")
    data object MedicationOverview: Screen("medication_overview")
    data object MedicationSaveScreen: Screen("save_medication")

    fun withNullableArgs(vararg args: String?): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                if (arg != null) {
                    append("?medicationKey=$arg")
                }

            }
        }
    }
}