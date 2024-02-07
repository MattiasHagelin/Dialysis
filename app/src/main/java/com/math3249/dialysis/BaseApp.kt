package com.math3249.dialysis

import android.app.Application
import com.math3249.dialysis.di.AppModule
import com.math3249.dialysis.di.AppModuleImpl


class BaseApp: Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}