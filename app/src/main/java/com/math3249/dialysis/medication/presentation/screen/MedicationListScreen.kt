package com.math3249.dialysis.medication.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.math3249.dialysis.R
import com.math3249.dialysis.medication.data.Medication
import com.math3249.dialysis.medication.domain.MedicationEvent
import com.math3249.dialysis.medication.presentation.MedicationUiState
import com.math3249.dialysis.navigation.NavigateEvent
import com.math3249.dialysis.navigation.Screen
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.ui.components.CategoryHeader
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.DialysisDropDownMenu
import com.math3249.dialysis.ui.components.model.MenuItemData
import com.math3249.dialysis.util.DateTimeHelper
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MedicationListScreen(
    state: MedicationUiState,
    onEvent: (MedicationEvent) -> Unit,
    onNavigate: (NavigateEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DialysisAppBar(
            canNavigateBack = true,
            navigateUp = {
                onNavigate(NavigateEvent.ToPrevious(Screen.StartScreen.route))
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
                    onNavigate = onNavigate
                )
            }
        )
        LazyColumn() {
            state.medications.forEach { category ->
                stickyHeader {
                    CategoryHeader( if (DateTimeHelper.localTimeOfSecondsOfDayOrNull(category.nameAsInt) == null){
                        stringResource(category.nameAsInt)
                    } else {
                        DateTimeFormatter
                            .ofPattern(Constants.TIME_24_H)
                            .format(DateTimeHelper.localTimeOfSecondsOfDayOrNull(category.nameAsInt))
                    })
                }
                items(category.items) { item ->
                    MedicationCard(
                        data = item,
                        onEvent = onEvent,
                        onNavigate = onNavigate
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MedicationCard(
    data: Medication,
    onEvent: (MedicationEvent) -> Unit,
    onNavigate: (NavigateEvent) -> Unit
) {
    val context = LocalContext.current
    val pauseMessage = stringResource(R.string.pause_message, data.name)
    Row  (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    onEvent(MedicationEvent.Completed(data))
                },
                onLongClick = {
                    onEvent(MedicationEvent.Edit(data))
                    onNavigate(NavigateEvent.ToMedicationSaveScreen)
                },
                onDoubleClick = {
                    Toast
                        .makeText(
                            context,
                            pauseMessage,
                            Toast.LENGTH_LONG
                        )
                        .show()
//                    onEvent(MedicationEvent.TogglePause(data))
                }
            )
            .height(50.dp)
            .fillMaxWidth()
    ){
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
            text = data.unit,
            modifier = Modifier
                .padding(start = 10.dp)
        )
//        Box (
//            modifier = Modifier
//                .background(
//                    MaterialTheme.colorScheme.background,
//                )
//                .shadow(2.dp)
////                    .padding(10.dp)
//                .fillMaxWidth()
//        ){
//            Row {
//
//            }
//
//        }
    }
    Divider(
        thickness = 1.dp,
        color = Color.LightGray,
        modifier = Modifier
            .padding(horizontal = 15.dp)
    )
//    }
}

@Composable
fun MedicationsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEvent: (MedicationEvent) -> Unit,
    onNavigate: (NavigateEvent) -> Unit
) {
    val menuItems = listOf(
        MenuItemData(
            title = stringResource(R.string.add),
            icon = Icons.Outlined.Add,
            onClick = {
                onEvent(MedicationEvent.Add)
                onNavigate(NavigateEvent.ToMedicationSaveScreen)
            }
        ),
        MenuItemData(
            title = stringResource(R.string.paused),
            icon = Icons.Outlined.Pause,
            onClick = {onNavigate(NavigateEvent.ToPausedMedication)}
        ),
        MenuItemData(
            title = stringResource(R.string.settings),
            icon = Icons.Outlined.Settings,
            onClick = { /*TODO Create settings page and add navigation to it*/ }
        ),
        MenuItemData(
            title = stringResource(R.string.logout),
            icon = Icons.Outlined.Logout,
            onClick = {onNavigate(NavigateEvent.ToSignIn)}
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
//    MedicationListScreen(
//        state = MedicationUiState(
//            medications = mutableListOf(
//                Medication(
//                    name = "Alvedon"
//                )
//            )
//        ),
//        onEvent = {},
//        onNavigate = {}
//    )

}