@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.math3249.dialysis.ui.swipe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
       mutableStateOf(false)
    }
    val state = rememberDismissState(
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = state,
            modifier = Modifier
                .padding(vertical = 4.dp),
            background = {
                val direction = state.dismissDirection ?: return@SwipeToDismiss
                val color by animateColorAsState(
                    when (state.targetValue) {
                        DismissValue.Default -> Color.LightGray
                        DismissValue.DismissedToEnd -> Color.Green
                        DismissValue.DismissedToStart -> Color.Red
                    }, label = ""
                )
                val alignment = when (direction) {
                    DismissDirection.StartToEnd -> Alignment.CenterStart
                    DismissDirection.EndToStart -> Alignment.CenterEnd
                }

                val icon = when (direction) {
                    DismissDirection.StartToEnd -> Icons.Outlined.Pause
                    DismissDirection.EndToStart -> Icons.Outlined.Delete
                }
                val scale by animateFloatAsState(
                    if (state.targetValue == DismissValue.Default) 0.75f else 1f,
                    label = ""
                )

                Box(
                    contentAlignment = alignment,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                        .padding(horizontal = 20.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .scale(scale)
                    )
                }
            },


//                DeleteBackground(
//                    swipeDismissState = state
//                ), label = ""
//            },
            dismissContent = {
                Card (
                    elevation = CardDefaults.cardElevation(animateDpAsState(
                        if (state.dismissDirection != null) 4.dp else 0.dp,
                        label = ""
                    ).value)
                ) {
                    content(item)
                } },
            directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart)
        )
    }
}

@Composable
fun <T> SwipeBothDirectionContainer(
    item: T,
    onEndToStart: (T) -> Unit,
    onStartToEnd: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    var isPaused by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                isPaused = true
                true
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onEndToStart(item)
        }
    }

    LaunchedEffect(key1 = isPaused) {
        if (isPaused) {
            delay(animationDuration.toLong())
            onStartToEnd(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved && !isPaused,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = state,
            modifier = Modifier
                .padding(vertical = 4.dp),
            background = {
                val direction = state.dismissDirection ?: return@SwipeToDismiss
                val color by animateColorAsState(
                    when (state.targetValue) {
                        DismissValue.Default -> Color.LightGray
                        DismissValue.DismissedToEnd -> Color(188,212,212)
                        DismissValue.DismissedToStart -> Color.Red
                    }, label = ""
                )
                val alignment = when (direction) {
                    DismissDirection.StartToEnd -> Alignment.CenterStart
                    DismissDirection.EndToStart -> Alignment.CenterEnd
                }

                val icon = when (direction) {
                    DismissDirection.StartToEnd -> Icons.Outlined.Pause
                    DismissDirection.EndToStart -> Icons.Outlined.Delete
                }
                val scale by animateFloatAsState(
                    if (state.targetValue == DismissValue.Default) 0.75f else 1f,
                    label = ""
                )

                Box(
                    contentAlignment = alignment,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                        .padding(horizontal = 20.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .scale(scale)
                    )
                }
            },
            dismissContent = {
                Card (
                    elevation = CardDefaults.cardElevation(animateDpAsState(
                        if (state.dismissDirection != null) 4.dp else 0.dp,
                        label = ""
                    ).value)
                ) {
                    content(item)
                } },
            directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart)
        )
    }
}