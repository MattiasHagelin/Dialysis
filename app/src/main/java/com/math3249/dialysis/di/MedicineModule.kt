package com.math3249.dialysis.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.math3249.dialysis.data.repository.MedicationsRepository
import com.math3249.dialysis.data.repository.repository_interface.IMedications
import com.math3249.dialysis.di.di_interface.IMedicineModule
import com.math3249.dialysis.other.Constants

class MedicineModule(
    private val appContext: Context
): IMedicineModule {
    override val database: FirebaseDatabase by lazy {
        Firebase.database(Constants.DATABASE_BASE_URL)
    }
    override val medicineRepository: IMedications by lazy {
        MedicationsRepository(database)
    }
}