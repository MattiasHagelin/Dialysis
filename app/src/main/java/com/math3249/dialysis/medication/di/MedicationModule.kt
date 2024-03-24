package com.math3249.dialysis.medication.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.math3249.dialysis.medication.data.IMedication
import com.math3249.dialysis.medication.data.MedicationsRepository
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.session.SessionCache

class MedicationModule(
    private val appContext: Context
): IMedicationModule {
    override val database: FirebaseDatabase by lazy {
        Firebase.database(Constants.DATABASE_BASE_URL)
    }
    override val medicineRepository: IMedication by lazy {
        MedicationsRepository(
            database,
            SessionCache(appContext.getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE))
        )
    }
}