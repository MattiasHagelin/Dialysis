package com.math3249.dialysis.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.math3249.dialysis.ui.components.model.MenuItemData

@Composable
fun DialysisDropDownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    menuItems: List<MenuItemData>
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        menuItems.forEach { item ->
            MenuItem(
                title = item.title,
                icon = item.icon,
                onClick = item.onClick
            )
        }
    }
}

@Composable
private fun MenuItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(5.dp)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(5.dp)
                )
            }
        },
        onClick = {
            onClick()
        },
        modifier = Modifier
            .sizeIn(
                minWidth = 175.dp,
                maxWidth = 175.dp
            )
    )
}