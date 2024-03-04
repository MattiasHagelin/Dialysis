package com.math3249.dialysis.medications.presentation.screen

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
import com.math3249.dialysis.medications.presentation.MedicationViewModel
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.LabeledCheckbox
import com.math3249.dialysis.ui.components.SelectTextField
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun MedicationScreen(
     onNavigateBack: () -> Unit,
     confirmationToast: (String) -> Unit,
     cancelToast: () -> Unit,
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
        val name = viewModel.name.collectAsStateWithLifecycle().value
        DialysisAppBar(
            canNavigateBack = true,
            navigateUp = {
                onNavigateBack()
                cancelToast()
            },
            canSignOut = false,
            showMenu = false,
            title = title,
            onSignOut = { /*TODO*/ },
            currentLocation = "here",
            canSave = true,
            onSaveAction = {
//                viewModel.saveMedication()
                confirmationToast(name)
                onNavigateBack()
            }
        )
        Row {
            OutlinedTextField(
                value = name,
                onValueChange = {
                                viewModel.setName(it)
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
                value = viewModel.dose.collectAsStateWithLifecycle().value,
                onValueChange = {
                                viewModel.setDose(it)
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
                value = viewModel.selectedValue.collectAsStateWithLifecycle().value,
                label = "Unit",
                expanded = viewModel.expanded.collectAsStateWithLifecycle().value,
                onExpandedChange = {
                    viewModel.showMenu(it)
                },
                onValueChange = {},
                onDismissRequest = { viewModel.showMenu(false) },
                items = listOf {
                    DropdownMenuItem(
                        text = {
                            Text("ml") },
                        onClick = {
                            viewModel.setSelectedValue("ml")
                            viewModel.showMenu(false)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text("g") },
                        onClick = {
                            viewModel.setSelectedValue("g")
                            viewModel.showMenu(false)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text("tablett") },
                        onClick = {
                            viewModel.setSelectedValue("tablett")
                            viewModel.showMenu(false)
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
                value = viewModel.formatDate(
                    viewModel.startDate
                        .collectAsStateWithLifecycle()
                        .value
                ),
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
                value = viewModel.formatTime(
                    viewModel.time
                        .collectAsStateWithLifecycle()
                        .value
                ),
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
                value = viewModel.interval.collectAsStateWithLifecycle().value,
                onValueChange = {
                    viewModel.setInterval(it)
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
                state = viewModel.paused.collectAsStateWithLifecycle().value,
                onStateChange = {
                    viewModel.setPaused(it)
                }
            )
        }
        val needPrep = viewModel.needPrep.collectAsStateWithLifecycle().value
        Row {
            LabeledCheckbox(
                label = "Preparation needed",
                state = needPrep,
                onStateChange = {
                    viewModel.setNeedPrep(it)
                }
            )
        }
        if (needPrep) {
            Row {
                OutlinedTextField(
                    value = viewModel.prepDesc.collectAsStateWithLifecycle().value,
                    onValueChange = {
                        viewModel.setPrepDesc(it)
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
            viewModel.setStartDate(it)
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
            viewModel.setTime(it)
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