package com.math3249.dialysis

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.math3249.dialysis.authentication.di.ISessionDataModule
import com.math3249.dialysis.authentication.di.SessionDataModule
import com.math3249.dialysis.di.UserModule
import com.math3249.dialysis.di.di_interface.IUserModule
import com.math3249.dialysis.dialysis.di.DialysisModule
import com.math3249.dialysis.dialysis.di.IDialysisModule
import com.math3249.dialysis.fluidbalance.di.FluidBalanceModule
import com.math3249.dialysis.fluidbalance.di.IFluidBalanceModule
import com.math3249.dialysis.medication.data.AndroidAlarmScheduler
import com.math3249.dialysis.medication.di.IMedicationModule
import com.math3249.dialysis.medication.di.MedicationModule
import com.math3249.dialysis.medication.domain.IMedicationScheduler
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.session.SessionCache


class BaseApp: Application() {
    companion object {
        lateinit var dialysisModule: IDialysisModule
        lateinit var fluidBalanceModule: IFluidBalanceModule
        lateinit var medicationModule: IMedicationModule
        lateinit var userModule: IUserModule
        lateinit var sessionDataModule: ISessionDataModule
        lateinit var sessionCache: SessionCache
        lateinit var medicationScheduler: IMedicationScheduler
        //lateinit var googleAuthUiClient: GoogleAuthUiClient
    }

    override fun onCreate() {
        super.onCreate()
        dialysisModule = DialysisModule(this)
        fluidBalanceModule = FluidBalanceModule(this)
        medicationModule = MedicationModule(this)
        userModule = UserModule(this)
        sessionDataModule = SessionDataModule(this)
        sessionCache = SessionCache(getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE))
        createMedicationNotificationChannel()
        medicationScheduler = AndroidAlarmScheduler(this, medicationModule.medicationRepository)
    }

    private fun createMedicationNotificationChannel(){
        val channel = NotificationChannel(
            Constants.MEDICATION_CHANNEL_ID,
            "Medication",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = getString(R.string.channel_description)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}