package com.math3249.dialysis.medication.data

import kotlinx.coroutines.flow.Flow

interface IMedication {
    suspend fun getMedications(): Flow<Result<MutableList<Medication>>>
    suspend fun getMedication(key: String, callback: (Medication) -> Unit)
    suspend fun updateMedication(medication: Medication)
    suspend fun updateRecurrentMedications(medication: Medication, medications: List<Medication>)
    suspend fun deleteMedication(key: String)
    suspend fun deleteRecurrentMedications(medications: List<Medication>)
    suspend fun createMedication(medication: Medication)
}