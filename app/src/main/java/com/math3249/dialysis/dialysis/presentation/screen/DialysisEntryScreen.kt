package com.math3249.dialysis.dialysis.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.math3249.dialysis.R
import com.math3249.dialysis.dialysis.domain.DialysisEvent
import com.math3249.dialysis.dialysis.presentation.DialysisUiState
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.dialysisProgramBackgroundBrush
import com.math3249.dialysis.ui.components.NumberField
import com.math3249.dialysis.ui.components.SelectTextField

@Composable
fun DialysisEntryScreen(
    state: DialysisUiState = DialysisUiState(),
    onEvent: (DialysisEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        DialysisAppBar(
            canNavigateBack = true,
            navigateUp = {
                onEvent(DialysisEvent.Clear)
                onEvent(DialysisEvent.Back)
            },
            title = if (state.itemKey != "") {
                stringResource(R.string.update_dialysis_entry)
            } else {
                stringResource(R.string.add_dialysis_entry)
            },
            menu = { },
            saveAction = {
                IconButton(
                    onClick = {
                        if (state.itemKey != ""){
                            onEvent(DialysisEvent.UpdateDialysisEntry)
                        } else {
                            onEvent(DialysisEvent.CreateEntry)
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Outlined.Save, contentDescription = null)
                }
            }
        )
        NumberField(
            label = {
                    Text(text = stringResource(R.string.weight_before))
            },
            value = state.weightBefore,
            onValueChanged = {
                onEvent(DialysisEvent.UpdateWeightBefore(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, end = 2.dp)
                .background(MaterialTheme.colorScheme.background)
        )
        NumberField(
            label = {
                    Text(text = stringResource(R.string.weight_after))
            },
            value = state.weightAfter,
            onValueChanged = {
                onEvent(DialysisEvent.UpdateWeightAfter(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, end = 2.dp)
                .background(MaterialTheme.colorScheme.background)
        )
        NumberField(
            label = {
                    Text(text = stringResource(R.string.ultrafiltration))
            },
            value = state.ultrafiltration,
            onValueChanged = {
                onEvent(DialysisEvent.UpdateUltrafiltration(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, end = 2.dp)
                .background(MaterialTheme.colorScheme.background)
        )
        SelectTextField(
                value = state.selectedProgram,
                label = stringResource(R.string.header_select_program),
                onValueChange = {},
                onDismissRequest = {
                    it.value = false
                },
                items = listOf { expanded ->
                    state.programs.forEach { program ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = program.time,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                )
                            },
                            onClick = {
                                onEvent(DialysisEvent.UpdateSelectedProgram(program.name))
                                expanded.value = false
                            },
                            modifier = Modifier
                                .background(
                                    brush = dialysisProgramBackgroundBrush(
                                        isVerticalGradient = false,
                                        colors = program.dialysisFluids.map {
                                            Color(it.toColorInt())
                                        }
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(3.dp)
                        )

                    }
                    Divider(
                        color = Color.White,
                        thickness = 1.dp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
    }
}

@Composable
@Preview
fun DialysisEntryScreenPreview() {
    DialysisEntryScreen(
        state = DialysisUiState(
            weightAfter = "137"
        ),
        onEvent ={} )
}