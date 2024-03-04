package com.math3249.dialysis.medications.di

import com.google.firebase.database.FirebaseDatabase
import com.math3249.dialysis.medications.data.IMedication

interface IMedicationModule {
    val database: FirebaseDatabase
    val medicineRepository: IMedication
}