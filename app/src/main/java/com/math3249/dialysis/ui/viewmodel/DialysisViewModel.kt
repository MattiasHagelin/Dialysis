package com.math3249.dialysis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.data.model.DialysisEntry
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.other.DialogType
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
    private val _weightBefore = MutableStateFlow("")
    private val _weightAfter = MutableStateFlow("")
    private val _ultrafiltration = MutableStateFlow("")


    val entries = _entries.asStateFlow()
    val showAddDialog = _showAddDialog.asStateFlow()
    val showDeleteDialog = _showDeleteDialog.asStateFlow()
    val showEditDialog = _showEditDialog.asStateFlow()
    val itemKey = _itemKey.asStateFlow()
    val weightBefore = _weightBefore.asStateFlow()
    val weightAfter = _weightAfter.asStateFlow()
    val ultrafiltration = _ultrafiltration.asStateFlow()

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

//    fun showAddDialog(show: Boolean) {
//        _showAddDialog.value = show
//    }
//
//    fun showDeleteDialog(show: Boolean) {
//        _showDeleteDialog.value = show
//    }
//
//    fun showEditDialog(show: Boolean) {
//        _showEditDialog.value = show
//    }

    fun showDialog(value: Boolean, type: DialogType) {
        when (type) {
            DialogType.ADD -> _showAddDialog.value = value
            DialogType.EDIT -> _showAddDialog.value = value
            DialogType.DELETE -> _showDeleteDialog.value = value
        }
    }

    fun setStringData(value: String, id: String) {
        when (id) {
            Constants.WEIGHT_BEFORE -> _weightBefore.value = value
            Constants.WEIGHT_AFTER -> _weightAfter.value = value
            Constants.ULTRAFILTRATION -> _ultrafiltration.value = value
            Constants.KEY -> _itemKey.value = value
        }
    }
}