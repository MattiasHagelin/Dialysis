@file:OptIn(ExperimentalMaterial3Api::class)

package com.math3249.dialysis.ui.swipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DeleteBackground(
    swipeDismissState: DismissState
) {
    val color = if(swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
        Color.Red
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color,
                RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun PauseBackground(
    swipeDismissState: DismissState
) {
    val color = if(swipeDismissState.dismissDirection == DismissDirection.StartToEnd) {
        Color(188,212,212)
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color,
                RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            imageVector = Icons.Outlined.Pause,
            contentDescription = null,
            tint = Color.White
        )
    }
}