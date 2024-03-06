package com.math3249.dialysis.authentication.di

import com.google.firebase.database.FirebaseDatabase
import com.math3249.dialysis.authentication.data.ISessionData

interface ISessionDataModule {
    val database: FirebaseDatabase
    val sessionDataRepository: ISessionData
}