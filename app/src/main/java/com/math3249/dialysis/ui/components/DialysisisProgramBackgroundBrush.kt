package com.math3249.dialysis.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun dialysisProgramBackgroundBrush(
    isVerticalGradient: Boolean = false,
    colors: List<Color>
): Brush {
    val endOffset = if (isVerticalGradient)
        Offset(0f, Float.POSITIVE_INFINITY)
    else
        Offset(Float.POSITIVE_INFINITY, 0f)

    return when (colors.size) {
        1 -> {
            Brush.linearGradient(
                0.0f to colors[0],
                1.0f to colors[0],
                start = Offset(0f, 0f),
                end = endOffset
            )
        }
        2 -> {
            Brush.linearGradient(
                0.0f to colors[0],
                0.5f to colors[0],
                0.51f to colors[1],
                1.0f to colors[1],
                start = Offset(0f, 0f),
                end = endOffset
            )
        }
        else -> {
            Brush.linearGradient(
                colors,
                start = Offset(0f,0f),
                end = endOffset
            )
        }
    }
        
}