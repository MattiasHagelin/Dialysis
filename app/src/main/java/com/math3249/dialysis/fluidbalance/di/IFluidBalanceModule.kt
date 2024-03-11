package com.math3249.dialysis.fluidbalance.di

import com.google.firebase.database.FirebaseDatabase
import com.math3249.dialysis.fluidbalance.data.IFluidBalance

interface IFluidBalanceModule {
    val database: FirebaseDatabase
    val fluidBalanceRepository: IFluidBalance
}