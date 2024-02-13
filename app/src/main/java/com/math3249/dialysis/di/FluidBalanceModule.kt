package com.math3249.dialysis.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.repository.FluidBalanceInterface
import com.math3249.dialysis.repository.FluidBalanceRepository

interface FluidBalanceModule {
    val database: FirebaseDatabase
    val fluidBalanceRepository: FluidBalanceInterface
}

class FluidBalanceModuleImpl(
    private val appContext: Context
): FluidBalanceModule {
    override val database: FirebaseDatabase by lazy {
        Firebase.database(Constants.DATABASE_BASE_URL)
    }
    override val fluidBalanceRepository: FluidBalanceInterface by lazy {
        FluidBalanceRepository(database)
    }
}