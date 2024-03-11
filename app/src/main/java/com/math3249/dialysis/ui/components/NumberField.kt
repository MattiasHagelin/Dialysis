package com.math3249.dialysis.ui.components

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun NumberField(
    value: String = "",
    label: @Composable () -> Unit = {},
    onValueChanged: (String) -> Unit = {},
    singleLine: Boolean = false,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = MaterialTheme.colorScheme.background
    ),
    onDone: KeyboardActionScope.() -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    modifier: Modifier
) {

    TextField(
        value = value,
        colors = colors,
        label = label,
        onValueChange = { onValueChanged(it) },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions(
            onDone = onDone
        ),
        modifier = modifier,
        trailingIcon = trailingIcon
    )
}