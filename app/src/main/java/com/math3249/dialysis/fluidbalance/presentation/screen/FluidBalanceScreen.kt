package com.math3249.dialysis.fluidbalance.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.math3249.dialysis.R
import com.math3249.dialysis.fluidbalance.domain.FluidBalanceEvent
import com.math3249.dialysis.fluidbalance.presentation.FluidBalanceUiState
import com.math3249.dialysis.navigation.NavigateEvent
import com.math3249.dialysis.navigation.Screen
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.DialysisDropDownMenu
import com.math3249.dialysis.ui.components.LabeledRadioButton
import com.math3249.dialysis.ui.components.NumberField
import com.math3249.dialysis.ui.components.OutlinedNumberField
import com.math3249.dialysis.ui.components.model.MenuItemData
import kotlinx.coroutines.flow.update

@Composable
fun FluidBalanceScreen(
    state: FluidBalanceUiState,
    onNavigate: (NavigateEvent) -> Unit,
    onEvent: (FluidBalanceEvent) -> Unit = {}
){
    val focusManager = LocalFocusManager.current
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        DialysisAppBar(
            canNavigateBack = true,
            navigateUp = {
               onNavigate(NavigateEvent.ToPrevious(Screen.StartScreen.route))
            },
            title = stringResource(R.string.screen_fluid_balance),
            menu = { expanded ->
                IconButton(onClick = { expanded.update { true } }) {
                    Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
                }
                FluidBalanceMenu(
                    expanded = expanded.collectAsStateWithLifecycle().value,
                    onDismiss = { expanded.update { false } },
                    onEvent = onEvent,
                    onNavigate = onNavigate
                )
            }
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp)
        ) {
            LabeledRadioButton(
                label = stringResource(R.string.water),
                value = R.string.water,
                selected = state.selectedFluid == R.string.water,
                fontSize = 12.sp,
                onStateChange = { value ->
                    onEvent(FluidBalanceEvent.UpdateSelectedFluid(value))
                } )
            LabeledRadioButton(
                label = stringResource(R.string.juice),
                value = R.string.juice,
                selected = state.selectedFluid == R.string.juice,
                fontSize = 12.sp,
                onStateChange = {value ->
                    onEvent(FluidBalanceEvent.UpdateSelectedFluid(value))
                } )
            LabeledRadioButton(
                label = stringResource(R.string.dairy),
                value = R.string.dairy,
                selected = state.selectedFluid == R.string.dairy,
                fontSize = 12.sp,
                onStateChange = { value->
                    onEvent(FluidBalanceEvent.UpdateSelectedFluid(value))
                } )
            LabeledRadioButton(
                label = stringResource(R.string.nutrition),
                value = R.string.nutrition,
                selected = state.selectedFluid == R.string.nutrition,
                fontSize = 12.sp,
                onStateChange = { value->
                    onEvent(FluidBalanceEvent.UpdateSelectedFluid(value))
                } )
            LabeledRadioButton(
                label = stringResource(R.string.other),
                value = R.string.other,
                selected = state.selectedFluid == R.string.other,
                fontSize = 12.sp,
                onStateChange = { value ->
                    onEvent(FluidBalanceEvent.UpdateSelectedFluid(value))
                } )
        }
        if (state.selectedFluid == R.string.other){
            TextField(
                value = state.otherText,
                label = {
                    Text(stringResource(R.string.other_fluid))
                },
                onValueChange = {
                    onEvent(FluidBalanceEvent.UpdateOtherText(it))
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.background
                    )
                    .fillMaxWidth()
                    .padding(start = 2.dp, top = 2.dp, end = 2.dp)
            )
        }
        NumberField(
            value = state.givenVolume,
            label = { Text(stringResource(R.string.header_consumed_fluid)) },
            onValueChanged = {
                onEvent(FluidBalanceEvent.UpdateGivenVolume(it))
            },
            singleLine = true,
            onDone = {
                onEvent(FluidBalanceEvent.UpdateDrunkVolume)
                focusManager.clearFocus()
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.add),
                    modifier = Modifier.clickable {
                        onEvent(FluidBalanceEvent.UpdateDrunkVolume)
                        focusManager.clearFocus()
                    }
                ) },
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background
                )
                .fillMaxWidth()
                .padding(start = 2.dp, top = 2.dp, end = 2.dp)
        )
            TextCard(
                header = stringResource(R.string.header_fluid_limit),
                content = state.fluidLimit.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 2.dp, top = 2.dp, end = 2.dp)
                    .shadow(1.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .clickable {
                        focusManager.clearFocus()
                        onNavigate(NavigateEvent.ToUpdateFluidBalanceLimit)
                    }
            )
        TextCard(
            header = stringResource(R.string.header_consumed_fluid),
            content = state.drunkVolume.toString(),
            showExtra = true,
            extraContent = state.lastDrunkTime + " "
                    + state.lastDrunkVolume + " "
                    + state.volumeUnit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, top = 2.dp, end = 2.dp)
                .shadow(1.dp)
                .background(MaterialTheme.colorScheme.background)
        )
        TextCard(
            header = stringResource(R.string.header_remaining_fluid),
            content = state.remainingFluid.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, top = 2.dp, end = 2.dp)
                .shadow(1.dp)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Composable
