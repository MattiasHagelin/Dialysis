package com.math3249.dialysis.start.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.math3249.dialysis.BaseApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BootCompletedReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            runBlocking {
                launch(Dispatchers.IO) {
                    Log.i("DialysisBootCompleted", "Dialysis knows the system is booted")
                    Log.i("DialysisBootCompleted", "Try to reschedule medication times")
                    BaseApp.medicationScheduler.autoSchedule()
                }
            }
        }
    }
}