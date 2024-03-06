package com.math3249.dialysis.medication.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.math3249.dialysis.medication.presentation.MedicationViewModel
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.LabeledCheckbox
import com.math3249.dialysis.ui.components.SelectTextField
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun MedicationScreen(
    key: String? = null,
    navController: NavController,
//    onNavigateBack: () -> Unit,
//    confirmationToast: (String) -> Unit,
//    cancelToast: () -> Unit,
    title: String,
    viewModel: MedicationViewModel
) {
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()
    Column (
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (key != null) {
            viewModel.getMedication(key)
        }
        val state = viewModel.state.collectAsStateWithLifecycle().value
        DialysisAppBar(
            canNavigateBack = true,
            navigateUp = {
                navController.navigateUp()
//                onNavigateBack()
//                cancelToast()
            },
            canSignOut = false,
            showMenu = false,
            title = title,
            onSignOut = { /*TODO*/ },
            currentLocation = "here",
            canSave = true,
            onSaveAction = {
                if (key != null)
                    viewModel.updateMedication(key)
                else
                    viewModel.saveMedication()
                navController.navigateUp()
            }
        )
        Row {
            OutlinedTextField(
                value = state.name,
                onValueChange = {
                    viewModel.updateName(it)
                },
                singleLine = true,
                label = {
                    Text(text = "Name")
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .weight(1f)
            )
        }
        Row {
            OutlinedTextField(
                value = state.dose,
                onValueChange = {
                    viewModel.updateDose(it)
                },
                singleLine = true,
                label = {
                    Text(text = "Dose")
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .weight(2f)
            )
            SelectTextField(
                value = state.selectedValue,
                label = "Unit",
                expanded = state.expanded,
                onExpandedChange = {
                    viewModel.updateExpanded(it)
                },
                onValueChange = {},
                onDismissRequest = { viewModel.updateExpanded(false) },
                items = listOf {
                    DropdownMenuItem(
                        text = {
                            Text("ml") },
                        onClick = {
                            viewModel.updateSelectedValue("ml")
                            viewModel.updateExpanded(false)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text("g") },
                        onClick = {
                            viewModel.updateSelectedValue("g")
                            viewModel.updateExpanded(false)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text("tablett") },
                        onClick = {
                            viewModel.updateSelectedValue("tablett")
                            viewModel.updateExpanded(false)
                        }
                    )
                },
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .weight(1f)
            )

        }
        Row {
            OutlinedTextField(
                value = DateTimeFormatter
                    .ofPattern(Constants.DATE_PATTERN)
                    .format(state.startDate),
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                label = {
                    Text(text = "Start date")
                },
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = "Calendar",
                        modifier = Modifier
                            .clickable {
                                dateDialogState.show()
                            }
                    )
                },
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .weight(2f)
            )
            OutlinedTextField(
                value = DateTimeFormatter
                    .ofPattern(Constants.TIME_24_H)
                    .format(state.time),
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                label = {
                    Text(text = "Time")
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = "Time",
                        modifier = Modifier
                            .clickable {
                                timeDialogState.show()
                            }
                    )
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .weight(1f)
            )
        }
        Row {
            OutlinedTextField(
                value = state.interval,
                onValueChange = {
                    viewModel.updateInterval(it)
                },
                singleLine = true,
                label = {
                    Text(text = "Interval")
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .weight(1f)
            )
        }
        Row {
            LabeledCheckbox(
                label = "Paused",
                state = state.paused,
                onStateChange = {
                    viewModel.updatePaused(it)
                }
            )
        }
        Row {
            LabeledCheckbox(
                label = "Preparation needed",
                state = state.needPrep,
                onStateChange = {
                    viewModel.updateNeedPrep(it)
                }
            )
        }
        if (state.needPrep) {
            Row {
                OutlinedTextField(
                    value = state.prepDescription,
                    onValueChange = {
                        viewModel.updatePrepDesc(it)
                    },
                    label = {
                        Text(text = "Preparation Description")
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp)
                        .weight(1f)
                )
            }
        }
    }

    MaterialDialog (
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok")
            negativeButton(text = "Cancel")
        }
    ){
        this.datepicker(
            initialDate = LocalDate.now(),
            title = "Start date",
        ) {
            viewModel.updateStartDate(it)
        }
    }

    MaterialDialog (
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "Ok")
            negativeButton(text = "Cancel")
        }
    ){
        this.timepicker(
            initialTime = LocalTime.now(),
            title = "Time",
            is24HourClock = true

        ){
            viewModel.updateTime(it)
        }
    }
}

@Composable
@Preview
fun MedicationScreenPreview(){
//    MedicationScreen(
//        onNavigateBack = {},
//        onConfirmAction = {},
//        title = "Title",
////        viewModel = null
//    )
}