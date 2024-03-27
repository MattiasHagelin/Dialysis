package com.math3249.dialysis.medication.data

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.math3249.dialysis.MainActivity
import com.math3249.dialysis.other.Constants

class MedicationNotificationService(
    private val context: Context
): IMedicationNotificationService {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    override fun showNotification(intent: Intent?) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        if (intent != null) {
            val notification = NotificationCompat.Builder(context, Constants.MEDICATION_CHANNEL_ID)
                .setSmallIcon(intent.getIntExtra(Constants.SMALL_ICON, 0))
                .setContentTitle(intent.getStringExtra(Constants.TITLE))
                .setContentText(intent.getStringExtra(Constants.TEXT))
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(intent.getIntExtra(Constants.ID, 0), notification)

        }
    }


}