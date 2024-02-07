package com.math3249.dialysis

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.math3249.dialysis.ui.authentication.GoogleAuthUiClient
import com.math3249.dialysis.ui.authentication.SignInScreen
import com.math3249.dialysis.ui.authentication.SignInViewModel
import com.math3249.dialysis.ui.components.DialysisScreen
import com.math3249.dialysis.ui.screen.DialysisScreen
import com.math3249.dialysis.ui.screen.MedicationListScreen
import com.math3249.dialysis.ui.theme.MyApplicationTheme
import com.math3249.dialysis.ui.util.viewModelFactory
import com.math3249.dialysis.ui.viewmodel.DialysisViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    NavHost(navController = navController, startDestination = "sign_in") {
                        composable("sign_in") {
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            
                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthUiClient.getSignedInUser() != null) {
                                    navController.navigate("dialysis")
                                }
                            }
                            
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = {result ->
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
                                    Toast.makeText(
                                        applicationContext,
                                        "Woooooo",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("dialysis")
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

                        composable("dialysis") {
                            DialysisScreen(
                                viewModel = viewModel<DialysisViewModel>(
                                    factory = viewModelFactory {
                                        DialysisViewModel(BaseApp.appModule.dialysisRepository)
                                    }),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.popBackStack()
                                    }
                                },
                                navController = navController,
                                modifier = Modifier.fillMaxWidth())
                        }

                        composable("medication_list") {
                            MedicationListScreen(
                                navController = navController,
                                title = DialysisScreen.MedicationList.name,
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.popBackStack()

                                    }
                                }
                            )
                        }
                    }

//                    screen.DialysisList(
//                        modifier = Modifier.fillMaxWidth()
//                    )
                }
            }
        }
    }
}