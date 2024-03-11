@file:OptIn(ExperimentalFoundationApi::class)

package com.math3249.dialysis.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.math3249.dialysis.fluidbalance.data.FluidBalanceHistory
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.ui.components.model.Category
import com.math3249.dialysis.util.DateTimeHelper


@Composable
fun <T> CategorizedLacyColumn(
    categories: List<Category<T>>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        categories.forEach { category ->
            stickyHeader {
                CategoryHeader(category.name)
            }
            items(category.items) { item ->
                CategoryItem(
                    item = item,
                    modifier = Modifier
                        .height(50.dp)
                )
            }
        }
    }
}
@Composable
private fun CategoryHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(15.dp)
    )
}

@Composable
private fun <T> CategoryItem(
    item: T,
    modifier: Modifier = Modifier
) {
    when (item) {
        is FluidBalanceHistory -> FluidBalanceHistoryRow(item, modifier)
        else -> return
    }
}

@Composable
private fun FluidBalanceHistoryRow(
    item: FluidBalanceHistory,
    modifier: Modifier
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .height(50.dp)
            .padding(horizontal = 15.dp)
    ){
        Text(
            text = DateTimeHelper
                .formatIsoDateTimeString(
                    item.drunkTimeStamp,
                    Constants.TIME_24_H
                ) +
                " " + item.drunkVolume +
                " " + item.volumeUnit +
                " " + item.fluidType
            )
    }
}

