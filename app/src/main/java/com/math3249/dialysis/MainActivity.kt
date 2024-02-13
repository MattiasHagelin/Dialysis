package com.math3249.dialysis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.MedicalInformation
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.ui.authentication.GoogleAuthUiClient
import com.math3249.dialysis.ui.authentication.SignInScreen
import com.math3249.dialysis.ui.authentication.SignInViewModel
import com.math3249.dialysis.ui.components.model.TabItem
import com.math3249.dialysis.ui.screen.DialysisScreen
import com.math3249.dialysis.ui.screen.FluidBalanceScreen
import com.math3249.dialysis.ui.screen.MedicationListScreen
import com.math3249.dialysis.ui.theme.MyApplicationTheme
import com.math3249.dialysis.ui.util.viewModelFactory
import com.math3249.dialysis.ui.viewmodel.DialysisViewModel
import com.math3249.dialysis.ui.viewmodel.FluidBalanceViewModel
import com.math3249.dialysis.ui.viewmodel.TabViewModel
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
        val tabItems = listOf(
            TabItem(
                title = resources.getString(R.string.screen_fluid_balance),
                selectedIcon = Icons.Filled.WaterDrop,
                unselectedIcon = Icons.Outlined.WaterDrop
            ),
            TabItem(
                title = resources.getString(R.string.icon_dialysis),
                selectedIcon = Icons.Filled.MedicalInformation,
                unselectedIcon = Icons.Outlined.MedicalInformation
            ),
            TabItem(
                title = resources.getString(R.string.icon_medication_list),
                selectedIcon = Icons.Filled.Vaccines,
                unselectedIcon = Icons.Outlined.Vaccines
            )

        )
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
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Constants.SIGN_IN
                    ) {
                        composable(Constants.SIGN_IN) {
                            SignInNavigation(navController)
                        }

                        composable(Constants.TABS) {
                            val viewModel = viewModel<TabViewModel>()
                            val selectedTabIndex = viewModel.selectedTabIndex.collectAsStateWithLifecycle().value
                            val pagerState = rememberPagerState {
                                tabItems.size
                            }
                            
                            LaunchedEffect(selectedTabIndex) {
                                pagerState.animateScrollToPage(selectedTabIndex)
                            }
                            LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
                                if (!pagerState.isScrollInProgress)
                                    viewModel.setSelectedTabIndex(pagerState.currentPage)
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                TabRow(
                                    selectedTabIndex = selectedTabIndex
                                ) {
                                    tabItems.forEachIndexed { index, tabItem ->
                                        Tab(
                                            selected = index == selectedTabIndex,
                                            onClick = {
                                                viewModel.setSelectedTabIndex(index)
                                            },
                                            text = {
                                                Text(tabItem.title)
                                            },
                                            icon = {
                                                Icon(
                                                    imageVector =
                                                    if (selectedTabIndex == index)
                                                        tabItem.selectedIcon
                                                    else
                                                        tabItem.unselectedIcon,
                                                    contentDescription = tabItem.title
                                                )
                                            })
                                    }
                                }
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {index ->
                                    when (index) {
                                        0 -> {
                                            FluidBalanceScreen(navController)
                                        }
                                        1 -> {
                                            DialysisScreen(
                                                viewModel = viewModel<DialysisViewModel>(
                                                    factory = viewModelFactory {
                                                        DialysisViewModel(BaseApp.dialysisModule.dialysisRepository)
                                                    }
                                                ),
                                                onSignOut = {
                                                    signOut(navController)
                                                },
                                                navController = navController,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                        2 -> {
                                            MedicationListScreen(
                                                navController = navController,
                                                title = stringResource(R.string.icon_medication_list),
                                                onSignOut = {
                                                    signOut(navController)
                                                },
                                                navigateUp = { navController.popBackStack() },
                                                canNavigateBack = true
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        //DialysisUi()
                    }
                }
            }
        }
    }

    @Composable
    private fun DialysisUi() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Constants.SIGN_IN
        ) {
            composable(Constants.SIGN_IN) {
                SignInNavigation(navController)
            }

            composable(Constants.DIALYSIS) {
                DialysisScreen(
                    viewModel = viewModel<DialysisViewModel>(
                        factory = viewModelFactory {
                            DialysisViewModel(BaseApp.dialysisModule.dialysisRepository)
                        }
                    ),
                    onSignOut = {
                        signOut(navController)
                    },
                    navController = navController,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            composable(Constants.MEDICATION_LIST) {
                MedicationListScreen(
                    navController = navController,
                    title = stringResource(R.string.icon_medication_list),
                    onSignOut = {
                        signOut(navController)
                    },
                    navigateUp = { navController.popBackStack() },
                    canNavigateBack = true
                )
            }

            composable(Constants.FLUID_BALANCE) {
                FluidBalanceScreen(navController)
            }

            composable(Constants.SETTINGS) {

            }
        }
    }

    @Composable
    private fun FluidBalanceScreen(navController: NavHostController) {
        val fluidBalanceViewModel = viewModel<FluidBalanceViewModel>(
            factory = viewModelFactory {
                FluidBalanceViewModel(BaseApp.fluidBalanceModule.fluidBalanceRepository)
            }
        )
        FluidBalanceScreen(
            viewModel = fluidBalanceViewModel,
            onSignOut = { signOut(navController) },
            navigateSettings = {}
        )
//        LaunchedEffect(Unit) {
//            fluidBalanceViewModel.focusRequester.value.requestFocus()
//        }
    }

    @Composable
    private fun SignInNavigation(navController: NavHostController) {
        val viewModel = viewModel<SignInViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = Unit) {
            if (googleAuthUiClient.getSignedInUser() != null) {
                BaseApp.userData = googleAuthUiClient.getSignedInUser()!!
                navController.navigate(Constants.TABS)
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
                if (googleAuthUiClient.getSignedInUser() != null)
                    BaseApp.userData = googleAuthUiClient.getSignedInUser()!!
                navController.navigate(Constants.TABS)
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
            navController.navigate(Constants.SIGN_IN) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    }
}