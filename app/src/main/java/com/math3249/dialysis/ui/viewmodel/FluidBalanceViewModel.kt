package com.math3249.dialysis.ui.viewmodel

import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.data.model.FluidBalance
import com.math3249.dialysis.data.model.GroupMember
import com.math3249.dialysis.data.repository.repository_interface.IFluidBalance
import com.math3249.dialysis.session.ISessionCache
import com.math3249.dialysis.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FluidBalanceViewModel(
    private val repository: IFluidBalance,
    private val sessionCache: ISessionCache
) :ViewModel(){
    private val _fluidBalance = MutableStateFlow<FluidBalance?>(FluidBalance())
    private val _editFluidLimit = MutableStateFlow("")
    private val _consumedFluid = MutableStateFlow("")
    private val _focusRequester = MutableStateFlow(FocusRequester())
    private val _groupKey = MutableStateFlow(GroupMember())
    private val _showDialog = MutableStateFlow(false)

    val fluidBalance = _fluidBalance.asStateFlow()
    val editFluidLimit = _editFluidLimit.asStateFlow()
    val consumedFluid = _consumedFluid.asStateFlow()
    val focusRequester = _focusRequester.asStateFlow()
    val showDialog = _showDialog.asStateFlow()
    private val session: Session

    init {
        session = sessionCache.getActiveSession() ?: Session(
            userId = "",
            email = "",
            groupId = ""
        )
        getFluidBalance()
    }

    private fun getFluidBalance() {
        viewModelScope.launch {
            repository.getFluidBalance(session.groupId).collect {
                when {
                    it.isSuccess -> {
                        _fluidBalance.value = it.getOrNull()
                        _editFluidLimit.value = _fluidBalance.value?.fluidLimit.toString()
                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            repository.resetFluidBalance(session.groupId)
        }
    }

    fun addConsumedFluid(volume: String) {
        viewModelScope.launch {
            val volumeAsInt = volume.toIntOrNull()
            if (fluidBalance.value != null &&
                volumeAsInt != null) {
                fluidBalance.value!!.consumedFluid += volumeAsInt
                repository.addConsumedFluid(fluidBalance.value!!, session.groupId)
            }
        }
    }
    fun setConsumedFluid(volume: String) {
        _consumedFluid.value = volume
    }

    fun updateFluidLimit(volume: String) {
        viewModelScope.launch {
            val volumeAsInt = volume.toIntOrNull()
            if (volumeAsInt != null
                && _fluidBalance.value != null) {
                _fluidBalance.value!!.fluidLimit = volumeAsInt
                repository.updateFluidLimit(fluidBalance.value!!, session.groupId)
            }

        }
    }
    fun setEditFluidLimit(volume: String) {
        _editFluidLimit.value = volume
    }

    fun showDialog(show: Boolean) {
        _showDialog.value = show
    }


}