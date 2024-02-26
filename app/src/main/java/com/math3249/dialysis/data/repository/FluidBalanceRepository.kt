package com.math3249.dialysis.data.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.math3249.dialysis.data.model.FluidBalance
import com.math3249.dialysis.data.model.GroupMember
import com.math3249.dialysis.data.repository.repository_interface.IFluidBalance
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.other.IGroupMemberCallback
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class FluidBalanceRepository (
    private val database: FirebaseDatabase
): IFluidBalance {
    private val group = database.getReference(Constants.TABLE_GROUP)
    private val groupMember = database.getReference(Constants.TABLE_GROUP_MEMBER)

    override suspend fun getFluidBalance(groupKey: String) = callbackFlow<Result<FluidBalance>> {
        val entryListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(FluidBalance::class.java)
                this@callbackFlow.trySendBlocking(Result.success(item) as Result<FluidBalance>)
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }
            group.child(groupKey).child(Constants.TABLE_FLUID_BALANCE).addValueEventListener(entryListener)

        awaitClose {
            group.removeEventListener(entryListener)
        }
    }

    override suspend fun addConsumedFluid(data: FluidBalance, groupKey: String) {
        group.child(groupKey).child(Constants.TABLE_FLUID_BALANCE).setValue(data)
    }

    override suspend fun resetFluidBalance(groupKey: String) {
        group.child(groupKey).child(Constants.TABLE_FLUID_BALANCE).setValue(FluidBalance(
            fluidLimit = 650,
            consumedFluid = 0
        ))
    }

    override suspend fun updateFluidLimit(data: FluidBalance, groupKey: String) {
        group.child(groupKey).child(Constants.TABLE_FLUID_BALANCE).setValue(data)
    }

    override suspend fun getGroupKey(
        user: String,
        groupMemberCallback: IGroupMemberCallback
    ) {
         groupMember.child(user).get()
            .addOnSuccessListener {
                it.children.forEach { item ->
                    Log.i("firebase", "Got value ${item.value}")
                    val data = item.getValue(GroupMember::class.java)
                    if (data != null)
                        groupMemberCallback.onCallback(data)
                }
            }
            .addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
    }

    private fun logger(message: String) {
        Log.w("MathLogger", message)
    }
}