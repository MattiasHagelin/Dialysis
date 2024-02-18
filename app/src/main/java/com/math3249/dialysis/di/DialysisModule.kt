package com.math3249.dialysis.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.repository.DialysisInterface
import com.math3249.dialysis.repository.DialysisRepository

interface DialysisModule {
    val database: FirebaseDatabase
    val dialysisRepository: DialysisInterface
}
class DialysisModuleImpl(
    private val  appContext : Context
): DialysisModule {
    override val database: FirebaseDatabase by lazy {
        Firebase.database(Constants.DATABASE_BASE_URL)
    }
    override val dialysisRepository: DialysisInterface by lazy {
        DialysisRepository(database)
    }

}