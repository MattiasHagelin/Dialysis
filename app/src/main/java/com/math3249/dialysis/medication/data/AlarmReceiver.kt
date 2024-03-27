package com.math3249.dialysis.medication.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            MedicationNotificationService(context).showNotification(intent)
        } else {
            Log.e("DialysisError", "Failed to create notification")
        }
    }
}