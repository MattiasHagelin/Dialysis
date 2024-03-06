package com.math3249.dialysis

import android.app.Application
import android.content.Context
import com.math3249.dialysis.authentication.di.ISessionDataModule
import com.math3249.dialysis.authentication.di.SessionDataModule
import com.math3249.dialysis.dialysis.di.DialysisModule
import com.math3249.dialysis.di.FluidBalanceModule
import com.math3249.dialysis.di.UserModule
import com.math3249.dialysis.dialysis.di.IDialysisModule
import com.math3249.dialysis.di.di_interface.IFluidBalanceModule
import com.math3249.dialysis.di.di_interface.IUserModule
import com.math3249.dialysis.medication.di.IMedicationModule
import com.math3249.dialysis.medication.di.MedicationModule
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.session.SessionCache


class BaseApp: Application() {
    companion object {
        lateinit var dialysisModule: IDialysisModule
        lateinit var fluidBalanceModule: IFluidBalanceModule
        lateinit var medicineModule: IMedicationModule
        lateinit var userModule: IUserModule
        lateinit var sessionDataModule: ISessionDataModule
        lateinit var sessionCache: SessionCache
    }

    override fun onCreate() {
        super.onCreate()
        dialysisModule = DialysisModule(this)
        fluidBalanceModule = FluidBalanceModule(this)
        medicineModule = MedicationModule(this)
        userModule = UserModule(this)
        sessionDataModule = SessionDataModule(this)
        sessionCache = SessionCache(getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE))
    }


}