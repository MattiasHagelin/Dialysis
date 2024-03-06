package com.math3249.dialysis.dialysis.di

import com.google.firebase.database.FirebaseDatabase
import com.math3249.dialysis.dialysis.data.IDialysis

interface IDialysisModule{
    val database: FirebaseDatabase
    val dialysisRepository: IDialysis
}