package com.math3249.dialysis.medication.presentation.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.math3249.dialysis.R
import com.math3249.dialysis.medication.data.Medication
import com.math3249.dialysis.medication.domain.MedicationEvent
import com.math3249.dialysis.medication.presentation.MedicationUiState
import com.math3249.dialysis.navigation.NavigateEvent
import com.math3249.dialysis.navigation.Screen
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.ExposedDropdownMenu
import com.math3249.dialysis.ui.components.LabeledCheckbox
import com.math3249.dialysis.ui.components.NumberField
import com.math3249.dialysis.util.textFieldColors
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.listItemsSingleChoice
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
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
    val recurrenceDialogState = rememberMaterialDialogState()
    val updateDialogState = rememberMaterialDialogState()
    val deleteDialogState = rememberMaterialDialogState()

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
                onNavigateEvent(NavigateEvent.ToPrevious(Screen.MedicationOverview.route))

            },
            title = if (state.medication.key != "") {
                stringResource(R.string.update_medication)
            } else {
                stringResource(R.string.add_medication)
            },
            saveAction = {
                if (state.medication.key != "") {
                    val context = LocalContext.current
                    val pauseMessage = stringResource(R.string.pause_message, state.medication.name)
                    IconButton(onClick = { deleteDialogState.show() }) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                    }
                    IconButton(onClick = {
                        Toast
                            .makeText(
                                context,
                                pauseMessage,
                                Toast.LENGTH_LONG
                            )
                            .show()
                        onEvent(MedicationEvent.TogglePause(state.medication))
                        onNavigateEvent(NavigateEvent.ToPrevious(Screen.MedicationOverview.route))
                    }
                    ) {
                        Icon(imageVector = Icons.Outlined.Pause, contentDescription = null)
                    }
                }
                IconButton(onClick = {
                    if (state.medication.key != "")
                        updateDialogState.show()
                    else {
                        onEvent(MedicationEvent.CreateMedication)
                        onNavigateEvent(NavigateEvent.ToPrevious(Screen.MedicationOverview.route))
                    }
                }) {
                    Icon(imageVector = Icons.Outlined.Save, contentDescription = null)
                }

            }
        )
        Row {
            TextField(
                value = state.medication.name,
                onValueChange = {
                    onEvent(MedicationEvent.UpdateMedicationState(state.medication.copy(name = it)))
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = MaterialTheme.colorScheme.background
                ),
                label = {
                    Text(stringResource(R.string.name))
                },
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .weight(1f)
            )
        }
        Row {
            TextField(
                value = state.medication.dose,
                onValueChange = {
                    onEvent(MedicationEvent.UpdateMedicationState(state.medication.copy(dose = it)))
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = MaterialTheme.colorScheme.background
                ),
                label = {
                    Text(stringResource(R.string.dose))
                },
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .weight(0.5f)
            )
            ExposedDropdownMenu(
                value = state.medication.unit,
                label = stringResource(R.string.unit),
                items = listOf {expanded ->
                    stringArrayResource(R.array.dose_units).forEach {item ->
                        DropdownMenuItem(
                            text = {
                                Text(item)
                            },
                            onClick = {
                                onEvent(MedicationEvent
                                    .UpdateMedicationState(
                                        state.medication.copy(unit = item)
                                    )
                                )
                                expanded.value = false
                            }
                        )
                        
                    }
                },
                modifier = Modifier
                    .weight(1f)
            )
        }
        Row {
            TextField(
                value = DateTimeFormatter
                    .ofPattern(Constants.DATE_PATTERN)
                    .format(state.startDate),
                onValueChange = {},
                singleLine = true,
                colors = textFieldColors(),
                readOnly = true,
                enabled = false,
                label = {
                    Text(stringResource(R.string.start_date))
                },
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
                    .padding(horizontal = 15.dp)
                    .weight(1.5f)
                    .clickable {
                        dateDialogState.show()
                    }
            )
            TextField(
                value = state.medication.time,
                onValueChange = {},
                singleLine = true,
                colors = textFieldColors(),
                readOnly = true,
                enabled = false,
                label = {
                    Text(stringResource(R.string.time))
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                timeDialogState.show()
                            }
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .weight(1f)
                    .clickable {
                        timeDialogState.show()
                    }
            )
        }
        Row {
            TextField(
                value = state.medication.recurrence,
                onValueChange = {},
                singleLine = true,
                colors = textFieldColors(),
                readOnly = true,
                enabled = false,
                label = {
                    Text(stringResource(R.string.recurrence))
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Replay,
                        contentDescription = "Calendar",
                        modifier = Modifier
                            .clickable {
                                recurrenceDialogState.show()
                            }
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .weight(1.5f)
                    .clickable {
                        recurrenceDialogState.show()
                    }
            )
        }
        Row {
            NumberField(
                value = state.medication.strength,
                onValueChanged = {
                    onEvent(MedicationEvent.UpdateMedicationState(state.medication.copy(strength = it)))
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = MaterialTheme.colorScheme.background
                ),
                label = {
                    Text(stringResource(R.string.strength))
                },
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .weight(1f)
            )
        }
        Row {
            Column {
                LabeledCheckbox(
                    label = stringResource(R.string.take_with_food),
                    state = state.medication.takeWithFood,
                    onStateChange = {
                        onEvent(MedicationEvent.UpdateMedicationState(state.medication.copy(takeWithFood = it)))
                    },
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                )
                AnimatedVisibility(visible = state.medication.takeWithFood) {
                    Column {
                        LabeledCheckbox(
                            label = stringResource(R.string.breakfast),
                            state = state.medication.withBreakfast,
                            onStateChange = {
                            onEvent(MedicationEvent.UpdateMedicationState(state.medication.copy(withBreakfast = it)))
                            },
                            modifier = Modifier
                                .padding(horizontal = 30.dp)
                        )
                        LabeledCheckbox(
                            label = stringResource(R.string.lunch),
                            state = state.medication.withLunch,
                            onStateChange = {
                            onEvent(MedicationEvent.UpdateMedicationState(state.medication.copy(withLunch = it)))
                            },
                            modifier = Modifier
                                .padding(horizontal = 30.dp)
                        )
                        LabeledCheckbox(
                            label = stringResource(R.string.dinner),
                            state = state.medication.withDinner,
                            onStateChange = {
                            onEvent(MedicationEvent.UpdateMedicationState(state.medication.copy(withDinner = it)))
                            },
                            modifier = Modifier
                                .padding(horizontal = 30.dp)
                        )
                    }
                }
            }
        }
        Row {
            LabeledCheckbox(
                label = stringResource(R.string.dont_take_with_food),
                state = state.medication.doNotTakeWithFood,
                onStateChange = {
                    onEvent(MedicationEvent.UpdateMedicationState(state.medication.copy(doNotTakeWithFood = it)))
                },
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            )
        }
        Row {
            TextField(
                value = state.medication.comment,
                onValueChange = {
                    onEvent(MedicationEvent.UpdateMedicationState(state.medication.copy(comment = it)))
                },
                label = {
                    Text(stringResource(R.string.medication_comment))
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = MaterialTheme.colorScheme.background
                ),
                maxLines = 5,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .weight(1f)
            )
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
            onEvent(MedicationEvent
                .UpdateMedicationState(
                    state.medication.copy(startDate = DateTimeFormatter
                        .ofPattern(Constants.DATE_PATTERN)
                        .format(it)
                    )
                )
            )
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
            onEvent(MedicationEvent
                .UpdateMedicationState(
                    state.medication.copy(time = DateTimeFormatter
                        .ofPattern(Constants.TIME_24_H)
                        .format(it)
                    )
                )
            )
        }
    }
    val recurrenceOptions = stringArrayResource(R.array.recurrence_interval).toList()
    MaterialDialog (
        dialogState = recurrenceDialogState
    ) {
        this.listItemsSingleChoice(
            list = recurrenceOptions,
            initialSelection = 0,
            onChoiceChange = {
                if (it == recurrenceOptions.size - 1) {
                    onNavigateEvent(NavigateEvent.ToPrevious(Screen.MedicationOverview.route))
                } else {
                    onEvent(MedicationEvent.UpdateMedicationState(state.medication.copy(recurrence = recurrenceOptions[it])))
                }
                recurrenceDialogState.hide()
            },
            waitForPositiveButton = false
        )
    }
    val updateRecurrenceOptions = stringArrayResource(R.array.update_recurrence_options).toList()
    var selectedMethod by remember {
        mutableIntStateOf(0)
    }
    MaterialDialog (
        dialogState = updateDialogState,
        buttons = {
            positiveButton(
                text = stringResource(R.string.update),
                onClick = {
                    onEvent(MedicationEvent.UpdateMedicationMethod(selectedMethod))
                    onNavigateEvent(NavigateEvent.ToPrevious(Screen.MedicationOverview.route))
                }
            )
            negativeButton(text = stringResource(R.string.cancel))
        }
    ) {
        this.title(
            text = stringResource(R.string.title_update_recurrence)
        )
        this.listItemsSingleChoice(
            list = updateRecurrenceOptions,
            initialSelection = 0,
            onChoiceChange = {
                selectedMethod = it
            },
            waitForPositiveButton = true
        )
    }
    MaterialDialog (
        dialogState = deleteDialogState,
        buttons = {
            positiveButton(
                text = stringResource(R.string.delete),
                onClick = {
                    onEvent(MedicationEvent.RemoveMedicationMethod(selectedMethod))
                    onNavigateEvent(NavigateEvent.ToPrevious(Screen.MedicationOverview.route))
                }
            )
            negativeButton(text = stringResource(R.string.cancel))
        }
    ) {
        this.title(
            text = stringResource(R.string.title_remove_recurrence)
        )
        this.listItemsSingleChoice(
            list = updateRecurrenceOptions,
            initialSelection = 0,
            onChoiceChange = {
                selectedMethod = it
            },
            waitForPositiveButton = true
        )
    }
}

@Composable
@Preview
fun MedicationScreenPreview(){
    val navController = rememberNavController()
    MedicationScreen(
        state = MedicationUiState(
            medication = Medication(
                name = "Hello World",
                recurrence = "100y"
            ),
            time = LocalTime.MIDNIGHT,
        ),
        onEvent = {}
    )
}