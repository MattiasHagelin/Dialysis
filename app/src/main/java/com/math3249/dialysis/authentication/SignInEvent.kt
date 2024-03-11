package com.math3249.dialysis.authentication

sealed class SignInEvent {
    data object ResetState : SignInEvent()
    data class CreateRequiredData(val value: GoogleAuthUiClient) : SignInEvent()
    data class UpdateShareDataToken(val value: String) : SignInEvent()
    data class OnSignInResult(val value: SignInResult) : SignInEvent()
    data class LoadRequiredData(val value: GoogleAuthUiClient) : SignInEvent()
}