package com.math3249.dialysis

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.identity.Identity
import com.math3249.dialysis.authentication.GoogleAuthUiClient
import com.math3249.dialysis.authentication.SignInScreen
import com.math3249.dialysis.authentication.SignInViewModel
import com.math3249.dialysis.medication.presentation.MedicationViewModel
import com.math3249.dialysis.medication.presentation.screen.MedicationScreen
import com.math3249.dialysis.navigation.Navigation
import com.math3249.dialysis.navigation.Screen
import com.math3249.dialysis.session.SessionCache
import com.math3249.dialysis.ui.screen.FluidBalanceScreen
import com.math3249.dialysis.ui.theme.MyApplicationTheme
import com.math3249.dialysis.ui.util.viewModelFactory
import com.math3249.dialysis.ui.viewmodel.FluidBalanceViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val tabItems = listOf(
//            TabItem(
//                title = resources.getString(R.string.screen_fluid_balance),
//                selectedIcon = Icons.Filled.WaterDrop,
//                unselectedIcon = Icons.Outlined.WaterDrop
//            ),
//            TabItem(
//                title = resources.getString(R.string.icon_dialysis),
//                selectedIcon = Icons.Filled.MedicalInformation,
//                unselectedIcon = Icons.Outlined.MedicalInformation
//            ),
//            TabItem(
//                title = resources.getString(R.string.icon_medication_list),
//                selectedIcon = Icons.Filled.Vaccines,
//                unselectedIcon = Icons.Outlined.Vaccines
//            )
//
//        )
        setContent {
            MyApplicationTheme {
//                val dialysisViewModel = viewModel<DialysisViewModel>(
//                    factory = viewModelFactory {
//                        DialysisViewModel(BaseApp.appModule.dialysisRepository)
//                    }
//                )
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        signIn = {
                                 SignInNavigation(navController = it)
                        },
                        signOut = {
                            signOut(it)
                        })
//                    val navController = rememberNavController()
//                    NavHost(
//                        navController = navController,
//                        startDestination = Constants.SIGN_IN
//                    ) {
//                        composable(Screen.SignInScreen.route) {
//                            SignInNavigation(navController)
//                        }
//
//                        composable(Constants.TABS) {
//                            val viewModel = viewModel<TabViewModel>()
//                            val selectedTabIndex = viewModel.selectedTabIndex.collectAsStateWithLifecycle().value
//                            val pagerState = rememberPagerState {
//                                tabItems.size
//                            }
//
//                            LaunchedEffect(selectedTabIndex) {
//                                pagerState.animateScrollToPage(selectedTabIndex)
//                            }
//                            LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
//                                if (!pagerState.isScrollInProgress)
//                                    viewModel.setSelectedTabIndex(pagerState.currentPage)
//                            }
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                            ) {
//                                TabRow(
//                                    selectedTabIndex = selectedTabIndex
//                                ) {
//                                    tabItems.forEachIndexed { index, tabItem ->
//                                        Tab(
//                                            selected = index == selectedTabIndex,
//                                            onClick = {
//                                                viewModel.setSelectedTabIndex(index)
//                                            },
//                                            text = {
//                                                Text(tabItem.title)
//                                            },
//                                            icon = {
//                                                Icon(
//                                                    imageVector =
//                                                    if (selectedTabIndex == index)
//                                                        tabItem.selectedIcon
//                                                    else
//                                                        tabItem.unselectedIcon,
//                                                    contentDescription = tabItem.title
//                                                )
//                                            })
//                                    }
//                                }
//                                HorizontalPager(
//                                    state = pagerState,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .weight(1f)
//                                ) {index ->
//                                    when (index) {
//                                        0 -> {
//                                            FluidBalanceScreen(navController)
//                                        }
//                                        1 -> {
//                                            DialysisScreen(
//                                                viewModel = viewModel<DialysisViewModel>(
//                                                    factory = viewModelFactory {
//                                                        DialysisViewModel(BaseApp.dialysisModule.dialysisRepository)
//                                                    }
//                                                ),
//                                                onSignOut = {
//                                                    signOut(navController)
//                                                },
////                                                navController = navController,
//                                                modifier = Modifier.fillMaxWidth()
//                                            )
//                                        }
//                                        2 -> {
//                                            MedicationListScreen(
//                                                navController = navController,
//                                                viewModel = viewModel<MedicationViewModel>(
//                                                    factory = viewModelFactory {
//                                                        MedicationViewModel(BaseApp.medicineModule.medicineRepository)
//                                                    }
//                                                ),
//                                                onSignOut = { signOut(navController) }
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        composable(
//                            route = Screen.AddMedicationScreen.route + "?medicationKey={medicationKey}",
//                            arguments = listOf(
//                                navArgument("medicationKey") {
//                                    type = NavType.StringType
//                                    nullable = true
//                                }
//                            )
//                        ) {
//                            AddMedicationScreen(
//                                navController,
//                                key = it.arguments?.getString("medicationKey")
//                            )
//                        }
//                    }
                }
            }
        }
    }
    @Composable
    private fun FluidBalanceScreen(navController: NavHostController) {
        val fluidBalanceViewModel = viewModel<FluidBalanceViewModel>(
            factory = viewModelFactory {
                FluidBalanceViewModel(
                    repository = BaseApp.fluidBalanceModule.fluidBalanceRepository,
                    sessionCache = BaseApp.sessionCache
                    )
            }
        )
        FluidBalanceScreen(
            viewModel = fluidBalanceViewModel,
            onSignOut = { signOut(navController) },
            navigateSettings = {}
        )
    }

    @Composable
    private fun AddMedicationScreen(
        navController: NavHostController,
        key: String? = null
    ) {
        MedicationScreen(
            key = key,
            navController = navController,
//            onNavigateBack = {
//                navController.navigateUp()
//            },
//            cancelToast = {
//                Toast.makeText(applicationContext, getString(R.string.cancelled_add_medication), Toast.LENGTH_LONG).show()
//                          },
//            confirmationToast = {
//                Toast.makeText(applicationContext, getString(R.string.new_medication_added, it), Toast.LENGTH_LONG).show()
//            },
            title = "Add medication",
            viewModel = viewModel<MedicationViewModel>(
                factory = viewModelFactory {
                    MedicationViewModel(
                        repository =  BaseApp.medicineModule.medicineRepository,
                        sessionCache = BaseApp.sessionCache
                    )
                }
            )
        )
    }

    @Composable
    fun SignInNavigation(navController: NavHostController) {
        val viewModel = viewModel<SignInViewModel>(
            factory = viewModelFactory {
                SignInViewModel(
                    sessionCache = BaseApp.sessionCache,
                    repository = BaseApp.sessionDataModule.sessionDataRepository
                )
            }
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = Unit) {
            if (googleAuthUiClient.getSignedInUser() != null) {
                navController.navigate(Screen.StartScreen.route)
            }
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if (result.resultCode == RESULT_OK) {
                    lifecycleScope.launch {
                        val signInResult = googleAuthUiClient.signInWithIntent(
                            intent = result.data ?: return@launch
                        )
                        viewModel.onSignInResult(signInResult)
                    }
                }
            }
        )

        LaunchedEffect(key1 = state.isSignInSuccessful) {
            if (state.isSignInSuccessful) {
                viewModel.createSession(googleAuthUiClient)
                navController.navigate(Screen.StartScreen.route)
                viewModel.resetState()

            }
        }
        SignInScreen(
            state = state,
            onSignInClick = {
                lifecycleScope.launch {
                    val signInIntentSender = googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            }
        )
    }

    private fun signOut(navController: NavHostController) {
        lifecycleScope.launch {
            googleAuthUiClient.signOut()
            SessionCache(getPreferences(Context.MODE_PRIVATE)).clearSession()
            navController.navigate(Screen.SignInScreen.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    }
}