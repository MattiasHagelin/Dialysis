package com.math3249.dialysis.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.math3249.dialysis.R
import com.math3249.dialysis.ui.authentication.GoogleAuthUiClient
import kotlinx.coroutines.flow.MutableStateFlow

enum class DialysisScreen {
    SignIn,
    Dialysis,
    MedicationList,
    FluidBalance,
    Settings
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialysisAppBar(
    navController: NavHostController,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    title: String,
    onSignOut: () -> Unit,
    currentLocation: DialysisScreen,
    modifier: Modifier = Modifier
){
    val expanded = MutableStateFlow(false)

    TopAppBar(
        title = {
            Text(title)
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = navigateUp
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.icon_arrow_back)
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = {
                    expanded.value = true
                }) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = stringResource(R.string.icon_menu)
                )
                DialysisDropdownMenu(
                    currentLocation = currentLocation,
                    navController = navController,
                    expanded
                )
            }

            IconButton(
                onClick = onSignOut
            ) {
                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = stringResource(R.string.icon_sign_out)
                )
            }
        }
    )
}

@Composable
private fun DialysisDropdownMenu(
    currentLocation: DialysisScreen,
    navController: NavHostController,
    expanded: MutableStateFlow<Boolean>
) {
    DropdownMenu(
        expanded = expanded.collectAsState().value,
        onDismissRequest = {

        }) {
        enumValues<DialysisScreen>().forEach {
            if (it != currentLocation &&
                it != DialysisScreen.SignIn) {
                DropdownMenuItem(
                    text = {
                        Box (

                        ){
                            Text(
                                text = it.name,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(5.dp)
                            )
                            Spacer(modifier = Modifier.size(15.dp))
                            Icon(
                                imageVector = Icons.Outlined.Vaccines,
                                contentDescription = stringResource(R.string.icon_medication_list),
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(5.dp)
                            )
                        }
                    },
                    onClick = {
                        navController.navigate("medication_list")
                        expanded.value = false
                    })
            }
        }
    }
}