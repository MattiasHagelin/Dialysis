package com.math3249.dialysis.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.math3249.dialysis.R
import com.math3249.dialysis.data.model.DialysisEntry
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.other.DialogType
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.viewmodel.DialysisViewModel
import com.math3249.dialysis.ui.components.DialysisScreen

@Composable
fun DialysisScreen(
    navController: NavHostController,
    viewModel: DialysisViewModel,
    onSignOut: () -> Unit,
    modifier: Modifier
) {
    val listData = viewModel.entries
        .collectAsStateWithLifecycle()
        .value.let {
            it ?: mutableListOf<DialysisEntry>()
        }
    Scaffold (
        topBar = {
            DialysisAppBar(
                navController = navController,
                canNavigateBack = false,
                navigateUp = { /*TODO*/ },
                onSignOut = onSignOut,
                title = "Dialysis",
                currentLocation = DialysisScreen.Dialysis,
                modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .shadow(1.dp, RectangleShape)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    viewModel.showDialog(true, DialogType.ADD)
                }
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        }
    ){ padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight()
        ){
            items(
                listData
            ) { listitem ->
                ListCard(
                    item = listitem,
                    editAction = {
                        viewModel.setStringData(listitem.key, Constants.KEY)
                        viewModel.showDialog(true, DialogType.EDIT)
                    },
                    deleteAction = {
                        viewModel.setStringData(listitem.key, Constants.KEY)
                        viewModel.showDialog(true, DialogType.DELETE)
                    },
                    modifier = Modifier
                        .padding(start = 5.dp, top = 2.dp, end = 5.dp)
                        .shadow(10.dp, RoundedCornerShape(8.dp))
                        .background(Color.White)

                )
            }
        }
        if (viewModel.showEditDialog.collectAsStateWithLifecycle().value) {
            val data = listData.first{item ->
                viewModel.itemKey.collectAsStateWithLifecycle().value == item.key
            }
            EnterNewDataDialog(
                data = data,
                onDismissRequest = {
                    viewModel.showDialog(false, DialogType.EDIT)
                },
                onConfirmation = {
                    viewModel.updateDialysisEntry(data)
                    viewModel.showDialog(false, DialogType.EDIT)
                },
                title = stringResource(R.string.edit),
                viewModel = viewModel,
                buttonText = stringResource(R.string.save)
            )

        }
        val context = LocalContext.current
        if (viewModel.showDeleteDialog.collectAsStateWithLifecycle().value) {
            val itemKey = viewModel.itemKey.collectAsStateWithLifecycle().value
            ConfirmDialog(
                question = "Really delete?",
                onConfirmation = {
                    Toast.makeText(context, "Entry deleted.", Toast.LENGTH_SHORT).show()
                    viewModel.deleteDialysisEntry(itemKey)
                    viewModel.showDialog(false, DialogType.DELETE)
                },
                onDismissRequest = {
                    viewModel.showDialog(false, DialogType.DELETE)
                })
        }
        if (viewModel.showAddDialog.collectAsStateWithLifecycle().value) {
            val data = DialysisEntry("")
            EnterNewDataDialog(
                data = data,
                onDismissRequest = {
                    viewModel.showDialog(false, DialogType.ADD)
                },
                onConfirmation = {
                    viewModel.addEntry(data)
                    viewModel.showDialog(false, DialogType.ADD)
                },
                title = stringResource(R.string.new_entry),
                viewModel = viewModel,
                buttonText = stringResource(R.string.add_entry)
            )
        }
    }
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
    data: DialysisEntry,
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

            OutlinedTextField(
                value = viewModel.weightBefore.collectAsStateWithLifecycle().value,
                placeholder = {
                    Text(stringResource(R.string.weight_before))
                },
                onValueChange = {
                    viewModel.setStringData(it, Constants.WEIGHT_BEFORE)
                },
                modifier = Modifier
                    .padding(1.dp)
            )
            OutlinedTextField(
                placeholder = {
                    Text(stringResource(R.string.weight_after))
                },
                value = viewModel.weightAfter.collectAsStateWithLifecycle().value,
                onValueChange = {
                    viewModel.setStringData(it, Constants.WEIGHT_AFTER)
                },
                modifier = Modifier.
                padding(1.dp)
            )
            OutlinedTextField(
                placeholder = {
                    Text(stringResource(R.string.ultrafiltration))
                },
                value = viewModel.ultrafiltration.collectAsStateWithLifecycle().value,
                onValueChange = {
                    viewModel.setStringData(it, Constants.ULTRAFILTRATION)
                },
                modifier = Modifier.
                padding(1.dp))
            Row (
                modifier = Modifier.padding(top = 1.dp, bottom = 1.dp)
            ){
                OutlinedButton(
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                        data.morningWeight = viewModel.weightAfter.value
                        data.eveningWeight = viewModel.weightBefore.value
                        data.ultraFiltration = viewModel.ultrafiltration.value
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

@Composable
fun ListCard(
    item: DialysisEntry,
    editAction: () -> Unit,
    deleteAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 5.dp)
        ){
            Text(
                text = item.date,
                modifier = Modifier
                    .weight(2f)
            )
            IconButton(
                onClick = {
                    editAction()
                }) {
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .size(20.dp),
                    imageVector = Icons.Outlined.Edit,
                    tint = Color.LightGray,
                    contentDescription = "Edit"
                )
            }
            IconButton(
                onClick = {
                    deleteAction()
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = Color.LightGray,
                    modifier = Modifier
                        .weight(1f)
                        .size(20.dp)

                )
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
                    text = item.morningWeight,
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
                    text = item.eveningWeight,
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