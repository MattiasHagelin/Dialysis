package com.math3249.dialysis

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.identity.Identity
import com.math3249.dialysis.authentication.GoogleAuthUiClient
import com.math3249.dialysis.navigation.Navigation
import com.math3249.dialysis.navigation.Screen
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.session.SessionCache
import com.math3249.dialysis.ui.theme.MyApplicationTheme
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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        googleAuthUiClient
                    )
                    Log.i("MatH_MainActivity", "Navigation created!")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MatH_MainActivity", "destroyed")
    }



    private fun signOut(navController: NavHostController) {
        lifecycleScope.launch {
            googleAuthUiClient.signOut()
            SessionCache(getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE)).clearSession()
            navController.navigate(Screen.SignInScreen.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    }
}