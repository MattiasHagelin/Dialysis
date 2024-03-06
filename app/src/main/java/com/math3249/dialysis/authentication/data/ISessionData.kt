package com.math3249.dialysis.authentication.data

interface ISessionData {
    suspend fun getGroupKey(userId: String, callback: (String) -> Unit)
}