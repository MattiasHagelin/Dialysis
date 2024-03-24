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
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.math3249.dialysis.R
import com.math3249.dialysis.dialysis.data.DialysisEntry
import com.math3249.dialysis.dialysis.data.DialysisProgram
import com.math3249.dialysis.dialysis.domain.DialysisEvent
import com.math3249.dialysis.dialysis.presentation.DialysisUiState
import com.math3249.dialysis.navigation.NavigateEvent
import com.math3249.dialysis.navigation.Screen
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
    onNavigate: (NavigateEvent) -> Unit,
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
                onNavigate(NavigateEvent.ToPrevious(Screen.StartScreen.route))
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
                    onNavigate = onNavigate
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
                            onNavigate(NavigateEvent.ToDialysisEntryScreen)
                        },
                        deleteAction = {
                            onEvent(DialysisEvent.UpdateItemKey(listItem.key))
                            deleteDialogState.show()
                        },
                        modifier = Modifier
                            .padding(2.dp)
                            .shadow(1.dp, RoundedCornerShape(2.dp))
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
                        onNavigate(NavigateEvent.ToDialysisEntryScreen)
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
    onNavigate: (NavigateEvent) -> Unit
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
            onClick = {onNavigate(NavigateEvent.ToSignIn)}
        )
    )
    DialysisDropDownMenu(
        expanded = expanded,
        onDismiss = onDismiss,
        menuItems = menuItems
    )
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
                text = item.program?.time ?: "",
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
                            colors = if (item.program == null) {
                                listOf(Color.Transparent)
                            } else {
                                item.program!!.dialysisFluids.map {
                                    Color(it.toColorInt())
                                }
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
        onEvent = {},
        onNavigate = {},
        modifier = Modifier)
}