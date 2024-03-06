package com.math3249.dialysis.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.math3249.dialysis.BaseApp
import com.math3249.dialysis.dialysis.presentation.DialysisViewModel
import com.math3249.dialysis.dialysis.presentation.screen.DialysisScreen
import com.math3249.dialysis.medication.presentation.MedicationViewModel
import com.math3249.dialysis.medication.presentation.screen.MedicationListScreen
import com.math3249.dialysis.medication.presentation.screen.MedicationScreen
import com.math3249.dialysis.start.presentation.screen.StartScreen
import com.math3249.dialysis.ui.util.viewModelFactory

@Composable
fun Navigation(
    signIn: @Composable (NavHostController) -> Unit,
    signOut: (NavHostController) -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.SignInScreen.route
    ) {
        composable(route = Screen.SignInScreen.route) {
            signIn(navController)
        }

        composable(route = Screen.StartScreen.route) {
            StartScreen(
                navController = navController,
                onSignOutAction = { signOut(navController) }
            )
        }

        navigation(
            startDestination = Screen.MedicationOverview.route,
            route = Screen.MedicationScreen.route
        ) {
            composable(route = Screen.MedicationOverview.route) {
                val viewModel = it.sharedViewModel<MedicationViewModel>(
                    navController = navController,
                    factory = viewModelFactory {
                        MedicationViewModel(
                            repository = BaseApp.medicineModule.medicineRepository,
                            sessionCache = BaseApp.sessionCache
                        )
                    }
                )
                MedicationListScreen(
                    navController = navController,
                    viewModel = viewModel,
                    onSignOut = {
                        signOut(navController)
                    }
                )
            }
            composable(route = Screen.MedicationSaveScreen.route + "?medicationKey={medicationKey}",
                arguments = listOf(
                    navArgument("medicationKey") {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ) {
                val viewModel = it.sharedViewModel<MedicationViewModel>(navController = navController)
                MedicationScreen(
                    navController = navController,
                    key = it.arguments?.getString("medicationKey"),
                    title = if (it.arguments?.getString("medicationKey") != null) {
                                "Update medication"
                            } else {
                                "New medication"
                            },
                    viewModel = viewModel
                )
            }
        }

        navigation(
            startDestination = Screen.DialysisOverviewScreen.route,
            route = Screen.DialysisScreen.route
        ) {
            composable(route = Screen.DialysisOverviewScreen.route) {
                val viewModel = it.sharedViewModel<DialysisViewModel>(
                    navController = navController,
                    factory = viewModelFactory {
                        DialysisViewModel(
                            repository = BaseApp.dialysisModule.dialysisRepository,
                            sessionCache = BaseApp.sessionCache)
                    }
                )
                DialysisScreen(
                    viewModel = viewModel,
                    onSignOut = { signOut(navController) },
                    modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
    factory: ViewModelProvider.Factory? = null
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return if (factory == null)
            viewModel(parentEntry)
    else
        viewModel(
        factory = factory,
        viewModelStoreOwner = parentEntry
    )
}