package com.math3249.dialysis.authentication.data

interface ISessionData {
    suspend fun getGroupKey(userId: String, callback: (String) -> Unit)
    suspend fun createGroupKey(userId: String, callback: (String) -> Unit)
    suspend fun joinGroup(userId: String, groupId: String, callback: (String) -> Unit)
    suspend fun createRequiredData(userId: String, groupId: String, callback: (Boolean) -> Unit)
    suspend fun loadRequiredData(userId: String, groupId: String, callback: (Boolean) -> Unit)
}