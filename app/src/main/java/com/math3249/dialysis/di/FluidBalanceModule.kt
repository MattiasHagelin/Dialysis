package com.math3249.dialysis.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.math3249.dialysis.data.repository.FluidBalanceRepository
import com.math3249.dialysis.data.repository.repository_interface.IFluidBalance
import com.math3249.dialysis.di.di_interface.IFluidBalanceModule
import com.math3249.dialysis.other.Constants

class FluidBalanceModule(
    private val appContext: Context
): IFluidBalanceModule {
    override val database: FirebaseDatabase by lazy {
        Firebase.database(Constants.DATABASE_BASE_URL)
    }
    override val fluidBalanceRepository: IFluidBalance by lazy {
        FluidBalanceRepository(database)
    }
}