package com.math3249.dialysis.ui.screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.math3249.dialysis.R
import com.math3249.dialysis.data.model.DialysisEntry
import com.math3249.dialysis.data.model.DialysisProgram
import com.math3249.dialysis.other.BooleanType
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.ui.components.DialysisDropDownMenu
import com.math3249.dialysis.ui.components.OutlinedNumberField
import com.math3249.dialysis.ui.components.SelectTextField
import com.math3249.dialysis.ui.components.dialysisProgramBackgroundBrush
import com.math3249.dialysis.ui.components.model.MenuItemData
import com.math3249.dialysis.ui.viewmodel.DialysisViewModel

@Composable
fun DialysisScreen(
    viewModel: DialysisViewModel,
    onSignOut: () -> Unit,
    modifier: Modifier
) {
    val listData = viewModel.entries
        .collectAsStateWithLifecycle()
        .value.let {
            it ?: mutableListOf<DialysisEntry>()
        }
    Box (
        modifier = Modifier
            .fillMaxSize()
    ){
        viewModel.setPrograms(listOf(
            DialysisProgram(
                name = stringResource(R.string.yellow_green_13),
                time = "13h",
                noOfCycles = 11,
                dialysisFluids = listOf(Constants.YELLOW, Constants.GREEN)
            ),
            DialysisProgram(
                name = stringResource(R.string.yellow_13),
                time = "13h",
                noOfCycles = 11,
                dialysisFluids = listOf(Constants.YELLOW)
            ),
            DialysisProgram(
                name = stringResource(R.string.yellow_green_16),
                time = "16h",
                noOfCycles = 13,
                dialysisFluids = listOf(Constants.YELLOW, Constants.GREEN)
            ),
            DialysisProgram(
                name = stringResource(R.string.yellow_16),
                time = "16h",
                noOfCycles = 13,
                dialysisFluids = listOf(Constants.YELLOW)
            )
        ))
        LazyColumn(
            modifier = Modifier
                .padding(1.dp)
                .fillMaxHeight()
        ){
            items(
                listData
            ) { listitem ->
                ListCard(
                    item = listitem,
                    editAction = {
                        viewModel.setStringData(listitem.key, Constants.KEY)
                        viewModel.setBooleanData(true, BooleanType.EDIT)
                    },
                    deleteAction = {
                        viewModel.setStringData(listitem.key, Constants.KEY)
                        viewModel.setBooleanData(true, BooleanType.DELETE)
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
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
        ){
            FloatingActionButton(
                onClick = { viewModel.setBooleanData(true, BooleanType.ADD) },
                shape = CircleShape,
                modifier = Modifier
                    .padding(top = 20.dp, end = 5.dp, bottom = 15.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
            }
            FloatingActionButton(
                onClick = { viewModel.setBooleanData(true, BooleanType.EXPANDED) },
                shape = CircleShape,
                modifier = Modifier
                    .padding(top = 20.dp, end = 15.dp, bottom = 15.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
                DialysisMenu(
                    expanded = viewModel.expanded.collectAsStateWithLifecycle().value,
                    onDismiss = {
                        viewModel.setBooleanData(false, BooleanType.EXPANDED)
                    },
                    onSignOut = {
                        onSignOut()
                        viewModel.setBooleanData(false, BooleanType.EXPANDED)
                    },
                    navigateSettings = {}
                )
            }
        }
    }

        if (viewModel.showEditDialog.collectAsStateWithLifecycle().value) {
            viewModel.setEditData(listData.first{item ->
                viewModel.itemKey.collectAsStateWithLifecycle().value == item.key
            })
            EnterNewDataDialog(
                onDismissRequest = {
                    viewModel.setBooleanData(false, BooleanType.EDIT)
                },
                onConfirmation = {
                    viewModel.updateDialysisEntry(viewModel.collectUpdatedData())
                    viewModel.setBooleanData(false, BooleanType.EDIT)
                },
                title = stringResource(R.string.edit),
                viewModel = viewModel,
                buttonText = stringResource(R.string.update)
            )

        }
        val context = LocalContext.current
        if (viewModel.showDeleteDialog.collectAsStateWithLifecycle().value) {
            val itemKey = viewModel.itemKey.collectAsStateWithLifecycle().value
            ConfirmDialog(
                question = stringResource(R.string.dialog_confirm_delete),
                onConfirmation = {
                    Toast.makeText(context, context.getText(R.string.entry_deleted_confirmation), Toast.LENGTH_SHORT).show()
                    viewModel.deleteDialysisEntry(itemKey)
                    viewModel.setBooleanData(false, BooleanType.DELETE)
                },
                onDismissRequest = {
                    viewModel.setBooleanData(false, BooleanType.DELETE)
                })
        }
        if (viewModel.showAddDialog.collectAsStateWithLifecycle().value) {
            viewModel.clearData()
            EnterNewDataDialog(
                onDismissRequest = {
                    viewModel.setBooleanData(false, BooleanType.ADD)
                },
                onConfirmation = {
                    viewModel.addEntry()
                    viewModel.setBooleanData(false, BooleanType.ADD)
                },
                title = stringResource(R.string.new_entry),
                viewModel = viewModel,
                buttonText = stringResource(R.string.add_entry)
            )
        }
//    }
}

@Composable
private fun DialysisMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSignOut: () -> Unit,
    navigateSettings: () -> Unit
) {
    val menuItems = listOf(
        MenuItemData(
            title = stringResource(R.string.settings),
            icon = Icons.Outlined.Settings,
            onClick = navigateSettings
        ),
        MenuItemData(
            title = stringResource(R.string.logout),
            icon = Icons.Outlined.Logout,
            onClick = onSignOut
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

@Composable
fun EnterNewDataDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    title: String,
    viewModel: DialysisViewModel,
    buttonText: String
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column (
            modifier = Modifier
                .padding(start = 5.dp, top = 2.dp, end = 5.dp)
                .shadow(10.dp, RoundedCornerShape(8.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Row {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(4f)
                )
            }
            OutlinedNumberField(
                value = viewModel.weightBefore.collectAsStateWithLifecycle().value,
                label = {
                    Text(stringResource(R.string.weight_before))
                },
                onValueChanged = {
                    viewModel.setStringData(it, Constants.WEIGHT_BEFORE)
                },
                modifier = Modifier
                    .padding(1.dp)
            )
            OutlinedNumberField(
                label = {
                    Text(stringResource(R.string.weight_after))
                },
                value = viewModel.weightAfter.collectAsStateWithLifecycle().value,
                onValueChanged = {
                    viewModel.setStringData(it, Constants.WEIGHT_AFTER)
                },
                modifier = Modifier.
                padding(1.dp)
            )
            OutlinedNumberField(
                label = {
                    Text(stringResource(R.string.ultrafiltration))
                },
                value = viewModel.ultrafiltration.collectAsStateWithLifecycle().value,
                onValueChanged = {
                    viewModel.setStringData(it, Constants.ULTRAFILTRATION)
                },
                modifier = Modifier.
                padding(1.dp)
            )
            SelectTextField(
                value = viewModel.selectedValue.collectAsStateWithLifecycle().value,
                label = stringResource(R.string.header_select_program),
                expanded = viewModel.selectExpanded.collectAsStateWithLifecycle().value,
                onExpandedChange = {
                    viewModel.setBooleanData(it, BooleanType.SELECT_EXPANDED)
                },
                onValueChange = {},
                onDismissRequest = { viewModel.setBooleanData(false, BooleanType.SELECT_EXPANDED) },
                items = listOf {
                    viewModel.dialysisPrograms.value.forEach { program ->
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
                                viewModel.setStringData(program.name, Constants.SELECTED_ITEM)
                                viewModel.setBooleanData(false, BooleanType.SELECT_EXPANDED)
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
                        Divider(
                            color = Color.White,
                            thickness = 1.dp
                        )
                    }
                },
                modifier = Modifier
                    .padding(1.dp)
            )
            Row (
                modifier = Modifier.padding(top = 1.dp, bottom = 1.dp)
            ){
                OutlinedButton(
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                        onConfirmation()
                    },
                    modifier = Modifier.padding(end = 5.dp)
                ) {
                    Text(buttonText)
                }
                OutlinedButton(
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                        onDismissRequest()
                    }
                ){
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }

}

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