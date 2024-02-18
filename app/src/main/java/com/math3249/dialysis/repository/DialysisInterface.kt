package com.math3249.dialysis.repository

import com.math3249.dialysis.data.model.DialysisEntry
import kotlinx.coroutines.flow.Flow

interface DialysisInterface {
    suspend fun getDialysisEntries(groupKey: String): Flow<Result<MutableList<DialysisEntry>>>
    suspend fun updateDialysisEntry(data: DialysisEntry, groupKey: String)
    suspend fun deleteDialysisEntry(key: String, groupKey: String)
    suspend fun addEntry(data: DialysisEntry, groupKey: String)

}
