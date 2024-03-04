package com.math3249.dialysis.medications.data

import kotlinx.coroutines.flow.Flow

interface IMedication {
    suspend fun getMedications(groupId: String): Flow<Result<MutableList<Medication>>>
    suspend fun updateMedication(medication: Medication, groupId: String)
    suspend fun deleteMedication(key: String, groupId: String)
    suspend fun createMedication(medication: Medication, groupId: String)
}