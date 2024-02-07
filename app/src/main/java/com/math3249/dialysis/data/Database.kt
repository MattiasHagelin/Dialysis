package com.math3249.dialysis.data

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class Database {
    private val database = Firebase.database("https://dialysis-8dae4-default-rtdb.europe-west1.firebasedatabase.app/")
    private val root = "https://dialysis-8dae4-default-rtdb.europe-west1.firebasedatabase.app/"

    fun dialysisEntriesReference(): DatabaseReference {
        return database.getReference("dialysis_entries")
    }
}