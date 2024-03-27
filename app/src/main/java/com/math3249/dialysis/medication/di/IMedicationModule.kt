package com.math3249.dialysis.medication.di

import com.google.firebase.database.FirebaseDatabase
import com.math3249.dialysis.medication.data.IMedication

interface IMedicationModule {
    val database: FirebaseDatabase
    val medicationRepository: IMedication
}