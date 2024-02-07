package com.math3249.dialysis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.math3249.dialysis.ui.screen.DialysisHomeScreen
import com.math3249.dialysis.ui.theme.MyApplicationTheme
import com.math3249.dialysis.ui.util.viewModelFactory
import com.math3249.dialysis.ui.viewmodel.DialysisViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val dialysisViewModel = viewModel<DialysisViewModel>(
                    factory = viewModelFactory {
                        DialysisViewModel(BaseApp.appModule.dialysisRepository)
                    }
                )
                val screen = DialysisHomeScreen(dialysisViewModel)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    screen.DialysisList(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}