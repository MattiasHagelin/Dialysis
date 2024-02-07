package com.math3249.dialysis.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopAppBar(
    modifier: Modifier,
    title: String
) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(start = 5.dp)
                .align(Alignment.CenterStart)
        )

        IconButton(
            onClick = { /*TODO*/ },
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = "Medication list"
                    )
                }

    }
    /*Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(start = 5.dp)
        )
        Row (
            modifier = Modifier
                .wrapContentSize(),
            horizontalArrangement = Arrangement.End

        ) {
            if (false) {
                IconButton(
                    onClick = { *//*TODO*//* },
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Vaccines,
                        contentDescription = "Medication list"
                    )
                }
            }

            IconButton(
                onClick = { *//*TODO*//* },
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "Medication list"
                )
            }
            if (false) {
                IconButton(
                    onClick = { *//*TODO*//* },
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.WaterDrop,
                        contentDescription = "Water balance"
                    )
                }
            }
        }

    }*/
}

@Composable
@Preview
fun TopAppBarPreview() {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        "Test"
    )
}