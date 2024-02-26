package com.math3249.dialysis.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.math3249.dialysis.R
import com.math3249.dialysis.ui.components.DialysisDropDownMenu
import com.math3249.dialysis.ui.components.SelectTextField
import com.math3249.dialysis.ui.components.model.MenuItemData
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MedicationListScreen(
    navigateAdd: () -> Unit
//    medicationListViewModel: MedicationListViewModel,
//    onSignOut: () -> Unit

) {
    val expanded = MutableStateFlow(false)
    val showEdit = MutableStateFlow(false)
    val items = mutableListOf("")
    for (i in 1..100) {
        items.add(i.toString())
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
        ) {
            items(items) { item ->
                MedicationCard(data = item)
            }
        }
        Row (
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
//            FloatingActionButton(
//                onClick = { showEdit.value = true },
//                shape = CircleShape,
//                modifier = Modifier
//                    .padding(top = 20.dp, end = 5.dp, bottom = 15.dp)
//            ) {
//                Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
//            }
            FloatingActionButton(
                onClick = { expanded.value = true },
                shape = CircleShape,
                modifier = Modifier
                    .padding(top = 20.dp, end = 15.dp, bottom = 15.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
                MedicationsMenu(
                    expanded = expanded.collectAsStateWithLifecycle().value,
                    onDismiss = {
                        expanded.value = false
                    },
                    onSignOut = { /*TODO*/ },
                    onAdd = navigateAdd,
                    navigateSettings = {}
                )
            }
        }
    }

    if (false) {
        MedicineDialog (
            onDismiss = {showEdit.value = false}
        )
    }
}

@Composable
private fun MedicineDialog(
    onDismiss: () -> Unit
){
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(top = 10.dp, bottom = 10.dp)
        ){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
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
                        label = "",
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
            }

        }
    }
}

@Composable
fun MedicationCard(
    data: String
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 5.dp, top = 2.dp, end = 5.dp)
            .clickable { }
    ){
        Row (
        ){
            Box (
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(10.dp)
                    .fillMaxWidth()
            ){
                Row {
                    Text(
                        text = "08.00",
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                    Text(
                        text = "Enalapril",
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                    Text(
                        text = "1 tablett",
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                    Text(
                        text = "5mg",
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                }

            }
        }
    }
}

@Composable
private fun MedicationsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSignOut: () -> Unit,
    onAdd: () -> Unit,
    navigateSettings: () -> Unit
) {
    val menuItems = listOf(
        MenuItemData(
            title = stringResource(R.string.add),
            icon = Icons.Outlined.Add,
            onClick = onAdd
        ),
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
@Preview
fun MedicationListScreenPreview() {
    MedicationListScreen{}

}