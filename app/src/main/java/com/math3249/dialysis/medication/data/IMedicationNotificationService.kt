package com.math3249.dialysis.medication.data

import android.content.Intent

interface IMedicationNotificationService {
    fun showNotification(intent: Intent?)
}