package com.math3249.dialysis.ui.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.sizeIn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.outlined.ArrowBack
//import androidx.compose.material.icons.outlined.Clear
//import androidx.compose.material.icons.outlined.Logout
//import androidx.compose.material.icons.outlined.MedicalInformation
//import androidx.compose.material.icons.outlined.Menu
//import androidx.compose.material.icons.outlined.Settings
//import androidx.compose.material.icons.outlined.Vaccines
//import androidx.compose.material.icons.outlined.WaterDrop
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import com.math3249.dialysis.R
//import com.math3249.dialysis.other.Constants
//import com.math3249.dialysis.other.NavigationHelper
//import kotlinx.coroutines.flow.MutableStateFlow
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DialysisAppBar(
//    navController: NavHostController,
//    canNavigateBack: Boolean,
//    navigateUp: () -> Unit,
//    title: String,
//    onSignOut: () -> Unit,
//    currentLocation: String,
//    showResetButton: Boolean = false,
//    onReset: () -> Unit
//){
//    val expanded = MutableStateFlow(false)
//    TopAppBar(
//        title = {
//            Text(title)
//        },
//        colors = TopAppBarDefaults.mediumTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer
//        ),
//        modifier = Modifier
//            .shadow(10.dp, RoundedCornerShape(8.dp))
//            .background(MaterialTheme.colorScheme.secondaryContainer),
//        navigationIcon = {
//            if (canNavigateBack) {
//                IconButton(
//                    onClick = navigateUp
//                ) {
//                    Icon(
//                        imageVector = Icons.Outlined.ArrowBack,
//                        contentDescription = stringResource(R.string.icon_arrow_back)
//                    )
//                }
//            }
//        },
//        actions = {
//            if  (showResetButton) {
//                IconButton(
//                    onClick = onReset
//                ) {
//                    Icon(
//                        imageVector = Icons.Outlined.Clear,
//                        contentDescription = "Clear")
//                }
//            }
//            IconButton(
//                onClick = {
//                    expanded.value = true
//                }) {
//                Icon(
//                    imageVector = Icons.Outlined.Menu,
//                    contentDescription = stringResource(R.string.icon_menu)
//                )
//                DialysisDropdownMenu(
//                    currentLocation = currentLocation,
//                    navController = navController,
//                    onDismissRequest = {
//                        expanded.value = false
//                    },
//                    expanded = expanded
//                )
//            }
//            IconButton(
//                onClick = onSignOut
//            ) {
//                Icon(
//                    imageVector = Icons.Outlined.Logout,
//                    contentDescription = stringResource(R.string.icon_sign_out)
//                )
//            }
//        }
//    )
//}
//
//@Composable
//private fun DialysisDropdownMenu(
//    currentLocation: String,
//    navController: NavHostController,
//    onDismissRequest: () -> Unit,
//    expanded: MutableStateFlow<Boolean>
//) {
//    DropdownMenu(
//        expanded = expanded.collectAsState().value,
//        onDismissRequest = onDismissRequest
//    ) {
//        val navigation = listOf(
//            NavigationHelper(
//                location = Constants.DIALYSIS,
//                icon = Icons.Outlined.MedicalInformation,
//                iconText = stringResource(R.string.icon_dialysis)
//            ),
//            NavigationHelper(
//                location = Constants.MEDICATION_LIST,
//                icon = Icons.Outlined.Vaccines,
//                iconText = stringResource(R.string.icon_medication_list)
//            ),
//            NavigationHelper(
//                location = Constants.FLUID_BALANCE,
//                icon = Icons.Outlined.WaterDrop,
//                iconText = stringResource(R.string.icon_fluid_balance)
//            ),
//            NavigationHelper(
//                location = Constants.SETTINGS,
//                icon = Icons.Outlined.Settings,
//                iconText = stringResource(R.string.icon_settings)
//            )
//        )
//        navigation.forEach {
//            if (it.location != currentLocation) {
//                DropdownMenuItem(
//                    text = {
//                        Box (
//                            modifier = Modifier
//                                .fillMaxWidth()
//                        ){
//                            Text(
//                                text = it.iconText,
//                                modifier = Modifier
//                                    .align(Alignment.CenterStart)
//                                    .padding(5.dp)
//                            )
//                            Icon(
//                                imageVector = it.icon,
//                                contentDescription = it.iconText,
//                                modifier = Modifier
//                                    .align(Alignment.CenterEnd)
//                                    .padding(5.dp)
//                            )
//                        }
//                    },
//                    onClick = {
//                        navController.navigate(it.location)
//                        expanded.value = false
//                    },
//                    modifier = Modifier
//                        .sizeIn(
//                            minWidth = 175.dp,
//                            maxWidth = 175.dp
//                        )
//                )
//            }
//        }
//    }
//}