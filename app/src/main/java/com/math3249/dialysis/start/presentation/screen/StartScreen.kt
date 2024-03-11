package com.math3249.dialysis.start.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MedicalInformation
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.math3249.dialysis.R
import com.math3249.dialysis.start.domain.StartEvent
import com.math3249.dialysis.start.presentation.StartUiState
import com.math3249.dialysis.ui.components.DialysisAppBar
import com.math3249.dialysis.ui.components.DialysisDropDownMenu
import com.math3249.dialysis.ui.components.model.MenuItemData
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.customView
import com.vanpra.composematerialdialogs.iconTitle

@Composable
fun StartScreen(
    state: StartUiState,
    onEvent: (StartEvent) -> Unit
) {
//    val shareDialogState = rememberMaterialDialogState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        DialysisAppBar(
            canNavigateBack = false,
            navigateUp = { },
            title = stringResource(R.string.app_name),
            menu = {    expanded ->
                IconButton(onClick = { expanded.value = true }) {
                    Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
                }
                StartMenu(
                    state = state,
                    expanded = expanded.collectAsStateWithLifecycle().value,
                    onDismiss = { expanded.value = false },
                    onEvent = onEvent
                )
            }
        )
        StartRow(
            onClickAction = { onEvent(StartEvent.SeeFluidBalance) },
            imageVector = Icons.Outlined.WaterDrop,
            text = stringResource(R.string.screen_fluid_balance),
            fontSize = 24.sp
        )
        StartRow(
            imageVector = Icons.Outlined.MedicalInformation,
            text = stringResource(R.string.screen_dialysis),
            fontSize = 24.sp,
            onClickAction = { onEvent(StartEvent.SeeDialysis)}
        )
        StartRow(
            imageVector = Icons.Outlined.Vaccines,
            text = stringResource(R.string.icon_medication_list),
            fontSize = 24.sp,
            onClickAction = { onEvent(StartEvent.SeeMedications) }
        )
        StartRow(
            painter = painterResource(R.drawable.blood_pressure),
            text = stringResource(R.string.blood_pressure),
            fontSize = 24.sp,
            onClickAction = {onEvent(StartEvent.SeeBloodPressure)}
        )
    }
    var text by remember {
        mutableStateOf("")
    }
    MaterialDialog (
        dialogState = state.shareDialogState,
        buttons = {
            positiveButton(
                text = "Share",
                onClick = {
                    onEvent(StartEvent.Share)
                }
            )
        },
        shape = RoundedCornerShape(8.dp)

    ) {

        this.iconTitle ("Share") {
            Icon(imageVector = Icons.Outlined.Share, contentDescription = null)
        }
        this.customView {
            TextField(
                value = state.shareToEmail,
                label = {
                    Text("Email")
                },
                onValueChange = {
                    onEvent(StartEvent.UpdateShareToEmail(it))
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
    }
}

@Composable
fun StartRow(
    onClickAction: () -> Unit = {},
    imageVector: ImageVector? = null,
    painter: Painter? = null,
    text: String = "",
    fontSize: TextUnit = 18.sp
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                onClickAction()
            }
            .fillMaxWidth()
            .background(Color.White)
            .shadow(1.dp)
    ){
        if (imageVector != null) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(32.dp)
            )
        } else if (painter != null){
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(32.dp)
            )
        }
        Text(
            fontSize = fontSize,
            text = text,
            modifier = Modifier
                .padding(5.dp
                )
        )
    }
}

@Composable
private fun StartMenu(
    state: StartUiState,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEvent: (StartEvent) -> Unit,
) {
    val menuItems = listOf(
        MenuItemData(
            title = stringResource(R.string.share),
            icon = Icons.Outlined.Share,
            onClick = {
                state.shareDialogState.show()
            }
        ),
        MenuItemData(
            title = stringResource(R.string.settings),
            icon = Icons.Outlined.Settings,
            onClick = {
                //TODO Implement NavigateEvent
            }
        ),
        MenuItemData(
            title = stringResource(R.string.logout),
            icon = Icons.Outlined.Logout,
            onClick = {onEvent(StartEvent.SignOut)}
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
fun StartScreenPreview() {
    StartScreen(StartUiState()) {

    }
}