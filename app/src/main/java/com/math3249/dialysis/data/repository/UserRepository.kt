package com.math3249.dialysis.data.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values
import com.math3249.dialysis.data.model.GroupMember
import com.math3249.dialysis.data.repository.repository_interface.IUser
import com.math3249.dialysis.other.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class UserRepository (
    private val database: FirebaseDatabase
): IUser {

    private val groupMember = database.getReference(Constants.TABLE_GROUP_MEMBER)
    private val group = database.getReference(Constants.TABLE_GROUP)
    override suspend fun getGroupKey(user: String) = callbackFlow<Result<String>> {

        val gmListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupKey = snapshot.getValue(GroupMember::class.java)?.groupId
                this@callbackFlow.trySendBlocking(Result.success(groupKey) as Result<String>)
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }

        groupMember.child(user).addValueEventListener(gmListener)

        awaitClose {
            groupMember.child(user).removeEventListener(gmListener)
        }
    }

    override suspend fun addNewGroupMember(user: String) {
        val gm = groupMember.child(user).values<Any>()
        if (gm == null) {

        }
    }

    override suspend fun createNewGroup(user: String) {
        groupMember.child(user).get()
            .addOnSuccessListener { result ->
                val gm = result.getValue(GroupMember::class.java)
                if (gm == null) {
                    val newGroupKey = groupMember.child(user).push().key
                    if (newGroupKey != null) {
                        groupMember.child(user).child(newGroupKey).setValue(
                            GroupMember(
                                groupId = newGroupKey
                            )
                        )
                        group.child(newGroupKey).setValue("placeholder")
                    }
                }
            }
            .addOnFailureListener {
                Log.w("Error", it.message, throw it)
            }
    }
}