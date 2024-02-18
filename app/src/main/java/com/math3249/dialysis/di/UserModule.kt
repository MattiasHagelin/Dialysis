package com.math3249.dialysis.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.math3249.dialysis.data.repository.UserRepository
import com.math3249.dialysis.data.repository.repository_interface.IUser
import com.math3249.dialysis.di.di_interface.IUserModule
import com.math3249.dialysis.other.Constants

class UserModule(
    private val appContext: Context
): IUserModule
{
    override val database: FirebaseDatabase by lazy {
        Firebase.database(Constants.DATABASE_BASE_URL)
    }
    override val userRepository: IUser by lazy {
        UserRepository(database)
    }

}