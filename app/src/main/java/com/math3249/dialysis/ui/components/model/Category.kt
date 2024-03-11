package com.math3249.dialysis.ui.components.model

data class Category<T>(
    val name: String,
    val items: List<T>
)
