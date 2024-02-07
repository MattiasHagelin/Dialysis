package com.math3249.dialysis.repository

import com.math3249.dialysis.data.model.DialysisEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface DialysisInterface {
    suspend fun getDialysisEntries(): Flow<Result<MutableList<DialysisEntry>>>
    suspend fun getDialysisEntry(key: String): MutableStateFlow<DialysisEntry>
    suspend fun updateDialysisEntry(data: DialysisEntry)
    suspend fun deleteDialysisEntry(key: String)
    suspend fun addEntry(data: DialysisEntry)

}
