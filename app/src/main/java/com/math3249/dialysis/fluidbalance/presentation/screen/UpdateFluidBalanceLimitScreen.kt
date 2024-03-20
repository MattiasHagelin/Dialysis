package com.math3249.dialysis.fluidbalance.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.math3249.dialysis.R
import com.math3249.dialysis.fluidbalance.domain.FluidBalanceEvent
import com.math3249.dialysis.fluidbalance.presentation.FluidBalanceUiState
import com.math3249.dialysis.navigation.NavigateEvent
import com.math3249.dialysis.navigation.Screen
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.NumberField

@Composable
fun UpdateFluidBalanceLimitScreen(
    state: FluidBalanceUiState = FluidBalanceUiState(),
    onEvent: (FluidBalanceEvent) -> Unit,
    onNavigate: (NavigateEvent) -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        DialysisAppBar(
            canNavigateBack = true,
            navigateUp = {
                onNavigate(NavigateEvent.ToPrevious(Screen.FluidBalanceDayScreen.route))
            },
            title = stringResource(R.string.header_update_fluid_limit),
            saveAction = {
                IconButton(onClick = {
                    onEvent(FluidBalanceEvent.UpdateFluidLimit)
                    onNavigate(NavigateEvent.ToPrevious(Screen.FluidBalanceDayScreen.route))
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Save,
                        contentDescription = null
                    )
                }

            }
        )
        NumberField(
            value = state.editFluidLimit,
            label = { Text(stringResource(R.string.header_fluid_limit)) },
            onValueChanged = {
                onEvent(FluidBalanceEvent.UpdateEditFluidLimit(it))
            },
            singleLine = true,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background
                )
                .fillMaxWidth()
                .padding(start = 2.dp, top = 2.dp, end = 2.dp)
        )
    }
}
@Composable
@Preview
fun UpdateFluidBalanceLimitScreenPreview() {
    UpdateFluidBalanceLimitScreen(
        onNavigate = {},
        onEvent = {}
    )
}