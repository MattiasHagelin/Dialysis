package com.math3249.dialysis.fluidbalance.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.math3249.dialysis.data.model.GroupMember
import com.math3249.dialysis.other.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class FluidBalanceRepository (
    database: FirebaseDatabase
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

    override suspend fun getFluidBalanceHistory(groupKey: String) = callbackFlow<Result<MutableList<FluidBalanceHistory>>> {
        val entryListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map { item ->
                    item.getValue(FluidBalanceHistory::class.java)
                }.sortedByDescending { it?.drunkTimeStamp }
                @Suppress("UNCHECKED_CAST")
                this@callbackFlow.trySendBlocking(Result.success(items) as Result<MutableList<FluidBalanceHistory>>)
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }
        group.child(groupKey).child(Constants.TABLE_FLUID_BALANCE_HISTORY).addValueEventListener(entryListener)

        awaitClose {
            group.child(groupKey).child(Constants.TABLE_FLUID_BALANCE_HISTORY).removeEventListener(entryListener)
        }
    }

    override suspend fun updateConsumedVolume(fluidBalance: FluidBalance, groupKey: String) {

        val dialysisGroup = group.child(groupKey)
        dialysisGroup.child(Constants.TABLE_FLUID_BALANCE).setValue(fluidBalance)
    }

    override suspend fun updateConsumedHistory(entry: FluidBalanceHistory, groupKey: String) {
        val key = group.child(groupKey)
            .child(Constants.TABLE_FLUID_BALANCE_HISTORY)
            .push().key
        if (key != null) {
            group.child(groupKey).child(Constants.TABLE_FLUID_BALANCE_HISTORY).child(key).setValue(entry)
        }
    }

    override suspend fun clearHistory(groupKey: String) {
        group.child(groupKey).child(Constants.TABLE_FLUID_BALANCE_HISTORY).removeValue()
    }

    override suspend fun resetFluidBalance(groupKey: String) {
        group.child(groupKey).child(Constants.TABLE_FLUID_BALANCE).setValue(
            FluidBalance(
                fluidLimit = 650,//TODO pass current value from uiState instead
                drunkVolume = 0,
                volumeUnit = ""
            )
        )
    }

    override suspend fun updateFluidLimit(volume: Int, groupKey: String) {
        group.child(groupKey).child(Constants.TABLE_FLUID_BALANCE)
            .child("fluidLimit").setValue(volume)
            .addOnSuccessListener {
                Log.i(Constants.DIALYSIS_FIREBASE, "Updated fluidLimit")
            }
            .addOnFailureListener {
                Log.e(Constants.DIALYSIS_FIREBASE, it.message, it)
            }
    }

    override suspend fun getGroupKey(
        user: String,
        callback: (GroupMember) -> Unit
    ) {
         groupMember.child(user).get()
            .addOnSuccessListener {
                it.children.forEach { item ->
                    Log.i("firebase", "Got value ${item.value}")
                    val data = item.getValue(GroupMember::class.java)
                    if (data != null)
                        callback(data)
                }
            }
            .addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
    }
}