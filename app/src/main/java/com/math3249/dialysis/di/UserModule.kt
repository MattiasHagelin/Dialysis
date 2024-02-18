package com.math3249.dialysis.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.repository.UserInterface
import com.math3249.dialysis.repository.UserRepository

interface UserModule {
    val database: FirebaseDatabase
    val userRepository: UserInterface
}

class UserModuleImpl(
    private val appContext: Context
): UserModule{
    override val database: FirebaseDatabase by lazy {
        Firebase.database(Constants.DATABASE_BASE_URL)
    }
    override val userRepository: UserInterface by lazy {
        UserRepository(database)
    }

}