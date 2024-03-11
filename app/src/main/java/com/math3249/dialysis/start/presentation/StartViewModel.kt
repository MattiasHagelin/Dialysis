package com.math3249.dialysis.start.presentation

import androidx.lifecycle.ViewModel
import com.math3249.dialysis.navigation.NavigateEvent
import com.math3249.dialysis.start.domain.StartEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StartViewModel(
    val onNavigateEvent: (NavigateEvent) -> Unit
): ViewModel() {
    private val _state = MutableStateFlow(StartUiState())
    val state = _state.asStateFlow()
    fun onEvent(event: StartEvent) {
        when (event) {
            is StartEvent.SignOut -> onNavigateEvent(NavigateEvent.ToSignIn)
            is StartEvent.SeeFluidBalance -> onNavigateEvent(NavigateEvent.ToFluidBalance)
            is StartEvent.SeeDialysis -> onNavigateEvent(NavigateEvent.ToDialysis)
            is StartEvent.SeeMedications -> onNavigateEvent(NavigateEvent.ToMedications)
            is StartEvent.SeeBloodPressure -> onNavigateEvent(NavigateEvent.ToBloodPressure)
            is StartEvent.Share -> {/*TODO send mail to given mail address*/}
            is StartEvent.UpdateShareToEmail -> _state.update { it.copy(shareToEmail = event.value) }
        }
    }
}