package com.math3249.dialysis.authentication

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

@Composable
fun SignInNavigation(
    state: SignInState = SignInState(),
    onEvent: (SignInEvent) -> Unit = {}
) {
    val context = LocalContext.current
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient (
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        if (googleAuthUiClient.getSignedInUser() != null) {
            onEvent(SignInEvent.LoadRequiredData(googleAuthUiClient))
//            if (state.dataLoadSuccessful) {
//                onNavigateEvent(NavigateEvent.ToStart)
//            } else {
//                onNavigateEvent(NavigateEvent.ToFirstTimeSignIn)
//            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                coroutineScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    onEvent(SignInEvent.OnSignInResult(signInResult))
                }
            }
        }
    )

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            onEvent(SignInEvent.LoadRequiredData(googleAuthUiClient))
//            if (state.dataLoadSuccessful) {
//                onNavigateEvent(NavigateEvent.ToStart)
//            } else {
//                onNavigateEvent(NavigateEvent.ToFirstTimeSignIn)
//            }
        }
            onEvent(SignInEvent.ResetState)
    }

    SignInScreen(
        state = state,
        onSignInClick = {
            coroutineScope.launch {
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