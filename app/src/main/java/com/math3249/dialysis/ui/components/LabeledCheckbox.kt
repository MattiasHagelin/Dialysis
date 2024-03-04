package com.math3249.dialysis.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun LabeledCheckbox(
    label: String,
    state: Boolean,
    onStateChange: (Boolean) -> Unit
) {

    // Checkbox with text on right side
    Row(modifier = Modifier
        .padding(8.dp)
        .clickable(
            role = Role.Checkbox,
            onClick = {
                onStateChange(!state)
            }
        )
        .padding(8.dp)
    ) {
        Checkbox(
            checked = state,
            onCheckedChange = null
        )
        Spacer(
            modifier = Modifier
            .width(8.dp)
        )
        Text(
            text = label,
            modifier = Modifier
            )
    }
}