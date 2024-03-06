package com.math3249.dialysis.start.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MedicalInformation
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.math3249.dialysis.R
import com.math3249.dialysis.navigation.Screen

@Composable
fun StartScreen(
    navController: NavHostController,
    onSignOutAction: (NavHostController) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        StartRow(
            onClickAction = { navController.navigate(Screen.FluidBalanceScreen.route) },
            imageVector = Icons.Outlined.WaterDrop,
            text = stringResource(R.string.screen_fluid_balance)
        )
        StartRow(
            imageVector = Icons.Outlined.MedicalInformation,
            text = stringResource(R.string.screen_dialysis),
            onClickAction = { navController.navigate(Screen.DialysisScreen.route)}
        )
        StartRow(
            imageVector = Icons.Outlined.Vaccines,
            text = stringResource(R.string.icon_medication_list),
            onClickAction = {
                navController.navigate(Screen.MedicationScreen.route)
            }
        )
        StartRow(
            painter = painterResource(R.drawable.blood_pressure),
            text = "Blood pressure",
            onClickAction = {navController.navigate("blood_pressure")}
        )
        FloatingActionButton(
            onClick = {
                      onSignOutAction(navController)
            },
        ) {
            Icon(
                imageVector = Icons.Outlined.Logout,
                contentDescription = null)
        }
    }
}

@Composable
fun StartRow(
    onClickAction: () -> Unit = {},
    imageVector: ImageVector? = null,
    painter: Painter? = null,
    text: String = ""
) {
    Row (
        modifier = Modifier
            .clickable {
                onClickAction()
            }
            .fillMaxWidth()
            .background(Color.White)
            .shadow(1.dp)
    ){
        if (imageVector != null) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
            )
        } else if (painter != null){
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(24.dp)
            )
        }
        Text(
            fontSize = 18.sp,
            text = text,
            modifier = Modifier
                .padding(5.dp
                )
        )
    }
}
@Composable
@Preview
fun StartScreenPreview() {
    val navController = rememberNavController()
    StartScreen(navController) {

    }
}