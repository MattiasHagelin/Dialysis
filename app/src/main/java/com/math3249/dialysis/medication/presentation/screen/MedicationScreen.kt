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
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.math3249.dialysis.R
import com.math3249.dialysis.medication.domain.MedicationEvent
import com.math3249.dialysis.medication.presentation.MedicationUiState
import com.math3249.dialysis.navigation.NavigateEvent
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.LabeledCheckbox
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun MedicationScreen(
    state: MedicationUiState,
    onEvent: (MedicationEvent) -> Unit,
    onNavigateEvent: (NavigateEvent) -> Unit = {}
) {
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()
    Column (
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        DialysisAppBar(
            canNavigateBack = true,
            navigateUp = {
                onEvent(MedicationEvent.Clear)
                onNavigateEvent(NavigateEvent.ToPrevious)

            },
            title = if (state.medicationKey != "") {
                stringResource(R.string.update_medication)
            } else {
                stringResource(R.string.add_medication)
            },
            saveAction = {
                IconButton(onClick = {
                    if (state.medicationKey != "")
                        onEvent(MedicationEvent.UpdateMedication)
                    else
                        onEvent(MedicationEvent.CreateMedication)
                    onNavigateEvent(NavigateEvent.ToPrevious)
                }) {
                    Icon(imageVector = Icons.Outlined.Save, contentDescription = null)
                }

            }
        )
        Row {
            OutlinedTextField(
                value = state.name,
                onValueChange = {
                    onEvent(MedicationEvent.UpdateName(it))
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
                    onEvent(MedicationEvent.UpdateDose(it))
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
//            TODO fix the SelectText
//            SelectTextField(
//                value = state.selectedValue,
//                label = "Unit",
//                onValueChange = {},
//                onDismissRequest = {
//                    onEvent(MedicationEvent.UpdateExpanded(false))
//               },
//                items = listOf {
//                    DropdownMenuItem(
//                        text = {
//                            Text("ml") },
//                        onClick = {
//                            onEvent(MedicationEvent.UpdateSelectedValue("ml"))
//                            onEvent(MedicationEvent.UpdateExpanded(false))
//                        }
//                    )
//                    DropdownMenuItem(
//                        text = {
//                            Text("g") },
//                        onClick = {
//                            onEvent(MedicationEvent.UpdateSelectedValue("g"))
//                            onEvent(MedicationEvent.UpdateExpanded(false))
//                        }
//                    )
//                    DropdownMenuItem(
//                        text = {
//                            Text("tablett") },
//                        onClick = {
//                            onEvent(MedicationEvent.UpdateSelectedValue("tablett"))
//                            onEvent(MedicationEvent.UpdateExpanded(false))
//                        }
//                    )
//                },
//                modifier = Modifier
//                    .padding(start = 5.dp, end = 5.dp)
//                    .weight(1f)
//            )

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
                    onEvent(MedicationEvent.UpdateInterval(it))
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
                    onEvent(MedicationEvent.UpdatePaused(it))
                }
            )
        }
        Row {
            LabeledCheckbox(
                label = "Preparation needed",
                state = state.needPrep,
                onStateChange = {
                    onEvent(MedicationEvent.UpdateNeedPrep(it))
                }
            )
        }
        if (state.needPrep) {
            Row {
                OutlinedTextField(
                    value = state.prepDescription,
                    onValueChange = {
                        onEvent(MedicationEvent.UpdatePrepDescription(it))
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
            onEvent(MedicationEvent.UpdateStartDate(it))
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
            onEvent(MedicationEvent.UpdateTime(it))
        }
    }
}

@Composable
@Preview
fun MedicationScreenPreview(){
    val navController = rememberNavController()
    MedicationScreen(
        state = MedicationUiState(
            name = "Hello World",
            time = LocalTime.MIDNIGHT,
            interval = "100y"
        ),
        onEvent = {}
    )
}