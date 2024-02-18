package com.math3249.dialysis.data.repository.repository_interface

import kotlinx.coroutines.flow.Flow

interface UserInterface {
    suspend fun getGroupKey(user: String): Flow<Result<String>>
    suspend fun addNewGroupMember(user: String)
    suspend fun createNewGroup(user: String)
}