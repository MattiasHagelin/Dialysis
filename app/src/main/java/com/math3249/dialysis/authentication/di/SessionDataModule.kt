package com.math3249.dialysis.authentication.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.math3249.dialysis.authentication.data.ISessionData
import com.math3249.dialysis.authentication.data.SessionDataRepository
import com.math3249.dialysis.other.Constants

class SessionDataModule(
    private val appContext: Context
): ISessionDataModule {
    override val database: FirebaseDatabase by lazy {
        Firebase.database(Constants.DATABASE_BASE_URL)
    }

    override val sessionDataRepository: ISessionData by lazy {
        SessionDataRepository(database)
    }
}