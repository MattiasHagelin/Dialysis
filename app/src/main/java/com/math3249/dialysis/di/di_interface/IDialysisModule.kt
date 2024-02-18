package com.math3249.dialysis.di.di_interface

import com.google.firebase.database.FirebaseDatabase
import com.math3249.dialysis.data.repository.repository_interface.IDialysis

interface IDialysisModule{
    val database: FirebaseDatabase
    val dialysisRepository: IDialysis
}