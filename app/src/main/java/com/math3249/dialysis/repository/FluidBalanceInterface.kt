package com.math3249.dialysis.repository

import com.math3249.dialysis.data.model.FluidBalance
import kotlinx.coroutines.flow.Flow

interface FluidBalanceInterface {
    suspend fun getFluidBalance(groupKey: String): Flow<Result<FluidBalance>>
    suspend fun addConsumedFluid(data: FluidBalance, groupKey: String)
    suspend fun resetFluidBalance(groupKey: String)
    suspend fun updateFluidLimit(data: FluidBalance, groupKey: String)
    suspend fun getGroupKey(user: String, groupMemberCallback: GroupMemberCallback)
}