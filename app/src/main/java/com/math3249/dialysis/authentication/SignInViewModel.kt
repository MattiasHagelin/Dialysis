package com.math3249.dialysis.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.authentication.data.ISessionData
import com.math3249.dialysis.session.ISessionCache
import com.math3249.dialysis.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val sessionCache: ISessionCache,
    private val repository: ISessionData
): ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    init {

    }

    fun onSignInResult(result: SignInResult) {
        _state.update {it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    fun createSession(googleAuthUiClient: GoogleAuthUiClient) {
        viewModelScope.launch {
            val user = googleAuthUiClient.getSignedInUser()
            if (user != null) {
                repository.getGroupKey(user.userId) {
                    sessionCache.saveSession(
                        Session(
                            userId = user.userId,
                            email = user.email ?: "",
                            groupId = it
                        )
                    )
                }
            }
        }
    }
}
