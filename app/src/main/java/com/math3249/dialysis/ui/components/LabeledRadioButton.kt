package com.math3249.dialysis.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
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
fun LabeledRadioButton(
    label: String = "",
    value: Int = 0,
    fontSize: TextUnit = 18.sp,
    selected: Boolean = false,
    onStateChange: (Int) -> Unit = { }
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                role = Role.RadioButton,
                onClick = {
                    onStateChange(value)
                }
            )
            .padding(start = 2.dp, end = 2.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = null
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
fun LabeledRadioButtonPreview() {
    LabeledRadioButton(
        label = "label",
        selected = true,
        onStateChange = {}
    )
}