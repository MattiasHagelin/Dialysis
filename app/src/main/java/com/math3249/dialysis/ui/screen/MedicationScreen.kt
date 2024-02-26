package com.math3249.dialysis.ui.screen

import android.content.Context
import android.widget.Toast
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.SelectTextField
import com.math3249.dialysis.ui.viewmodel.MedicationViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun MedicationScreen(
     onNavigateBack: () -> Unit,
     onConfirmAction: () -> Unit,
     title: String,
     buttonText: String,
     viewModel: MedicationViewModel?,
     context: Context?
) {
    var startDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var time by remember {
        mutableStateOf(LocalTime.now())
    }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd-MM-yyyy")
                .format(startDate)
        }
    }

    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("HH:mm")
                .format(time)
        }
    }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        DialysisAppBar(
            canNavigateBack = true,
            navigateUp = onNavigateBack,
            canSignOut = false,
            showMenu = false,
            title = title,
            onSignOut = { /*TODO*/ },
            currentLocation = "here",
            canSave = true,
            onSaveAction = {}
        )
        Row {
            OutlinedTextField(
                value = "",
                onValueChange = {},
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
                value = "",
                onValueChange = {},
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
                value = "",
                label = "Unit",
                expanded = false,
                onExpandedChange = {},
                onValueChange = {},
                onDismissRequest = { /*TODO*/ },
                items = listOf {
                    DropdownMenuItem(
                        text = { "ml" }, onClick = { /*TODO*/ }
                    )
                    DropdownMenuItem(
                        text = { "g" }, onClick = { /*TODO*/ }
                    )
                    DropdownMenuItem(
                        text = { "tablett" }, onClick = { /*TODO*/ }
                    )
                },
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .weight(1f)
            )

        }
        Row {
            OutlinedTextField(
                value = formattedDate,
                onValueChange = {},
                singleLine = true,
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
                value = formattedTime,
                onValueChange = {},
                singleLine = true,
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
                value = "",
                onValueChange = {},
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
    }

    MaterialDialog (
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                Toast.makeText(
                    context!!,
                    "Clicked",
                    Toast.LENGTH_LONG
                ).show()
            }
            negativeButton(text = "Cancel")
        }
    ){
        this.datepicker(
            initialDate = LocalDate.now(),
            title = "Start date",
        ) {
            startDate = it
        }
    }

    MaterialDialog (
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                Toast.makeText(
                    context!!,
                    "Clicked",
                    Toast.LENGTH_LONG
                ).show()
            }
            negativeButton(text = "Cancel")
        }
    ){
        this.timepicker(
            initialTime = LocalTime.now(),
            title = "Time",
            is24HourClock = true

        ){
            time = it
        }
    }
}

@Composable
@Preview
fun MedicationScreenPreview(){
    MedicationScreen(
        onNavigateBack = {},
        onConfirmAction = {},
        title = "Title",
        buttonText = "button",
        viewModel = null,
        context = null
    )
}