private fun FluidBalanceMenu(
    expanded: Boolean,
    onEvent: (FluidBalanceEvent) -> Unit,
    onNavigate: (NavigateEvent) -> Unit,
    onDismiss: () -> Unit
) {
    val resetText = stringResource(R.string.reset_message)
    val menuItems = listOf(
        MenuItemData(
            title = stringResource(R.string.reset),
            icon = Icons.Outlined.Refresh,
            onClick = {
                onEvent(FluidBalanceEvent.UpdateOtherText(resetText))
                onEvent(FluidBalanceEvent.Reset)
                onDismiss()
            }
        ),
        MenuItemData(
            title = stringResource(R.string.history),
            icon = Icons.Outlined.History,
            onClick = {
                onNavigate(NavigateEvent.ToFluidBalanceHistory)
            }
        ),
        MenuItemData(
            title = stringResource(R.string.settings),
            icon = Icons.Outlined.Settings,
            onClick = { }
        ),
        MenuItemData(
            title = stringResource(R.string.logout),
            icon = Icons.Outlined.Logout,
            onClick = { onNavigate(NavigateEvent.ToSignIn) }
        )
    )
    DialysisDropDownMenu(
        expanded = expanded,
        onDismiss = onDismiss,
        menuItems = menuItems
    )
}

@Composable
fun TextCard(
    header: String,
    content: String,
    showExtra: Boolean = false,
    extraContent: String = "",
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column (
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Text(
                text = header,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Box (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = content,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .align(Alignment.Center)
                )
                if (showExtra) {
                    Text(
                        text = extraContent,
                        fontSize = 10.sp,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EditDialog(
    header: String,
    value: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDone: KeyboardActionScope.() -> Unit,
    onValueChanged: (String) -> Unit,
    modifier: Modifier
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column (
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ){
            Row (
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp)
            ){
                OutlinedNumberField(
                    value = value,
                    label = {
                        Text(header)
                    },
                    onValueChanged = onValueChanged,
                    singleLine = true,
                    onDone = onDone,
                    modifier = Modifier
                        .padding(10.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(8.dp)
                        )
                )
            }

            Row (
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            ){
                OutlinedButton(
                    onClick = onConfirm,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp, end = 5.dp)
                ) {
                    Text(stringResource(R.string.update))
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 5.dp, end = 10.dp)
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}

@Composable
@Preview
fun FluidBalanceScreenPreview() {
    FluidBalanceScreen(
        state = FluidBalanceUiState(
            givenVolume = "150",
            lastDrunkVolume = "75",
            lastDrunkTime = "13:44",
            volumeUnit = "ml"
        ),
        onNavigate = {}
    )
}