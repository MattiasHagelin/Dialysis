package com.math3249.dialysis.medication.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.math3249.dialysis.other.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class MedicationsRepository(
    database: FirebaseDatabase
): IMedication {
    private val group = database.getReference(Constants.TABLE_GROUP)
    override suspend fun getMedications(groupId: String) = callbackFlow<Result<MutableList<Medication>>> {
        val medicationListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map { item ->
                    item.getValue(Medication::class.java)
                }
                this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()) as Result<MutableList<Medication>>)
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }
        group.child(groupId)
            .child(Constants.TABLE_MEDICATIONS)
            .addValueEventListener(medicationListener)

        awaitClose {
            group.child(Constants.TABLE_MEDICATIONS)
                .removeEventListener(medicationListener)
        }
    }

    override suspend fun getMedication(
        key: String,
        groupId: String,
        callback: (Medication) -> Unit
    ) {
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val medication = snapshot.getValue(Medication::class.java)
                if (medication != null)
                    callback(medication)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        group.child(groupId)
            .child(Constants.TABLE_MEDICATIONS)
            .child(key)
            .addValueEventListener(listener)
    }

    /*override suspend fun getMedication(key: String, groupId: String) = callbackFlow<Result<Medication>> {
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val medication = snapshot.getValue(Medication::class.java)
                this@callbackFlow.trySendBlocking(Result.success(medication) as Result<Medication>)
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }

        group.child(groupId)
            .child(Constants.TABLE_MEDICATIONS)
            .child(key)
            .addValueEventListener(listener)

        awaitClose{
            group.child(groupId)
                .child(Constants.TABLE_MEDICATIONS)
                .child(key)
                .removeEventListener(listener)
        }
    }*/

    override suspend fun updateMedication(medication: Medication, groupId: String) {
        group.child(groupId)
            .child(Constants.TABLE_MEDICATIONS)
            .child(medication.key)
            .setValue(medication)
    }

    override suspend fun deleteMedication(key: String, groupId: String) {
        group.child(groupId)
            .child(Constants.TABLE_MEDICATIONS)
            .child(key)
            .removeValue()
    }

    override suspend fun createMedication(medication: Medication, groupId: String) {
        val key = group.child(Constants.TABLE_MEDICATIONS).push().key

        if (key != null) {
            group.child(groupId)
                .child(Constants.TABLE_MEDICATIONS)
                .child(key)
                .setValue(
                    medication.copy(key = key)
                )
        }
    }
}