@file:OptIn(ExperimentalFoundationApi::class)

package com.math3249.dialysis.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SelectTextField(
    value: String,
    label: String,
    colors: TextFieldColors = TextFieldDefaults.colors(
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = MaterialTheme.colorScheme.background,
        focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
        //For Icons
        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
    ),
    onValueChange: (String) -> Unit,
    onDismissRequest: (MutableState<Boolean>) -> Unit,
    items: List<@Composable ColumnScope.(MutableState<Boolean>) -> Unit>,
    modifier: Modifier
) {
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
            modifier = modifier
                .clickable { expanded.value = !expanded.value },
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
                .background(MaterialTheme.colorScheme.background)
        ) {
            items.map { item ->
                item(expanded)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenu(
    value: String,
    items: List<@Composable ColumnScope.(MutableState<Boolean>) -> Unit>,
    modifier: Modifier = Modifier,
    label: String = ""
) {
    val expanded = remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = it
        },
        modifier = modifier
            .padding(horizontal = 15.dp)
    ) {
        TextField(
            value = value,
            label = {
                Text(label)
            },
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.background
            ),
            modifier = modifier
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            },
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            items.map { item ->
                item(expanded)
            }
        }
    }

}