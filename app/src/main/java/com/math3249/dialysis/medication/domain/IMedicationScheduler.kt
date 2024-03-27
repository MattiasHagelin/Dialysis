package com.math3249.dialysis.medication.domain

interface IMedicationScheduler {
    fun schedule(item: MedicationAlarm)
   suspend fun autoSchedule()
    fun cancel(item: MedicationAlarm)
}