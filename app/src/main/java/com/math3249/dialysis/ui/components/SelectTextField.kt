@file:OptIn(ExperimentalFoundationApi::class)

package com.math3249.dialysis.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize

@Composable
fun SelectTextField(
    value: String,
    label: String,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = MaterialTheme.colorScheme.background
    ),
    onValueChange: (String) -> Unit,
    onDismissRequest: (MutableState<Boolean>) -> Unit,
    items: List<@Composable ColumnScope.(MutableState<Boolean>) -> Unit>,
    modifier: Modifier
) {
    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }
    val expanded = remember {
        mutableStateOf(false)
    }
    val icon = if (expanded.value) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown
    Column(
        modifier = modifier
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            colors = colors,
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .then(modifier),
            label = {
                Text(text = label)
            },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            expanded.value = true
                        }
                )
            }
        )

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { onDismissRequest(expanded) },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .background(Color.White)
        ) {
            items.map { item ->
                item(expanded)
            }
        }
    }
}