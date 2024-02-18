package com.math3249.dialysis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.BaseApp
import com.math3249.dialysis.data.model.DialysisEntry
import com.math3249.dialysis.data.model.DialysisProgram
import com.math3249.dialysis.other.BooleanType
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.repository.DialysisInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
    private val _expanded = MutableStateFlow(false)
    private val _dialysisPrograms = MutableStateFlow(listOf<DialysisProgram>())
    private val _selectedValue = MutableStateFlow("")
    private val _selectExpanded = MutableStateFlow(false)


    val entries = _entries.asStateFlow()
    val showAddDialog = _showAddDialog.asStateFlow()
    val showDeleteDialog = _showDeleteDialog.asStateFlow()
    val showEditDialog = _showEditDialog.asStateFlow()
    val itemKey = _itemKey.asStateFlow()
    val weightBefore = _weightBefore.asStateFlow()
    val weightAfter = _weightAfter.asStateFlow()
    val ultrafiltration = _ultrafiltration.asStateFlow()
    val expanded = _expanded.asStateFlow()
    val dialysisPrograms = _dialysisPrograms.asStateFlow()
    val selectedValue = _selectedValue.asStateFlow()
    val selectExpanded = _selectExpanded.asStateFlow()

    init {
        getEntries()
    }

    private fun getEntries(){
        viewModelScope.launch {
            repository.getDialysisEntries(BaseApp.groupKey).collect {
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
    fun setPrograms(programs: List<DialysisProgram>) {
        _dialysisPrograms.value = programs
    }

    fun updateDialysisEntry(data: DialysisEntry) {
        viewModelScope.launch {
            repository.updateDialysisEntry(data, BaseApp.groupKey)
        }
    }

    fun deleteDialysisEntry(key: String) {
        viewModelScope.launch {
            repository.deleteDialysisEntry(key, BaseApp.groupKey)
        }
    }

    fun addEntry() {
        viewModelScope.launch {
            val entry = DialysisEntry(
                weightAfter = _weightAfter.value,
                weightBefore = _weightBefore.value,
                ultraFiltration = _ultrafiltration.value,
                program =  _dialysisPrograms.value.first { program ->
                    program.name == _selectedValue.value
                }
            )
            repository.addEntry(entry, BaseApp.groupKey)
        }
    }

    fun setEditData(data: DialysisEntry){
        _weightBefore.value = data.weightBefore
        _weightAfter.value = data.weightAfter
        _ultrafiltration.value = data.ultraFiltration
        _selectedValue.value = data.program!!.name
    }

    fun collectUpdatedData(): DialysisEntry {
        return DialysisEntry(
            key = _itemKey.value,
            weightBefore = _weightBefore.value,
            weightAfter = _weightAfter.value,
            ultraFiltration = _ultrafiltration.value,
            date = _entries.value?.first { item ->
                item.key == _itemKey.value
            }?.date!!,
            program = _dialysisPrograms.value.first { program ->
                program.name == _selectedValue.value
            }
        )
    }

    fun clearData() {
        _weightBefore.value = ""
        _weightAfter.value = ""
        _ultrafiltration.value = ""
    }

    fun setBooleanData(value: Boolean, type: BooleanType) {
        when (type) {
            BooleanType.ADD -> _showAddDialog.value = value
            BooleanType.EDIT -> _showEditDialog.value = value
            BooleanType.DELETE -> _showDeleteDialog.value = value
            BooleanType.EXPANDED -> _expanded.value = value
            BooleanType.SELECT_EXPANDED -> _selectExpanded.value = value

        }
    }

    fun setStringData(value: String, id: String) {
        when (id) {
            Constants.WEIGHT_BEFORE -> _weightBefore.value = value
            Constants.WEIGHT_AFTER -> _weightAfter.value = value
            Constants.ULTRAFILTRATION -> _ultrafiltration.value = value
            Constants.KEY -> _itemKey.value = value
            Constants.SELECTED_ITEM -> _selectedValue.value = value
        }
    }
}