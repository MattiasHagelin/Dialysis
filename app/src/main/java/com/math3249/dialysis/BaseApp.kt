package com.math3249.dialysis

import android.app.Application
import com.math3249.dialysis.di.DialysisModule
import com.math3249.dialysis.di.FluidBalanceModule
import com.math3249.dialysis.medications.di.MedicationModule
import com.math3249.dialysis.di.UserModule
import com.math3249.dialysis.di.di_interface.IDialysisModule
import com.math3249.dialysis.di.di_interface.IFluidBalanceModule
import com.math3249.dialysis.medications.di.IMedicationModule
import com.math3249.dialysis.di.di_interface.IUserModule
import com.math3249.dialysis.ui.authentication.UserData


class BaseApp: Application() {
    companion object {
        lateinit var dialysisModule: IDialysisModule
        lateinit var fluidBalanceModule: IFluidBalanceModule
        lateinit var medicineModule: IMedicationModule
        lateinit var userModule: IUserModule
        lateinit var userData: UserData
        lateinit var groupKey: String
    }

    override fun onCreate() {
        super.onCreate()
        dialysisModule = DialysisModule(this)
        fluidBalanceModule = FluidBalanceModule(this)
        medicineModule = MedicationModule(this)
        userModule = UserModule(this)
        groupKey = ""
    }


}