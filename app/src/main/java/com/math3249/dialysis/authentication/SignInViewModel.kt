package com.math3249.dialysis.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.authentication.data.ISessionData
import com.math3249.dialysis.navigation.NavigateEvent
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.session.ISessionCache
import com.math3249.dialysis.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val sessionCache: ISessionCache,
    private val repository: ISessionData,
    private val onNavigateEvent: (NavigateEvent) -> Unit
): ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    init {

    }

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.LoadRequiredData -> {loadRequiredData(event.value) { result ->
                if (result) {
                    onNavigateEvent(NavigateEvent.ToStart)
                } else {
                    onNavigateEvent(NavigateEvent.ToFirstTimeSignIn)
                }
            }}
            is SignInEvent.OnSignInResult -> onSignInResult(event.value)
            is SignInEvent.ResetState -> resetState()
            is SignInEvent.UpdateShareDataToken -> _state.update { it.copy(shareDataToken = event.value) }
            is SignInEvent.CreateRequiredData -> {
                if (_state.value.shareDataToken.isNotBlank()) {
                    joinGroup(event.value.getSignedInUser(), _state.value.shareDataToken) { result ->
                        if (result) {
                            onNavigateEvent(NavigateEvent.ToStart)
                        } else {
                            //TODO SignOut
                        }
                    }
                } else {
                    createRequiredData(event.value) { result ->
                        if (result) {
                            onNavigateEvent(NavigateEvent.ToStart)
                        } else {
                            //TODO SignOut
                        }
                    }
                }
            }
        }
    }

    private fun onSignInResult(result: SignInResult) {
        _state.update {it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    private fun resetState() {
        _state.update { SignInState() }
    }

    private fun joinGroup(user: UserData?, shareDataToken: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (user != null ){
                repository.joinGroup(
                    userId = user.userId,
                    groupId = shareDataToken
                ) {
                    createSession(user, it)
                    viewModelScope.launch {
                        repository.createRequiredData(
                            userId = user.userId,
                            groupId = shareDataToken
                        ) { success ->
                            callback(success)
                        }
                    }
                }
            }
        }
    }
    private fun createRequiredData(googleAuthUiClient: GoogleAuthUiClient, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = googleAuthUiClient.getSignedInUser()
            if (user != null) {
                repository.createGroupKey(user.userId) {
                    if (it.isNotBlank()) {
                        createSession(user, it)
                        viewModelScope.launch {
                            repository.createRequiredData(
                                userId = user.userId,
                                groupId = it
                            ) { success ->
                                callback(success)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createSession(user: UserData, groupId: String) {
        sessionCache.saveSession(
            Session(
                userId = user.userId,
                email = user.email ?: "",
                groupId = groupId
            )
        )
    }

    private fun loadRequiredData(googleAuthUiClient: GoogleAuthUiClient, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = googleAuthUiClient.getSignedInUser()
            createSessionFromDatabase(user) {
                if (it) {
                    val session = sessionCache.getActiveSession()
                    if (session != null) {
                        viewModelScope.launch {
                            repository.loadRequiredData(user!!.userId, session.groupId) { requiredDataSuccessfully ->
                                if (requiredDataSuccessfully) {
                                    Log.i(Constants.DIALYSIS_FIREBASE, "Data loaded successfully.")
                                    callback(true)
                                } else {
                                    Log.e(Constants.DIALYSIS_FIREBASE, "Failed to load required data.")
                                    callback(false)
                                }
                            }
                        }
                    } else {
                        Log.e(Constants.DIALYSIS_FIREBASE, "Missing session")
                        callback(false)
                    }
                } else {
                    Log.e(Constants.DIALYSIS_FIREBASE, "Failed creating session")
                    callback(false)
                }

            }
        }
    }

    private suspend fun createSessionFromDatabase(
        user: UserData?,
        callback: (Boolean) -> Unit
    ) {
        if (user != null) {
            repository.getGroupKey(user.userId) {
                if (it != "") {
                    createSession(user, it)
                    callback(true)
                } else {
                    Log.e(Constants.DIALYSIS_FIREBASE, "Missing groupId.")
                    callback(false)
                }
            }
        } else {
            Log.e(Constants.DIALYSIS_FIREBASE, "Missing user")
            callback(false)
        }
    }
}
