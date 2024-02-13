package com.math3249.dialysis.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.math3249.dialysis.data.model.DialysisEntry
import com.math3249.dialysis.other.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

class DialysisRepository (
   private val database: FirebaseDatabase
): DialysisInterface {
    private val dialysisEntry = database.getReference(Constants.TABLE_DIALYSIS_ENTRIES)
    override suspend fun getDialysisEntries() = callbackFlow<Result<MutableList<DialysisEntry>>> {
        val entryListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map { item ->
                    item.getValue(DialysisEntry::class.java)
                }
                this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()) as Result<MutableList<DialysisEntry>>)

            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }
        database.getReference(Constants.TABLE_DIALYSIS_ENTRIES)
            .addValueEventListener(entryListener)

        awaitClose {
            database.getReference(Constants.TABLE_DIALYSIS_ENTRIES)
                .removeEventListener(entryListener)
        }
    }

    override suspend fun getDialysisEntry(key: String): MutableStateFlow<DialysisEntry> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDialysisEntry(data: DialysisEntry) {
        dialysisEntry.child(data.key).setValue(data)
    }

    override suspend fun deleteDialysisEntry(key: String) {
        dialysisEntry.child(key).removeValue()
    }

    override suspend fun addEntry(data: DialysisEntry) {
        val key = dialysisEntry.push().key
        if (key != null) {
            dialysisEntry.child(key).setValue(DialysisEntry(
                key = key,
                morningWeight = data.morningWeight,
                eveningWeight = data.eveningWeight,
                ultraFiltration = data.ultraFiltration,
                date = data.date
            ))
        }
    }

}