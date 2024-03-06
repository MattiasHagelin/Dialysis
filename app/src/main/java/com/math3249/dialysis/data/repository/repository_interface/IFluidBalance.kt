package com.math3249.dialysis.data.repository.repository_interface

import com.math3249.dialysis.data.model.FluidBalance
import com.math3249.dialysis.data.model.GroupMember
import kotlinx.coroutines.flow.Flow

interface IFluidBalance {
    suspend fun getFluidBalance(groupKey: String): Flow<Result<FluidBalance>>
    suspend fun addConsumedFluid(data: FluidBalance, groupKey: String)
    suspend fun resetFluidBalance(groupKey: String)
    suspend fun updateFluidLimit(data: FluidBalance, groupKey: String)
    suspend fun getGroupKey(user: String, callback: (GroupMember) -> Unit)
}