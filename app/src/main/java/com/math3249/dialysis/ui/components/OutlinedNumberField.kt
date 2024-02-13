package com.math3249.dialysis.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedNumberField(
    value: String = "",
    label: @Composable () -> Unit = {},
    onValueChanged: (String) -> Unit = {},
    singleLine: Boolean = false,
    onDone: KeyboardActionScope.() -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    modifier: Modifier
) {

    OutlinedTextField(
        value = value,
        label = label,
        onValueChange = { onValueChanged(it) },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions(
            onDone = onDone
        ),
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        trailingIcon = trailingIcon
    )
}