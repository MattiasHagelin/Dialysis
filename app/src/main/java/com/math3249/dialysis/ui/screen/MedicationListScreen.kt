package com.math3249.dialysis.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.math3249.dialysis.R
import com.math3249.dialysis.ui.components.DialysisDropDownMenu
import com.math3249.dialysis.ui.components.model.MenuItemData

@Composable
fun MedicationListScreen(
//    medicationListViewModel: MedicationListViewModel,
//    onSignOut: () -> Unit

) {
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
        FloatingActionButton(
            onClick = { /*viewModel.setBooleanData(true, BooleanType.EXPANDED)*/ },
            shape = CircleShape,
            modifier = Modifier
                .padding(top = 20.dp, end = 15.dp, bottom = 15.dp)
        ) {
            Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
            MedicationListMenu(
                expanded = false/*viewModel.expanded.collectAsStateWithLifecycle().value*/,
                onDismiss = {
                    /*viewModel.setBooleanData(false, BooleanType.EXPANDED)*/
                },
                onSignOut = {
                   /* onSignOut()
                    viewModel.setBooleanData(false, BooleanType.EXPANDED)*/
                },
                navigateSettings = {}
            )
        }
    }
}
@Composable
fun MedicationListMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSignOut: () -> Unit,
    navigateSettings: () -> Unit
){
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
fun MedicationCard(
    data: String
) {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ){
        Text(data)
    }
}

@Composable
@Preview
fun MedicationListScreenPreview() {
    MedicationListScreen()
}