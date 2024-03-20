package com.math3249.dialysis.fluidbalance.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.math3249.dialysis.R
import com.math3249.dialysis.fluidbalance.domain.FluidBalanceEvent
import com.math3249.dialysis.fluidbalance.presentation.FluidBalanceUiState
import com.math3249.dialysis.navigation.NavigateEvent
import com.math3249.dialysis.navigation.Screen
import com.math3249.dialysis.ui.components.CategorizedLazyColumn
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.iconTitle
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun FluidBalanceHistoryScreen(
    state: FluidBalanceUiState = FluidBalanceUiState(),
    modifier: Modifier = Modifier,
    onEvent: (FluidBalanceEvent) -> Unit,
    onNavigate: (NavigateEvent) -> Unit
) {
    val deleteDialogState = rememberMaterialDialogState()
    Column {
        DialysisAppBar(
            canNavigateBack = true,
            navigateUp = { onNavigate(NavigateEvent.ToPrevious(Screen.FluidBalanceDayScreen.route)) },
            title = stringResource(R.string.screen_fluid_balance_history),
            saveAction = {
                IconButton(
                    onClick = { 
                        deleteDialogState.show()
                    }
                ) {
                    Icon(imageVector = Icons.Outlined.Clear, contentDescription = null)
                }
            }
        )
        if (state.history.isNotEmpty()) {
            CategorizedLazyColumn(categories = state.history)
        } else {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.empty_history),
                    modifier = modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
    MaterialDialog (
        dialogState = deleteDialogState,
        buttons = {
            positiveButton(
                text = stringResource(R.string.yes),
                onClick = {
                    onEvent(FluidBalanceEvent.ClearHistory)
                }
            )
            negativeButton(text = stringResource(R.string.no))
        },

        ){
        this.iconTitle(
            text = stringResource(R.string.clear_history),
        ) {
            Icon(imageVector = Icons.Outlined.Warning, contentDescription = null)
        }
        this.message(text = stringResource(R.string.dialog_clear_history))
    }
}

@Composable
@Preview
fun FluidBalanceHistoryScreenPreview() {
    FluidBalanceHistoryScreen(
        state = FluidBalanceUiState(),
        onEvent = {},
        onNavigate = {}
    )
}