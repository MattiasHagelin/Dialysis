package com.math3249.dialysis.data.repository.repository_interface

import com.math3249.dialysis.data.model.Medicine
import kotlinx.coroutines.flow.Flow

interface IMedications {
    suspend fun getMedications(groupId: String): Flow<Result<MutableList<Medicine>>>
    suspend fun updateMedicine(medicine: Medicine, groupId: String)
    suspend fun deleteMedicine(key: String, groupId: String)
    suspend fun createMedicine(medicine: Medicine, groupId: String)
}