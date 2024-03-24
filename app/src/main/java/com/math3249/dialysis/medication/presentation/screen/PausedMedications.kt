package com.math3249.dialysis.medication.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.math3249.dialysis.R
import com.math3249.dialysis.medication.domain.MedicationEvent
import com.math3249.dialysis.medication.presentation.MedicationUiState
import com.math3249.dialysis.navigation.NavigateEvent
import com.math3249.dialysis.navigation.Screen
import com.math3249.dialysis.ui.components.DialysisAppBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PausedMedications(
    state: MedicationUiState,
    onEvent: (MedicationEvent) -> Unit,
    onNavigate: (NavigateEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val context = LocalContext.current
        DialysisAppBar(
            canNavigateBack = true,
            navigateUp = {
                onNavigate(NavigateEvent.ToPrevious(Screen.MedicationOverview.route))
            },
            title = stringResource(R.string.paused_medications),
            menu = { expanded ->
                IconButton(onClick = { expanded.value = true }) {
                    Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
                }
                MedicationsMenu(
                    expanded = expanded.collectAsStateWithLifecycle().value,
                    onDismiss = {
                        expanded.value = false
                    },
                    onEvent = onEvent,
                    onNavigate = onNavigate
                )
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
        ) {
            items(state.pausedMedications) { medication ->
                val activationMessage = stringResource(R.string.activated_message, medication.name)
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .combinedClickable(
                            onClick = {
                                Toast
                                    .makeText(
                                        context,
                                        activationMessage,
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                onEvent(MedicationEvent.TogglePause(medication))
                            },
                            onLongClick = {
//                                onEvent(MedicationEvent.RemoveMedicationMethod(medication.key))
                            }
                        )
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.White)
                ){
                    Text(
                        text = medication.name,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                    )
                }
                Divider(
                    thickness = 1.dp,
                    color = Color.LightGray,
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                )
            }
        }
    }
}