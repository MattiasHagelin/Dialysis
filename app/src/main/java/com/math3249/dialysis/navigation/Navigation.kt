package com.math3249.dialysis.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.math3249.dialysis.BaseApp
import com.math3249.dialysis.authentication.GoogleAuthUiClient
import com.math3249.dialysis.authentication.SignInNavigation
import com.math3249.dialysis.authentication.SignInViewModel
import com.math3249.dialysis.authentication.presentation.screen.FirstTimeSignInScreen
import com.math3249.dialysis.dialysis.presentation.DialysisViewModel
import com.math3249.dialysis.dialysis.presentation.screen.DialysisEntryScreen
import com.math3249.dialysis.dialysis.presentation.screen.DialysisOverviewScreen
import com.math3249.dialysis.fluidbalance.presentation.FluidBalanceViewModel
import com.math3249.dialysis.fluidbalance.presentation.screen.FluidBalanceHistoryScreen
import com.math3249.dialysis.fluidbalance.presentation.screen.FluidBalanceScreen
import com.math3249.dialysis.fluidbalance.presentation.screen.UpdateFluidBalanceLimitScreen
import com.math3249.dialysis.medication.presentation.MedicationViewModel
import com.math3249.dialysis.medication.presentation.screen.MedicationListScreen
import com.math3249.dialysis.medication.presentation.screen.MedicationScreen
import com.math3249.dialysis.medication.presentation.screen.PausedMedications
import com.math3249.dialysis.start.presentation.StartViewModel
import com.math3249.dialysis.start.presentation.screen.StartScreen
import com.math3249.dialysis.ui.util.viewModelFactory

@Composable
fun Navigation(
    googleAuthUiClient: GoogleAuthUiClient
) {
    val navController = rememberNavController()
    Log.i("MatH_Navigation", "NavController created")
    val navigator = Navigator(navController, googleAuthUiClient)
    NavHost(
        navController = navController,
        startDestination = Screen.AppStartScreen.route
    ) {
        navigation (
            startDestination = Screen.SignInScreen.route,
            route = Screen.AppStartScreen.route
        ) {
            composable(route = Screen.SignInScreen.route) {
                val viewModel = it.sharedViewModel<SignInViewModel>(
                    navController = navController,
                    factory = viewModelFactory {
                        SignInViewModel(
                            sessionCache = BaseApp.sessionCache,
                            repository = BaseApp.sessionDataModule.sessionDataRepository,
                            onNavigateEvent = navigator::onNavigateEvent
                        )
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()
                SignInNavigation(
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }

            composable(Screen.FirstTimeSignInScreen.route) {
                val viewModel = it.sharedViewModel<SignInViewModel>(navController = navController)
                val state by viewModel.state.collectAsStateWithLifecycle()
                FirstTimeSignInScreen(
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }

            composable(route = Screen.StartScreen.route) {
                val viewModel = viewModel<StartViewModel>(
                    factory = viewModelFactory {
                        StartViewModel(navigator::onNavigateEvent)
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()
                StartScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigation = navigator::onNavigateEvent
                )
            }
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
                            repository = BaseApp.medicineModule.medicineRepository
                        )
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()
                MedicationListScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = navigator::onNavigateEvent
                )
            }
            composable(route = Screen.MedicationSaveScreen.route) {
                val viewModel = it.sharedViewModel<MedicationViewModel>(navController = navController)
                val state by viewModel.state.collectAsStateWithLifecycle()
                MedicationScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigateEvent = navigator::onNavigateEvent
                )
            }
            composable(route = Screen.MedicationPaused.route) {
                val viewModel = it.sharedViewModel<MedicationViewModel>(navController = navController)
                val state by viewModel.state.collectAsStateWithLifecycle()
                PausedMedications(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = navigator::onNavigateEvent
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
                            sessionCache = BaseApp.sessionCache
                        )
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()
                DialysisOverviewScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = navigator::onNavigateEvent,
                    modifier = Modifier.fillMaxSize())
            }
            composable(route = Screen.DialysisEntryScreen.route) {
                val viewModel = it.sharedViewModel<DialysisViewModel>(navController = navController)
                val state by viewModel.state.collectAsStateWithLifecycle()
                DialysisEntryScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = navigator::onNavigateEvent
                )
            }
        }

        navigation(
            startDestination = Screen.FluidBalanceDayScreen.route,
            route = Screen.FluidBalanceScreen.route
        ) {
            composable(route = Screen.FluidBalanceDayScreen.route) {
                val viewModel = it.sharedViewModel<FluidBalanceViewModel>(
                    navController = navController,
                    factory = viewModelFactory {
                        FluidBalanceViewModel(
                            repository = BaseApp.fluidBalanceModule.fluidBalanceRepository,
                            sessionCache = BaseApp.sessionCache
                        )
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()
                FluidBalanceScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = navigator::onNavigateEvent
                )
            }

            composable(route = Screen.FluidBalanceUpdateFluidLimit.route) {
                val viewModel = it.sharedViewModel<FluidBalanceViewModel>(navController = navController)
                val state by viewModel.state.collectAsStateWithLifecycle()

                UpdateFluidBalanceLimitScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = navigator::onNavigateEvent
                )

            }

            composable(route = Screen.FluidBalanceHistoryScreen.route) {
                val viewModel = it.sharedViewModel<FluidBalanceViewModel>(navController = navController)
                val state by viewModel.state.collectAsStateWithLifecycle()

                FluidBalanceHistoryScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = navigator::onNavigateEvent
                )
            }
        }

        navigation(
            startDestination = Screen.BloodPressureOverview.route,
            route = Screen.BloodPressureScreen.route
        ) {
            composable(route = Screen.BloodPressureOverview.route) {
                Button(
                    onClick = {
                        navController!!.navigate(Screen.StartScreen.route) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                ) {

                }
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