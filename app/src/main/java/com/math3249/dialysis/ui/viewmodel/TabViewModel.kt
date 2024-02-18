package com.math3249.dialysis.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TabViewModel: ViewModel() {
    private val _selectedTabIndex = MutableStateFlow(0)

    val selectedTabIndex = _selectedTabIndex.asStateFlow()


    fun setSelectedTabIndex(index: Int) {
        _selectedTabIndex.value = index
    }
}