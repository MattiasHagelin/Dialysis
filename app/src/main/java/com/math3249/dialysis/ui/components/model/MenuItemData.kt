package com.math3249.dialysis.ui.components.model

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItemData(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)
