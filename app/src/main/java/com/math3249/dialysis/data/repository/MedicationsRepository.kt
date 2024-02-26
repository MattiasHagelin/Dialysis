package com.math3249.dialysis.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.math3249.dialysis.data.model.Medicine
import com.math3249.dialysis.data.repository.repository_interface.IMedications
import com.math3249.dialysis.other.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class MedicationsRepository(
    database: FirebaseDatabase
): IMedications {
    private val group = database.getReference(Constants.TABLE_GROUP)
    override suspend fun getMedications(groupId: String) = callbackFlow<Result<MutableList<Medicine>>> {
        val medicineListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map { item ->
                    item.getValue(Medicine::class.java)
                }
                this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()) as Result<MutableList<Medicine>>)
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }
        group.child(groupId)
            .child(Constants.TABLE_MEDICATIONS)
            .addValueEventListener(medicineListener)

        awaitClose {
            group.child(Constants.TABLE_MEDICATIONS)
                .removeEventListener(medicineListener)
        }
    }

    override suspend fun updateMedicine(medicine: Medicine, groupId: String) {
        group.child(groupId)
            .child(Constants.TABLE_MEDICATIONS)
            .child(medicine.key)
            .setValue(medicine)
    }

    override suspend fun deleteMedicine(key: String, groupId: String) {
        group.child(groupId)
            .child(Constants.TABLE_MEDICATIONS)
            .child(key)
            .removeValue()
    }

    override suspend fun createMedicine(medicine: Medicine, groupId: String) {
        val key = group.child(Constants.TABLE_MEDICATIONS).push().key

        if (key != null) {
            group.child(groupId)
                .child(Constants.TABLE_MEDICATIONS)
                .child(key)
                .setValue(
                    medicine.copy(key = key)
                )
        }
    }
}