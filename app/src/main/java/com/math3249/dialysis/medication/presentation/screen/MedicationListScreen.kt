package com.math3249.dialysis.medication.presentation.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.math3249.dialysis.R
import com.math3249.dialysis.medication.data.Medication
import com.math3249.dialysis.medication.domain.MedicationEvent
import com.math3249.dialysis.medication.presentation.MedicationUiState
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.DialysisDropDownMenu
import com.math3249.dialysis.ui.components.model.MenuItemData
import com.math3249.dialysis.ui.swipe.SwipeBothDirectionContainer

@Composable
fun MedicationListScreen(
    state: MedicationUiState,
    onEvent: (MedicationEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DialysisAppBar(
            canNavigateBack = true,
            navigateUp = {
                onEvent(MedicationEvent.Back)
            },
            title = stringResource(R.string.medications),
            menu = { expanded ->
                IconButton(onClick = { expanded.value = true }) {
                    Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
                }
                MedicationsMenu(
                    expanded = expanded.collectAsStateWithLifecycle().value,
                    onDismiss = {
                        expanded.value = false
                    },
                    onEvent = onEvent,
                    navigateSettings = {}
                )
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
        ) {
            items(state.pausedMedications) { medication ->
                SwipeBothDirectionContainer(
                    item = medication,
                    onEndToStart = {
                        onEvent(MedicationEvent.RemoveMedication(it.key))
                    },
                    onStartToEnd = {
                        Log.i("MedicationList", "Medication Paused")
                        onEvent(MedicationEvent.Pause(it))
                    }
                ) {
                    MedicationCard(
                        data = medication,
                        onEditAction = {
                            onEvent(MedicationEvent.Edit(medication.key))
                        }
                    )
                }
            }
        }
        Row (
            modifier = Modifier
        ) {
//            FloatingActionButton(
//                onClick = { expanded.value = true },
//                shape = CircleShape,
//                modifier = Modifier
//                    .padding(top = 20.dp, end = 15.dp, bottom = 15.dp)
//            ) {
//                Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
//                MedicationsMenu(
//                    expanded = expanded.collectAsStateWithLifecycle().value,
//                    onDismiss = {
//                        expanded.value = false
//                    },
//                    onSignOut = onSignOut,
//                    onAdd = {
//                        navController.navigate(Screen.MedicationSaveScreen.route)
//                    },
//                    navigateSettings = {}
//                )
//            }
        }
    }
}

@Composable
fun MedicationCard(
    data: Medication,
    onEditAction: () -> Unit = {}
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 5.dp, top = 2.dp, end = 5.dp)
            .clickable {
                onEditAction()
            }
    ){
        Row (
        ){
            Box (
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(10.dp)
                    .fillMaxWidth()
            ){
                Row {
                    Text(
                        text = data.time,
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                    Text(
                        text = data.name,
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                    Text(
                        text = data.dose,
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                    Text(
                        text = data.strength,
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                }

            }
        }
    }
}

@Composable
private fun MedicationsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEvent: (MedicationEvent) -> Unit,
    navigateSettings: () -> Unit
) {
    val menuItems = listOf(
        MenuItemData(
            title = stringResource(R.string.add),
            icon = Icons.Outlined.Add,
            onClick = {onEvent(MedicationEvent.Add)}
        ),
        MenuItemData(
            title = stringResource(R.string.settings),
            icon = Icons.Outlined.Settings,
            onClick = navigateSettings
        ),
        MenuItemData(
            title = stringResource(R.string.logout),
            icon = Icons.Outlined.Logout,
            onClick = {onEvent(MedicationEvent.SignOut)}
        )
    )
    DialysisDropDownMenu(
        expanded = expanded,
        onDismiss = onDismiss,
        menuItems = menuItems
    )
}
@Composable
@Preview
fun MedicationListScreenPreview() {
    val navController = rememberNavController()
    MedicationListScreen(
        state = MedicationUiState(
            medications = mutableListOf(
                Medication(
                    name = "Alvedon"
                )
            )
        ),
        onEvent = {}
    )

}