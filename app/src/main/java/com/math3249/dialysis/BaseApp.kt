package com.math3249.dialysis

import android.app.Application
import com.math3249.dialysis.di.DialysisModule
import com.math3249.dialysis.di.DialysisModuleImpl
import com.math3249.dialysis.di.FluidBalanceModule
import com.math3249.dialysis.di.FluidBalanceModuleImpl
import com.math3249.dialysis.di.UserModule
import com.math3249.dialysis.di.UserModuleImpl
import com.math3249.dialysis.ui.authentication.UserData


class BaseApp: Application() {
    companion object {
        lateinit var dialysisModule: DialysisModule
        lateinit var fluidBalanceModule: FluidBalanceModule
        lateinit var userModule: UserModule
        lateinit var userData: UserData
        lateinit var groupKey: String
    }

    override fun onCreate() {
        super.onCreate()
        dialysisModule = DialysisModuleImpl(this)
        fluidBalanceModule = FluidBalanceModuleImpl(this)
        userModule = UserModuleImpl(this)
        groupKey = ""
    }


}