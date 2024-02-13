package com.math3249.dialysis.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.math3249.dialysis.R
import com.math3249.dialysis.data.model.FluidBalance
import com.math3249.dialysis.ui.components.DialysisDropDownMenu
import com.math3249.dialysis.ui.components.OutlinedNumberField
import com.math3249.dialysis.ui.viewmodel.FluidBalanceViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun FluidBalanceScreen(
    viewModel: FluidBalanceViewModel,
    onSignOut: () -> Unit,
    navigateSettings: () -> Unit
){
    val expanded = MutableStateFlow(false)
    val fluidBalance = viewModel.fluidBalance
        .collectAsStateWithLifecycle()
        .value.let {
            it ?: FluidBalance()
        }
    val focusRequester = viewModel.focusRequester.collectAsStateWithLifecycle().value
    val focusManager = LocalFocusManager.current
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        OutlinedNumberField(
            value = viewModel.consumedFluid.collectAsStateWithLifecycle().value,
            label = { Text(stringResource(R.string.hint_consumed_fluid)) },
            onValueChanged = { viewModel.setConsumedFluid(it) },
            singleLine = true,
            onDone = {
                viewModel.addConsumedFluid(viewModel.consumedFluid.value)
                viewModel.setConsumedFluid("")
                focusManager.clearFocus()
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.AddCircleOutline,
                    contentDescription = stringResource(R.string.add),
                    modifier = Modifier.clickable {
                        viewModel.addConsumedFluid(viewModel.consumedFluid.value)
                        viewModel.setConsumedFluid("")
                        focusManager.clearFocus()
                    }
                ) },
            modifier = Modifier
                .focusRequester(focusRequester)
                .padding(top = 100.dp)
                .align(Alignment.TopCenter)
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    RoundedCornerShape(8.dp)
                )
        )
        Column (
            modifier = Modifier
                .align(Alignment.Center)
        ){
            TextCard(
                header = "Fluid limit",
                content = fluidBalance.fluidLimit.toString(),
                modifier = Modifier
                    .width(200.dp)
                    .padding(start = 5.dp, top = 2.dp, end = 5.dp)
                    .shadow(10.dp, RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        focusManager.clearFocus()
                        viewModel.showDialog(true)
                    }
            )
            TextCard(
                header = "Consumed fluid",
                content = fluidBalance.consumedFluid.toString(),
                modifier = Modifier
                    .width(200.dp)
                    .padding(start = 5.dp, top = 2.dp, end = 5.dp)
                    .shadow(10.dp, RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )
            TextCard(
                header = "Remaining fluid",
                content = (fluidBalance.fluidLimit.minus(fluidBalance.consumedFluid)).toString(),
                modifier = Modifier
                    .width(200.dp)
                    .padding(start = 5.dp, top = 2.dp, end = 5.dp)
                    .shadow(10.dp, RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )
        }
        Row (
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
        ){
            FloatingActionButton(
                onClick = { expanded.value = true },
                shape = CircleShape,
                modifier = Modifier
                    .padding(top = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
                DialysisDropDownMenu(
                    expanded = expanded,
                    onSignOut = {
                        onSignOut()
                    },
                    onSettings = navigateSettings
                )
            }
        }
    }
    if (viewModel.showDialog.collectAsStateWithLifecycle().value)
        EditDialog(
            header = "Update Fluid Limit",
            value = viewModel.editFluidLimit.collectAsStateWithLifecycle().value,
            onValueChanged =
            {
                viewModel.setEditFluidLimit(it)
            },
            onDismiss =
            {
                viewModel.showDialog(false)
            },
            onConfirm =
            {
                viewModel.updateFluidLimit(viewModel.editFluidLimit.value)
                viewModel.showDialog(false)
            },
            onDone =
            {
                viewModel.updateFluidLimit(viewModel.editFluidLimit.value)
                viewModel.showDialog(false)
            },
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                )
        )
}

@Composable
fun TextCard(
    header: String,
    content: String,
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
            Text(
                text = content,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .align(Alignment.CenterHorizontally)
            )
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
                    Text(stringResource(R.string.edit))
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
fun TextCardPreview() {
    Column {
        TextCard(
            header = "Test header",
            content = "test content",
            modifier = Modifier
                .width(200.dp)
                .padding(start = 5.dp, top = 2.dp, end = 5.dp)
                .shadow(10.dp, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        TextCard(
            header = "Test header",
            content = "test content",
            modifier = Modifier
                .width(200.dp)
                .padding(start = 5.dp, top = 2.dp, end = 5.dp)
                .shadow(10.dp, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
    }
}