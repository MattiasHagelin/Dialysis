package com.math3249.dialysis.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.math3249.dialysis.data.repository.DialysisRepository
import com.math3249.dialysis.data.repository.repository_interface.IDialysis
import com.math3249.dialysis.di.di_interface.IDialysisModule
import com.math3249.dialysis.other.Constants


class DialysisModule(
    private val  appContext : Context
): IDialysisModule {
    override val database: FirebaseDatabase by lazy {
        Firebase.database(Constants.DATABASE_BASE_URL)
    }
    override val dialysisRepository: IDialysis by lazy {
        DialysisRepository(database)
    }

}