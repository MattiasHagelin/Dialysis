package com.math3249.dialysis.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LabeledCheckbox(
    label: String,
    fontSize: TextUnit = 18.sp,
    state: Boolean,
    onStateChange: (Boolean) -> Unit
) {

    // Checkbox with text on right side
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
        .clickable(
            role = Role.Checkbox,
            onClick = {
                onStateChange(!state)
            }
        )
            .padding(start = 2.dp, end = 2.dp)
    ) {
        Checkbox(
            checked = state,
            onCheckedChange = null
        )
        Spacer(
            modifier = Modifier
            .width(4.dp)
        )
        Text(
            text = label,
            fontSize = fontSize
            )
    }
}

@Composable
@Preview
fun LabeledChecboxPreview() {
    LabeledCheckbox(
        label = "text",
        fontSize = 12.sp,
        state = true,
        onStateChange = {}
    )
}