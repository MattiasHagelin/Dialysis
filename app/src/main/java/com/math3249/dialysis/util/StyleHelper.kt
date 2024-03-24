package com.math3249.dialysis.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun styling(): Modifier {
    return Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(5.dp))
        .background(MaterialTheme.colorScheme.background)
        .height(50.dp)
        .padding(horizontal = 5.dp, vertical = 2.dp)
}

@Composable
fun textFieldColors() : TextFieldColors{
    return TextFieldDefaults.colors(
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
    )
}