package com.math3249.dialysis.dialysis.presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.math3249.dialysis.R
import com.math3249.dialysis.dialysis.data.DialysisEntry
import com.math3249.dialysis.dialysis.data.DialysisProgram
import com.math3249.dialysis.dialysis.domain.DialysisEvent
import com.math3249.dialysis.dialysis.presentation.DialysisUiState
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.DialysisDropDownMenu
import com.math3249.dialysis.ui.components.dialysisProgramBackgroundBrush
import com.math3249.dialysis.ui.components.model.MenuItemData
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.iconTitle
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.update

@Composable
fun DialysisOverviewScreen(
    state: DialysisUiState,
    onEvent: (DialysisEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val deleteDialogState = rememberMaterialDialogState()
    Column (
        modifier = modifier
            .fillMaxSize()
    ){
        DialysisAppBar(
            canNavigateBack = true,
            navigateUp = {
                onEvent(DialysisEvent.Back)
            },
            title = stringResource(R.string.dialysis_entries),
            menu = { expanded ->
                IconButton(onClick = { expanded.update { true } }) {
                    Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
                }
                DialysisMenu(
                    expanded = expanded.collectAsStateWithLifecycle().value,
                    onDismiss = {
                        expanded.update { false }
                    },
                    onEvent = onEvent
                )
            }
        )
        Box(modifier = Modifier
            .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(1.dp)
            ){
                items(
                    state.entries
                ) { listItem ->
                    ListCard(
                        item = listItem,
                        editAction = {
                            onEvent(DialysisEvent.Edit(listItem.key))
                        },
                        deleteAction = {
                            onEvent(DialysisEvent.UpdateItemKey(listItem.key))
                            deleteDialogState.show()
                        },
                        modifier = Modifier
                            .padding(start = 5.dp, top = 2.dp, end = 5.dp)
                            .shadow(10.dp, RoundedCornerShape(8.dp))
                            .background(Color.White)

                    )
                }
            }

            Row (
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
            ){
                FloatingActionButton(
                    onClick = {
                        onEvent(DialysisEvent.Add)
                    },
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(top = 20.dp, end = 20.dp, bottom = 20.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                }
            }
        }
    }
    MaterialDialog (
        dialogState = deleteDialogState,
        buttons = {
            positiveButton(
                text = stringResource(R.string.yes),
                onClick = {
                    onEvent(DialysisEvent.DeleteEntry)
                }
            )
            negativeButton(text = stringResource(R.string.no))
        },

    ){
        this.iconTitle(
            text = stringResource(R.string.delete),
        ) {
            Icon(imageVector = Icons.Outlined.Warning, contentDescription = null)
        }
        this.message(text = stringResource(R.string.dialog_delete))
    }
////    }
}

@Composable
private fun DialysisMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEvent: (DialysisEvent) -> Unit
) {
    val menuItems = listOf(
        MenuItemData(
            title = stringResource(R.string.settings),
            icon = Icons.Outlined.Settings,
            onClick = { /*TODO Navigate to settings*/ }
        ),
        MenuItemData(
            title = stringResource(R.string.logout),
            icon = Icons.Outlined.Logout,
            onClick = {onEvent(DialysisEvent.SignOut)}
        )
    )
    DialysisDropDownMenu(
        expanded = expanded,
        onDismiss = onDismiss,
        menuItems = menuItems
    )
}
@Composable
fun ConfirmDialog(
    question: String,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .padding(start = 5.dp, top = 2.dp, end = 5.dp)
                .shadow(10.dp, RoundedCornerShape(8.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row (
            ){
                Text(
                    modifier = Modifier.weight(2f),
                    text = question,
                    textAlign = TextAlign.Center
                )
            }
            Row {
                OutlinedButton(
                    onClick = { onConfirmation() }
                ) {
                    Text(text = stringResource(R.string.delete))
                }
                OutlinedButton(
                    onClick = { onDismissRequest() }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        }
    }
}

//@Composable
//fun EnterNewDataDialog(
//    onDismissRequest: () -> Unit,
//    onConfirmation: () -> Unit,
//    title: String,
//    viewModel: DialysisViewModel,
//    buttonText: String
//) {
//    Dialog(onDismissRequest = { onDismissRequest() }) {
//        Column (
//            modifier = Modifier
//                .padding(start = 5.dp, top = 2.dp, end = 5.dp)
//                .shadow(10.dp, RoundedCornerShape(8.dp))
//                .background(Color.White),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ){
//            Row {
//                Text(
//                    text = title,
//                    textAlign = TextAlign.Center,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier
//                        .padding(10.dp)
//                        .weight(4f)
//                )
//            }
//            OutlinedNumberField(
//                value = viewModel.weightBefore.collectAsStateWithLifecycle().value,
//                label = {
//                    Text(stringResource(R.string.weight_before))
//                },
//                onValueChanged = {
//                    viewModel.setStringData(it, Constants.WEIGHT_BEFORE)
//                },
//                modifier = Modifier
//                    .padding(1.dp)
//            )
//            OutlinedNumberField(
//                label = {
//                    Text(stringResource(R.string.weight_after))
//                },
//                value = viewModel.weightAfter.collectAsStateWithLifecycle().value,
//                onValueChanged = {
//                    viewModel.setStringData(it, Constants.WEIGHT_AFTER)
//                },
//                modifier = Modifier.
//                padding(1.dp)
//            )
//            OutlinedNumberField(
//                label = {
//                    Text(stringResource(R.string.ultrafiltration))
//                },
//                value = viewModel.ultrafiltration.collectAsStateWithLifecycle().value,
//                onValueChanged = {
//                    viewModel.setStringData(it, Constants.ULTRAFILTRATION)
//                },
//                modifier = Modifier.
//                padding(1.dp)
//            )
//            SelectTextField(
//                value = viewModel.selectedValue.collectAsStateWithLifecycle().value,
//                label = stringResource(R.string.header_select_program),
//                expanded = viewModel.selectExpanded.collectAsStateWithLifecycle().value,
//                onExpandedChange = {
//                    viewModel.setBooleanData(it, BooleanType.SELECT_EXPANDED)
//                },
//                onValueChange = {},
//                onDismissRequest = { viewModel.setBooleanData(false, BooleanType.SELECT_EXPANDED) },
//                items = listOf {
//                    viewModel.dialysisPrograms.value.forEach { program ->
//                        DropdownMenuItem(
//                            text = {
//                                Text(
//                                    text = program.time,
//                                    textAlign = TextAlign.Center,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(5.dp)
//                                )
//                            },
//                            onClick = {
//                                viewModel.setStringData(program.name, Constants.SELECTED_ITEM)
//                                viewModel.setBooleanData(false, BooleanType.SELECT_EXPANDED)
//                            },
//                            modifier = Modifier
//                                .background(
//                                    brush = dialysisProgramBackgroundBrush(
//                                        isVerticalGradient = false,
//                                        colors = program.dialysisFluids.map {
//                                            Color(it.toColorInt())
//                                        }
//                                    ),
//                                    shape = RoundedCornerShape(8.dp)
//                                )
//                                .padding(3.dp)
//                        )
//                        Divider(
//                            color = Color.White,
//                            thickness = 1.dp
//                        )
//                    }
//                },
//                modifier = Modifier
//                    .padding(1.dp)
//            )
//            Row (
//                modifier = Modifier.padding(top = 1.dp, bottom = 1.dp)
//            ){
//                OutlinedButton(
//                    shape = RoundedCornerShape(15.dp),
//                    onClick = {
//                        onConfirmation()
//                    },
//                    modifier = Modifier.padding(end = 5.dp)
//                ) {
//                    Text(buttonText)
//                }
//                OutlinedButton(
//                    shape = RoundedCornerShape(15.dp),
//                    onClick = {
//                        onDismissRequest()
//                    }
//                ){
//                    Text(stringResource(R.string.cancel))
//                }
//            }
//        }
//    }
//}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListCard(
    item: DialysisEntry,
    editAction: () -> Unit,
    deleteAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .combinedClickable(
                onClick = {
                    editAction()
                },
                onLongClick = {
                    deleteAction()
                }
            )
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 5.dp, top = 5.dp, end = 5.dp)
        ){
            Text(
                text = item.date,
                modifier = Modifier
                    .weight(2f)
            )
            Text(
                text = item.program!!.time,
                modifier = Modifier
                    .padding(end = 5.dp)
            )
            Box (
                modifier = Modifier
                    .width(40.dp)
                    .height(20.dp)
                    .background(
                        brush = dialysisProgramBackgroundBrush(
                            isVerticalGradient = false,
                            colors = item.program!!.dialysisFluids.map {
                                Color(it.toColorInt())
                            }
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(50.dp)
            ){

            }
        }
        Row (
            modifier = Modifier.padding(bottom = 5.dp)
        ) {
            Column (
                modifier = Modifier
                    .padding(start = 5.dp)
                    .weight(2f)
            ) {
                Text(
                    text = stringResource(R.string.weight_before),
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.weightBefore,
                    textAlign = TextAlign.Start,
                )
            }
            Column (
                modifier = Modifier
                    .weight(2f)
            ){
                Text(
                    text = stringResource(R.string.weight_after),
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.weightAfter,
                    textAlign = TextAlign.Start
                )
            }
            Column (
                modifier = Modifier
                    .weight(2f)
            ){
                Text(
                    text = stringResource(R.string.ultrafiltration),
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.ultraFiltration,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
@Preview
fun DialysisScreenPreview() {
    DialysisOverviewScreen(
        state = DialysisUiState(
            entries = mutableListOf(
                DialysisEntry(
                    weightBefore = "100",
                    weightAfter = "50",
                    ultraFiltration = "50000",
                    date = "2024-03-08",
                    program = DialysisProgram(
                        name = "CooltProgram",
                        dialysisFluids = listOf(Constants.YELLOW)
                    )
                )
            )
        ),
        onEvent = {},  modifier = Modifier)
}