package com.math3249.dialysis.dialysis.data

import kotlinx.coroutines.flow.Flow

interface IDialysis {
    suspend fun getDialysisEntries(groupKey: String): Flow<Result<MutableList<DialysisEntry>>>
    suspend fun getDialysisEntry(key: String, groupId: String, callback: (DialysisEntry) -> Unit)
    suspend fun updateDialysisEntry(data: DialysisEntry, groupKey: String)
    suspend fun deleteDialysisEntry(key: String, groupKey: String)
    suspend fun addEntry(data: DialysisEntry, groupKey: String)

}
