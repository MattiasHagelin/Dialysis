package com.math3249.dialysis.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.math3249.dialysis.BaseApp
import com.math3249.dialysis.data.model.DialysisEntry
import com.math3249.dialysis.other.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalDate

class DialysisRepository (
   private val database: FirebaseDatabase
): DialysisInterface {
    private val group = database.getReference(Constants.TABLE_GROUP)
    override suspend fun getDialysisEntries(groupKey: String) = callbackFlow<Result<MutableList<DialysisEntry>>> {
        val entryListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map { item ->
                    item.getValue(DialysisEntry::class.java)
                }.sortedByDescending { it?.date }
                this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()) as Result<MutableList<DialysisEntry>>)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("MatHInfo", BaseApp.groupKey)
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }
        group.child(groupKey).child(Constants.TABLE_DIALYSIS_ENTRIES)
            .addValueEventListener(entryListener)

        awaitClose {
            group.removeEventListener(entryListener)
        }
    }

    override suspend fun updateDialysisEntry(data: DialysisEntry, groupKey: String) {
        group.child(groupKey)
            .child(Constants.TABLE_DIALYSIS_ENTRIES)
            .child(data.key).setValue(data)
    }

    override suspend fun deleteDialysisEntry(key: String, groupKey: String) {
        group.child(groupKey)
            .child(Constants.TABLE_DIALYSIS_ENTRIES)
            .child(key).removeValue()
    }

    override suspend fun addEntry(data: DialysisEntry, groupKey: String) {
        val key = group.child(groupKey)
            .child(Constants.TABLE_DIALYSIS_ENTRIES)
            .push().key
        if (key != null) {
            group.child(groupKey)
                .child(Constants.TABLE_DIALYSIS_ENTRIES)
                .child(key).setValue(DialysisEntry(
                    key = key,
                    weightAfter = data.weightAfter,
                    weightBefore = data.weightBefore,
                    ultraFiltration = data.ultraFiltration,
                    program = data.program,
                    date = LocalDate.now().plusDays(1).toString()
                ))
        }
    }

}