package com.math3249.dialysis.data.repository.repository_interface

interface IUser {
    suspend fun getGroupKey(user: String, callback: (String) -> Unit)
    suspend fun addNewGroupMember(user: String)
    suspend fun createMemberGroup(user: String, callback: (String) -> Unit)
    suspend fun createNewGroup(user: String)
}