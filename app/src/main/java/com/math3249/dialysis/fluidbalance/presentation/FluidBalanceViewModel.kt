package com.math3249.dialysis.fluidbalance.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.R
import com.math3249.dialysis.fluidbalance.data.FluidBalance
import com.math3249.dialysis.fluidbalance.data.FluidBalanceHistory
import com.math3249.dialysis.fluidbalance.data.IFluidBalance
import com.math3249.dialysis.fluidbalance.domain.FluidBalanceEvent
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.session.ISessionCache
import com.math3249.dialysis.session.Session
import com.math3249.dialysis.ui.components.model.Category
import com.math3249.dialysis.util.DateTimeHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FluidBalanceViewModel(
    private val repository: IFluidBalance,
    sessionCache: ISessionCache
) :ViewModel(){
    private val _state = MutableStateFlow(FluidBalanceUiState())
    private val _undoList = MutableStateFlow(mutableListOf(""))

    val state = _state.asStateFlow()
    private val session: Session?

    init {
        session = sessionCache.getActiveSession()
//        if (session == null) {
//            onNavigateEvent(NavigateEvent.ToSignInScreen)
//        }

        getFluidBalance()
        getFluidBalanceHistory()
    }

    fun onEvent(event: FluidBalanceEvent) {
        when (event) {
            is FluidBalanceEvent.ClearHistory -> clearHistory()
            is FluidBalanceEvent.UpdateFluidLimit -> updateFluidLimit()
            is FluidBalanceEvent.UpdateEditFluidLimit -> _state.update { it.copy(editFluidLimit = event.value ) }
            is FluidBalanceEvent.UpdateGivenVolume -> _state.update { it.copy(givenVolume = event.value) }
            is FluidBalanceEvent.UpdateShowDialog -> _state.update { it.copy(showDialog = event.value) }
            is FluidBalanceEvent.UpdateOtherText -> _state.update { it.copy(otherText = event.value) }
            is FluidBalanceEvent.UpdateSelectedFluid -> _state.update { it.copy(selectedFluid = event.value) }
            is FluidBalanceEvent.Reset -> {
                reset()
                updateHistory(LocalDateTime.now(), true)
                clear()
            }
            is FluidBalanceEvent.UpdateDrunkVolume -> {
                val timeStamp = LocalDateTime.now()
                addDrunkVolume(timeStamp)
                updateHistory(timeStamp)
                clear()
            }
        }
    }

    private fun getFluidBalanceHistory() {
        viewModelScope.launch {
            repository.getFluidBalanceHistory(session!!.groupId).collect { result ->
                when {
                    result.isSuccess -> {
                        _state.update { it.copy(history = result.getOrThrow()
                            .groupBy { history ->
                               DateTimeHelper.formatIsoDateTimeString(history.drunkTimeStamp, Constants.DATE_PATTERN)
                            }
                            .map { mappedHistory ->
                                Category(
                                    name = mappedHistory.key,
                                    items = mappedHistory.value
                                )
                            }) }
                    }
                    result.isFailure -> {
                        //TODO handle failure
                    }
                }
            }
        }
    }

    private fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory(session!!.groupId)
        }
    }

    private fun getFluidBalance() {
        viewModelScope.launch {
            repository.getFluidBalance(session!!.groupId).collect {
                when {
                    it.isSuccess -> {
                        val data = it.getOrNull()
                        if (data != null) {
                            _state.value = _state.value.copy(
                                fluidLimit = data.fluidLimit,
                                editFluidLimit = data.fluidLimit.toString(),
                                drunkVolume = data.drunkVolume,
                                lastDrunkTime = data.lastDrunkTime,
                                lastDrunkVolume = data.lastDrunkVolume,
                                remainingFluid = data.fluidLimit.minus(data.drunkVolume),
                                volumeUnit = data.volumeUnit
                            )
                        }
                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }

    private fun reset() {
        viewModelScope.launch {
            repository.resetFluidBalance(session!!.groupId)
        }
    }
    private fun clear() {
        _state.value = _state.value.copy(
            givenVolume = "",
            otherText = "",
            selectedFluid = R.string.water,
            volumeUnit = ""
        )
    }
    private fun addDrunkVolume(timeStamp: LocalDateTime) {
        viewModelScope.launch {
            val volume = _state.value.givenVolume.toIntOrNull()
            if (volume != null) {
                repository.updateConsumedVolume(
                    FluidBalance(
                        fluidLimit = _state.value.fluidLimit,
                        drunkVolume = (_state.value.drunkVolume + volume),
                        lastDrunkVolume = _state.value.givenVolume,
                        lastDrunkTime = DateTimeFormatter
                            .ofPattern(Constants.TIME_24_H)
                            .format(timeStamp),
                        volumeUnit = "ml"
                    ),
                    session!!.groupId
                )
            }
        }
    }

    private fun updateHistory(
        timeStamp: LocalDateTime,
        isReset: Boolean = false
    ) {
        viewModelScope.launch {
            if (isReset) {
                val history = FluidBalanceHistory(
                    drunkVolume = "",
                    drunkTimeStamp = DateTimeHelper.dateTimeAsISOString(timeStamp),
                    fluidType = R.string.reset_message,
                    extraText = ""
                )
                repository.updateConsumedHistory(
                    history,
                    session!!.groupId
                )
            } else {
                val history = FluidBalanceHistory(
                    drunkVolume = _state.value.givenVolume,
                    drunkTimeStamp = DateTimeFormatter
                        .ISO_DATE_TIME
                        .format(timeStamp),
                    fluidType = _state.value.selectedFluid,
                    extraText = if (_state.value.selectedFluid == R.string.other) {
                        _state.value.otherText
                    } else {
                        ""
                    }
                )
                repository.updateConsumedHistory(
                    history,
                    session!!.groupId
                )
            }
        }
    }
    private fun updateFluidLimit() {
        viewModelScope.launch {
            val volume = _state.value.editFluidLimit.toIntOrNull()
            if (volume != null) {
                repository.updateFluidLimit(volume, session!!.groupId)
                clear()
//                onNavigateEvent(NavigateEvent.ToPrevious)
            }
        }
    }
}