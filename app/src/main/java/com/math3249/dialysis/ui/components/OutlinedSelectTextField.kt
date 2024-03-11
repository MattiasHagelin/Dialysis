package com.math3249.dialysis.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun OutlinedSelectTextField(
    value: String,
    label: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    items: List<@Composable ColumnScope.() -> Unit>,
    modifier: Modifier
) {
    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }
    val icon = if (expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = true,
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .onFocusChanged {
                    if (it.hasFocus)
                        onExpandedChange(!expanded)
                },
            label = {
                Text(text = label)
            },
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            onExpandedChange(!expanded)
                        }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier
                .width(with(LocalDensity.current){textFieldSize.width.toDp()})
                .background(Color.White)
        ) {
            items.map { item ->
                item()
            }
        }
    }
}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SelectTextField(
//    value: String,
//    expanded: Boolean,
//    onExpandedChange: (Boolean) -> Unit,
//    onValueChange: (String) -> Unit,
//    onDismissRequest: () -> Unit,
//    items: List<@Composable ColumnScope.() -> Unit>
//) {
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = onExpandedChange,
//        modifier = Modifier
//            .background(Color.White)
//    ) {
//        OutlinedTextField(
//            value = value,
//            onValueChange = onValueChange,
//            readOnly = true,
//            trailingIcon = {
//                Icon(
//                    imageVector = if (expanded) Icons.Outlined.ArrowDropUp else Icons.Outlined.ArrowDropDown,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .clickable {
//                            onExpandedChange(!expanded)
//                        }
//                )
//            },
//            label = {
//                Text(stringResource(R.string.header_select_program))
//            },
//            colors = ExposedDropdownMenuDefaults.textFieldColors(),
//            modifier = Modifier
//                .menuAnchor()
//                .background(Color.White)
//        )
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = onDismissRequest,
//            modifier = Modifier
//                .background(Color.White)
//        ) {
//            items.map { item ->
//                item()
//            }
//        }
//    }
//}