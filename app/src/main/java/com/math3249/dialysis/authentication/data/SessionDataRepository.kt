package com.math3249.dialysis.authentication.data

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.math3249.dialysis.other.Constants

class SessionDataRepository(
    private val database: FirebaseDatabase
): ISessionData {
    private val groupMember = database.getReference(Constants.TABLE_GROUP_MEMBER)
    override suspend fun getGroupKey(userId: String, callback: (String) -> Unit) {
        groupMember.child(userId).get()
            .addOnSuccessListener {
                it.children.forEach { item ->
                    Log.i("firebase", "Got value ${item.value}")
                    val data = item.key
                    if (data != null)
                        callback(data)
                }
            }
            .addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
    }
}