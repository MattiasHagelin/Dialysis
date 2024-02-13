package com.math3249.dialysis.ui.viewmodel

import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.BaseApp
import com.math3249.dialysis.data.model.FluidBalance
import com.math3249.dialysis.data.model.GroupMember
import com.math3249.dialysis.repository.FluidBalanceInterface
import com.math3249.dialysis.repository.GroupMemberCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FluidBalanceViewModel(
    private val repository: FluidBalanceInterface
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
    private val groupKey = _groupKey.asStateFlow()

    init {
        getGroupKey()
    }

    private fun getGroupKey() {
        viewModelScope.launch {
            repository.getGroupKey(BaseApp.userData.userId, object: GroupMemberCallback {
                override fun onCallback(value: GroupMember) {
                    _groupKey.value = value
                    if (BaseApp.groupKey.isBlank() && value.groupId.isNotBlank())
                        BaseApp.groupKey = value.groupId
                    getFluidBalance()
                }
            })
        }
    }
    private fun getFluidBalance() {
        viewModelScope.launch {
            repository.getFluidBalance(groupKey.value.groupId).collect {
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
            repository.resetFluidBalance(groupKey.value.groupId)
        }
    }

    fun addConsumedFluid(volume: String) {
        viewModelScope.launch {
            val volumeAsInt = volume.toIntOrNull()
            if (fluidBalance.value != null &&
                volumeAsInt != null) {
                fluidBalance.value!!.consumedFluid += volumeAsInt
                repository.addConsumedFluid(fluidBalance.value!!, groupKey.value.groupId)
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
                repository.updateFluidLimit(fluidBalance.value!!, groupKey.value.groupId)
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