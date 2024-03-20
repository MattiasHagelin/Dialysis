package com.math3249.dialysis.medication.data

import kotlinx.coroutines.flow.Flow

interface IMedication {
    suspend fun getMedications(groupId: String): Flow<Result<MutableList<Medication>>>
    suspend fun getMedication(key: String, groupId: String, callback: (Medication) -> Unit)
    suspend fun updateMedication(medication: Medication, groupId: String)
    suspend fun deleteMedication(key: String, groupId: String)
    suspend fun createMedication(medications: List<Medication>, groupId: String)
}