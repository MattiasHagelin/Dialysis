package com.math3249.dialysis.dialysis.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.dialysis.data.DialysisEntry
import com.math3249.dialysis.dialysis.data.DialysisProgram
import com.math3249.dialysis.dialysis.data.IDialysis
import com.math3249.dialysis.dialysis.domain.DialysisEvent
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.session.ISessionCache
import com.math3249.dialysis.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DialysisViewModel(
    private val repository: IDialysis,
    private val sessionCache: ISessionCache
): ViewModel() {
    private val _state = MutableStateFlow(DialysisUiState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            session = sessionCache.getActiveSession() ?: Session()
        ) }
        getEntries()
        getPrograms()
    }

    fun onEvent(event: DialysisEvent) {
        when (event) {
            is DialysisEvent.UpdateItemKey -> _state.update { it.copy(itemKey = event.value) }
            is DialysisEvent.UpdateWeightBefore -> _state.update { it.copy(weightBefore = event.value) }
            is DialysisEvent.UpdateWeightAfter -> _state.update { it.copy(weightAfter = event.value) }
            is DialysisEvent.UpdateUltrafiltration -> _state.update { it.copy(ultrafiltration = event.value) }
            is DialysisEvent.UpdateSelectedProgram -> _state.update { it.copy(selectedProgram = event.value) }
            is DialysisEvent.UpdateExpanded -> _state.update { it.copy(expanded = event.value) }
            is DialysisEvent.DeleteEntry -> deleteDialysisEntry(_state.value.itemKey)
            is DialysisEvent.UpdateDialysisEntry -> updateDialysisEntry()
            is DialysisEvent.GetDialysisEntry -> getDialysisEntry()
            is DialysisEvent.CreateEntry -> addEntry()
            is DialysisEvent.Clear -> clearData()
            is DialysisEvent.Edit -> getDialysisEntry(event.value)
            is DialysisEvent.Add -> newEntry()
            else -> return
        }
    }

    private fun newEntry() {
        clearData()
    }

    private fun getEntries(){
        viewModelScope.launch {
            repository.getDialysisEntries(_state.value.session.groupId).collect { result ->
                when {
                    result.isSuccess -> {
                        _state.update { it.copy(entries = result.getOrThrow()) }
                    }
                    result.isFailure -> {
                        result.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }

    private fun getPrograms() {
        _state.update {
            it.copy(
                programs = mutableListOf(
                    DialysisProgram(
                        name = "GulGrön 13h",
                        time = "13h",
                        noOfCycles = 11,
                        dialysisFluids = listOf(Constants.YELLOW, Constants.GREEN)
                    ),
                    DialysisProgram(
                        name = "Gul 13h",
                        time = "13h",
                        noOfCycles = 11,
                        dialysisFluids = listOf(Constants.YELLOW)
                    ),
                    DialysisProgram(
                        name = "GulGrön 16h",
                        time = "16h",
                        noOfCycles = 13,
                        dialysisFluids = listOf(Constants.YELLOW, Constants.GREEN)
                    ),
                    DialysisProgram(
                        name = "Gul 16h",
                        time = "16h",
                        noOfCycles = 13,
                        dialysisFluids = listOf(Constants.YELLOW)
                    )
                )
            )
        }
    }


    private fun updateDialysisEntry() {
        viewModelScope.launch {
            repository.updateDialysisEntry(
                DialysisEntry(
                    key = _state.value.itemKey,
                    weightBefore = _state.value.weightBefore,
                    weightAfter =  _state.value.weightAfter,
                    ultraFiltration = _state.value.ultrafiltration,
                    program = _state.value.programs.first { it.name == _state.value.selectedProgram },
                    date = _state.value.entries.first { it.key == _state.value.itemKey }.date
                ),
                _state.value.session.groupId
            )
            clearData()
        }
    }

    private fun deleteDialysisEntry(key: String) {
        viewModelScope.launch {
            repository.deleteDialysisEntry(key, _state.value.session.groupId)
        }
    }

    private fun addEntry() {
        viewModelScope.launch {
            val entry = DialysisEntry(
                weightAfter = _state.value.weightAfter,
                weightBefore = _state.value.weightBefore,
                ultraFiltration = _state.value.ultrafiltration,
                program =  _state.value.programs.firstOrNull { program ->
                    program.name == _state.value.selectedProgram
                }
            )
            repository.addEntry(entry, _state.value.session.groupId)
            clearData()
        }
    }


    private fun getDialysisEntry() {
        viewModelScope.launch {
            repository.getDialysisEntry(_state.value.itemKey, _state.value.session.groupId) { dialysisEntry ->
                  _state.update {
                      it.copy(
                          itemKey = dialysisEntry.key,
                          weightBefore = dialysisEntry.weightBefore,
                          weightAfter = dialysisEntry.weightBefore,
                          ultrafiltration = dialysisEntry.ultraFiltration,
                          selectedProgram = dialysisEntry.program!!.name
                      )
                   }
            }
        }
    }
    private fun getDialysisEntry(key: String) {
        _state.update { it.copy(itemKey = key )}
        viewModelScope.launch {
            repository.getDialysisEntry(key, _state.value.session.groupId) { dialysisEntry ->
                _state.update {
                  it.copy(
                      itemKey = dialysisEntry.key,
                      weightBefore = dialysisEntry.weightBefore,
                      weightAfter = dialysisEntry.weightAfter,
                      ultrafiltration = dialysisEntry.ultraFiltration,
                      selectedProgram = dialysisEntry.program?.name ?: ""
                  )
                }
            }
        }
    }

    private fun clearData() {
        _state.update { it.copy(
            weightBefore = "",
            weightAfter = "",
            ultrafiltration = "",
            itemKey = "",
            selectedProgram = ""
        ) }
    }
}