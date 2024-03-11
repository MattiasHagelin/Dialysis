package com.math3249.dialysis.authentication.data

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.math3249.dialysis.data.model.GroupMember
import com.math3249.dialysis.other.Constants

class SessionDataRepository(
    private val database: FirebaseDatabase
): ISessionData {
    override suspend fun getGroupKey(userId: String, callback: (String) -> Unit) {
        database.getReference(Constants.TABLE_MEMBER_GROUP).child(userId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    callback(snapshot.value.toString())
                    Log.i("dialysis_firebase", "Snapshot value is ${snapshot.value}")
                } else {
                    callback("")
                    Log.i("dialysis_firebase", "Empty snapshot")
                }
            }
            .addOnFailureListener {
                Log.e("dialysis_firebase", it.message, it)
            }
    }

    override suspend fun createGroupKey(userId: String, callback: (String) -> Unit) {
        val key = database.getReference("key").push().key
        if (key != null) {
            database.getReference("key").removeValue()
            database.getReference(Constants.TABLE_MEMBER_GROUP).child(userId).setValue(key)
                .addOnSuccessListener {
                    callback(key)
                    Log.i("dialysis_firebase", "${Constants.TABLE_MEMBER_GROUP} was created.")
                }
                .addOnFailureListener {
                    callback("")
                    Log.e("dialysis_firebase", it.message, it)
                }
        } else {
            callback("")
            Log.e("dialysis_firebase", "Failed to create new group key")
        }
    }

    override suspend fun loadRequiredData(
        userId: String,
        groupId: String,
        callback: (Boolean) -> Unit
    ) {
        database.getReference(Constants.TABLE_GROUP_MEMBER).child(groupId).child(userId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    callback(true)
                    Log.i(Constants.DIALYSIS_FIREBASE, "${Constants.TABLE_GROUP_MEMBER} exists")
                } else {
                    Log.e(Constants.DIALYSIS_FIREBASE, "Failed to retrieve ${Constants.TABLE_GROUP_MEMBER}")
                    callback(false)
//                    createGroupMember(userId, groupId) {
//                    }
                }
            }
            .addOnFailureListener {
                Log.e(Constants.DIALYSIS_FIREBASE, "Failed to retrieve ${Constants.TABLE_GROUP_MEMBER}")
                Log.e(Constants.DIALYSIS_FIREBASE, it.message, it)
            }
    }

    override suspend fun joinGroup(userId: String, groupId: String, callback: (String) -> Unit) {
        if (groupId != "") {
            database.getReference(Constants.TABLE_MEMBER_GROUP).child(userId).setValue(groupId)
                .addOnSuccessListener {
                    callback(groupId)
                    Log.i("dialysis_firebase", "Joined group.")
                }
                .addOnFailureListener {
                    callback("")
                    Log.e("dialysis_firebase", it.message, it)
                }
        } else {
            callback("")
            Log.e("dialysis_firebase", "Failed to join group")
        }
    }

    override suspend fun createRequiredData(userId: String, groupId: String, callback: (Boolean) -> Unit) {
        database.getReference(Constants.TABLE_GROUP_MEMBER).child(groupId).child(userId).get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    createGroupMember(userId, groupId) {
                        callback(it)
                    }
                    callback(true)
                    Log.i(Constants.DIALYSIS_FIREBASE, "${Constants.TABLE_GROUP_MEMBER} created.")
                } else {
                    Log.e(Constants.DIALYSIS_FIREBASE, "${Constants.TABLE_GROUP_MEMBER} already exists.")
                    callback(false)
                }
            }
            .addOnFailureListener {
                Log.e(Constants.DIALYSIS_FIREBASE, "Something went wrong while reading database.")
                Log.e(Constants.DIALYSIS_FIREBASE, it.message, it)
            }
    }

    private fun createGroupMember(userId: String, groupId: String, callback: (Boolean) -> Unit) {
        database.getReference(Constants.TABLE_GROUP_MEMBER)
            .child(groupId)
            .child(userId)
            .setValue(
                GroupMember(
                    groupId = groupId
                )
            )
            .addOnSuccessListener {
                callback(true)
                Log.i(Constants.DIALYSIS_FIREBASE, "${Constants.TABLE_GROUP_MEMBER} was created.")
            }
            .addOnFailureListener {
                callback(false)
                Log.e(Constants.DIALYSIS_FIREBASE, "Failed to create ${Constants.TABLE_GROUP_MEMBER}")
                Log.e(Constants.DIALYSIS_FIREBASE, it.message, it)
            }
    }
}