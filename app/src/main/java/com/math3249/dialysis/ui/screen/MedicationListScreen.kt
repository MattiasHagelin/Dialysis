package com.math3249.dialysis.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.DialysisScreen

@Composable
fun MedicationListScreen(
    navController: NavHostController,
    title: String,
    onSignOut: () -> Unit,

) {
    Scaffold (
        topBar = {
            DialysisAppBar(
                navController = navController,
                canNavigateBack = false,
                navigateUp = { /*TODO*/ },
                title = title,
                onSignOut = onSignOut,
                currentLocation = DialysisScreen.MedicationList
            )
        }
    ){ padding ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(padding)
        ) {
            Text("Hello World")
        }
    }
}

@Composable
@Preview
fun MedicationListScreenPreview() {
//    MedicationListScreen()
}