package com.math3249.dialysis.medications.presentation.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.math3249.dialysis.R
import com.math3249.dialysis.ui.components.DialysisDropDownMenu
import com.math3249.dialysis.ui.components.model.MenuItemData
import com.math3249.dialysis.ui.swipe.SwipeBothDirectionContainer
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MedicationListScreen(
    navigateAdd: () -> Unit
//    medicationListViewModel: MedicationListViewModel,
//    onSignOut: () -> Unit

) {
    val expanded = MutableStateFlow(false)
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
                SwipeBothDirectionContainer(
                    item = item,
                    onEndToStart = {
                        items -= item
                    },
                    onStartToEnd = {
                        Log.i("MedicationList", "Medication Paused")
                        items -= item
                    }
                ) {
                    MedicationCard(data = item)
                }
            }
        }
        Row (
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
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