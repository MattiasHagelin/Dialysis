package com.math3249.dialysis.di.di_interface

import com.google.firebase.database.FirebaseDatabase
import com.math3249.dialysis.data.repository.repository_interface.FluidBalanceInterface

interface IFluidBalanceModule {
    val database: FirebaseDatabase
    val fluidBalanceRepository: FluidBalanceInterface
}