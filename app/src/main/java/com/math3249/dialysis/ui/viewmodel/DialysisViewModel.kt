package com.math3249.dialysis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.data.model.DialysisEntry
import com.math3249.dialysis.repository.DialysisInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class DialysisViewModel(
    private val repository: DialysisInterface
): ViewModel() {
    private val _entries = MutableStateFlow<MutableList<DialysisEntry>?>(mutableListOf())
    private val _showAddDialog = MutableStateFlow<Boolean>(false)
    private val _showDeleteDialog = MutableStateFlow<Boolean>(false)
    private val _showEditDialog = MutableStateFlow<Boolean>(false)
    private val _itemKey = MutableStateFlow<String>("")


    val entries = _entries.asStateFlow()
    val showAddDialog = _showAddDialog.asStateFlow()
    val showDeleteDialog = _showDeleteDialog.asStateFlow()
    val showEditDialog = _showEditDialog.asStateFlow()
    val itemKey = _itemKey.asStateFlow()

    init {
        getEntries()
    }

    private fun getEntries(){
        viewModelScope.launch {
            repository.getDialysisEntries().collect {
                when {
                    it.isSuccess -> {
                        _entries.value = it.getOrNull()
                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }

    fun getDialysisEntry(key: String): MutableStateFlow<DialysisEntry> {
        TODO("Not yet implemented")
    }

    fun updateDialysisEntry(data: DialysisEntry) {
        viewModelScope.launch {
            repository.updateDialysisEntry(data)
        }
    }

    fun deleteDialysisEntry(key: String) {
        viewModelScope.launch {
            repository.deleteDialysisEntry(key)
        }
    }

    fun addEntry(data: DialysisEntry) {
        viewModelScope.launch {
            val entry = DialysisEntry(
                key = UUID.randomUUID().toString(),
                morningWeight = data.morningWeight,
                eveningWeight = data.eveningWeight,
                ultraFiltration = data.ultraFiltration
            )
            repository.addEntry(entry)
        }
    }

    fun showAddDialog(show: Boolean) {
        _showAddDialog.value = show
    }

    fun showDeleteDialog(show: Boolean) {
        _showDeleteDialog.value = show
    }

    fun showEditDialog(show: Boolean) {
        _showEditDialog.value = show
    }

    fun setItemKey(key: String) {
        _itemKey.value = key
    }
}