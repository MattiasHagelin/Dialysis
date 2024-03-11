package com.math3249.dialysis.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.math3249.dialysis.data.model.GroupMember
import com.math3249.dialysis.data.repository.repository_interface.IUser
import com.math3249.dialysis.other.Constants

class UserRepository (
    private val database: FirebaseDatabase
): IUser {

    private val groupMember = database.getReference(Constants.TABLE_GROUP_MEMBER)
    override suspend fun getGroupKey(
        user: String,
        callback: (String) -> Unit
        )  {
        val gmListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupKey = snapshot.getValue(GroupMember::class.java)?.groupId
                if (groupKey != null) {
                    callback(groupKey)
                } else {
                    callback("groupKeyIsNull")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback("Failed")
            }
        }
        groupMember.child(user).addValueEventListener(gmListener)
    }

    override suspend fun addNewGroupMember(user: String) {

    }

    override suspend fun createMemberGroup(
        user: String,
        callback: (String) -> Unit
    ) {
        val key = database.getReference("key").push().key
        if (key != null) {
            database.getReference("key").removeValue()
            database.getReference("member_group").child(user).setValue(key)
        }
    }

    override suspend fun createNewGroup(user: String) {

        val key = database.getReference("key").push().key
        if (key != null) {
            database.getReference("key").removeValue()
            database.getReference("member_group").child(user).setValue(key)
        }
//        groupMember.child(user).get()
//            .addOnSuccessListener { result ->
//                val gm = result.getValue(GroupMember::class.java)
//                if (gm == null) {
//                    val newGroupKey = groupMember.child(user).push().key
//                    if (newGroupKey != null) {
//                        groupMember.child(user).child(newGroupKey).setValue(
//                            GroupMember(
//                                groupId = newGroupKey
//                            )
//                        )
//                        group.child(newGroupKey).setValue("placeholder")
//                    }
//                }
//            }
//            .addOnFailureListener {
//                Log.w("Error", it.message, throw it)
//            }
    }
}