package com.math3249.dialysis.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
    onStateChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Checkbox with text on right side
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                role = Role.Checkbox,
                onClick = {
                    onStateChange(!state)
                }
            )
            .height(50.dp)
    ) {
        Checkbox(
            checked = state,
            onCheckedChange = null,
            modifier = modifier
                .shadow(0.dp, RoundedCornerShape(4.dp))
        )
        Spacer(
            modifier = Modifier
            .width(5.dp)
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