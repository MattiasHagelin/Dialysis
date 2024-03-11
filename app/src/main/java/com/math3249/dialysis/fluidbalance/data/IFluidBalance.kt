package com.math3249.dialysis.fluidbalance.data

import com.math3249.dialysis.data.model.GroupMember
import kotlinx.coroutines.flow.Flow

interface IFluidBalance {
    suspend fun getFluidBalance(groupKey: String): Flow<Result<FluidBalance>>
    suspend fun getFluidBalanceHistory(groupKey: String): Flow<Result<MutableList<FluidBalanceHistory>>>
    suspend fun updateConsumedVolume(fluidBalance: FluidBalance, groupKey: String)
    suspend fun updateConsumedHistory(entry: FluidBalanceHistory, groupKey: String)
    suspend fun clearHistory(groupKey: String)
    suspend fun resetFluidBalance(groupKey: String)
    suspend fun updateFluidLimit(volume: Int, groupKey: String)
    suspend fun getGroupKey(user: String, callback: (GroupMember) -> Unit)
}