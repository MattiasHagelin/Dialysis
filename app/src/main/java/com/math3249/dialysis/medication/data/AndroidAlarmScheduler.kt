package com.math3249.dialysis.medication.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.math3249.dialysis.R
import com.math3249.dialysis.medication.domain.IMedicationScheduler
import com.math3249.dialysis.medication.domain.MedicationAlarm
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.util.DateTimeHelper
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalTime
import java.time.ZoneId

class AndroidAlarmScheduler(
    private val context: Context,
    private val repository: IMedication
): IMedicationScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    private val notificationTimes = MutableStateFlow<List<MedicationAlarm>>(listOf())
    override fun schedule(item: MedicationAlarm) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(Constants.SMALL_ICON, R.drawable.dialysis)
            putExtra(Constants.TITLE, context.getString(R.string.medication_time, item.message))
            putExtra(Constants.TEXT, context.getString(R.string.medication_text))
            putExtra(Constants.ID, item.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(
                item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                pendingIntent
            ),
            pendingIntent
        )
    }

    override suspend fun autoSchedule() {
        Log.i("AlarmScheduler", "Auto schedule start")
        repository.getMedications().collect() { result ->
            when {
                result.isSuccess -> {
                    notificationTimes.value = result.getOrThrow()
                        .filter { medication ->
                            !medication.paused &&
                                    !DateTimeHelper.hasPassed(medication.lastCompleted)
                        }
                        .sortedBy { it.time }
                        .groupBy { it.time }
                        .keys
                        .map {time ->
                            val dateTime = DateTimeHelper.dateTimeFromTimeString(time)
                            MedicationAlarm(
                                id = LocalTime.of
                                    (
                                        dateTime.hour,
                                        dateTime.minute,
                                        dateTime.second,
                                        dateTime.nano
                                    )
                                    .toSecondOfDay(),
                                time = dateTime,
                                message = time
                            )
                        }
                    notificationTimes.value.forEach {
                        schedule(it)
                    }
                    Log.i("AlarmScheduler", "Auto schedule done")
                }
                result.isFailure -> {
                    Log.e("AlarmScheduler", result.exceptionOrNull()?.message ?: "", result.exceptionOrNull())
                }
            }
        }
    }

    override fun cancel(item: MedicationAlarm) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.id,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}