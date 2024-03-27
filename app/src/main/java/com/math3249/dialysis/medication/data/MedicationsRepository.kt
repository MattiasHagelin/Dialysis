package com.math3249.dialysis.medication.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.math3249.dialysis.other.Constants
import com.math3249.dialysis.session.ISessionCache
import com.math3249.dialysis.session.Session
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MedicationsRepository(
    database: FirebaseDatabase,
    session: ISessionCache
): IMedication {
    private val group = database.getReference(Constants.TABLE_GROUP)
    private val _session: Session

    init {
        _session = session.getActiveSession() ?: Session(
            userId = "",
            email = "",
            groupId = ""
        )
    }
    override suspend fun getMedications() = callbackFlow<Result<MutableList<Medication>>> {
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
        group.child(_session.groupId)
            .child(Constants.TABLE_MEDICATIONS)
            .addValueEventListener(medicationListener)

        awaitClose {
            group.child(_session.groupId)
                .child(Constants.TABLE_MEDICATIONS)
                .removeEventListener(medicationListener)
        }
    }

    override suspend fun getMedication(
        key: String,
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

        group.child(_session.groupId)
            .child(Constants.TABLE_MEDICATIONS)
            .child(key)
            .addValueEventListener(listener)
    }

    override suspend fun updateMedication(medication: Medication) {
        Log.i(Constants.DIALYSIS_FIREBASE, "Updating medication.")
        group.child(_session.groupId)
            .child(Constants.TABLE_MEDICATIONS)
            .child(medication.key)
            .setValue(
                if (!medication.takeWithFood)
                    medication.copy(category = LocalTime.parse(medication.time).toSecondOfDay())
                else
                    medication
            )
    }

    override suspend fun updateRecurrentMedications(
        medication: Medication,
        medications: List<Medication>
    ) {
        if (medication.takeWithFood) {
            updateTakeWithFoodRecurrence(medication, (medications))
        } else {
            val times = RecurrenceHelper().recalculateRecurrence(medication)
            updateMedication(medication)
            medications.forEach {
                if (times.isNotEmpty()) {
                    val time = times.first()
                    updateMedication(medication.copy(
                        key = it.key,
                        time = DateTimeFormatter
                            .ofPattern(Constants.TIME_24_H)
                            .format(time)
                    )
                    )
                    times.remove(time)
                } else {
                    //If more recurrences exists than new times
                    deleteMedication(it.key)
                }
            }
            //If there's recurrences left create new objects for them
            if (times.isNotEmpty()) {
                times.forEach {
                    generateMedication(
                        medication.copy(
                            time = DateTimeFormatter
                                .ofPattern(Constants.TIME_24_H)
                                .format(it),
                            category = it.toSecondOfDay()
                        )
                    )
                }
            }
        }
    }

    private suspend fun updateTakeWithFoodRecurrence(
        medication: Medication,
        medications: List<Medication>
    ) {
        val helper = RecurrenceHelper()
        val meals = helper.whichMeals(medication)
        when (meals.size) {
            1 -> {
                updateEditedMedication(meals, medication)
                medications.forEach {
                    deleteMedication(it.key)
                }
            }
            2 -> {
                updateEditedMedication(meals, medication)
                if (medications.isNotEmpty()) {
                    val key = medications.first().key
                    updateFirstMedication(medication, key, meals)
                    medications.forEach {
                        if (it.key != key)
                            deleteMedication(it.key)
                    }
                } else {
                    generateMedication(medication
                        .copy(
                            time = meals.first().time,
                            category = meals.first().stringResourceId,
                            withBreakfast = meals.first() == Meals.BREAKFAST,
                            withLunch = meals.first() == Meals.LUNCH,
                            withDinner = meals.first() == Meals.DINNER
                        )
                    )
                }
            }
            3 -> {
                updateEditedMedication(meals, medication)
                if (medications.size == meals.size) {
                    for (i in medications.indices) {
                        updateMedication(medication
                            .copy(
                                key = medications[i].key,
                                time = meals[i].time,
                                category = meals[i].stringResourceId
                            )
                        )
                    }
                } else if (medications.isNotEmpty()) {
                    val key = medications.first().key
                    updateFirstMedication(medication, key, meals)
                    generateMedication(medication
                        .copy(
                            time = meals.last().time,
                            category = meals.last().stringResourceId,
                            withBreakfast = meals.last() == Meals.BREAKFAST,
                            withLunch = meals.last() == Meals.LUNCH,
                            withDinner = meals.last() == Meals.DINNER
                        )
                    )
                } else {
                    meals.forEach {
                        generateMedication(medication
                            .copy(
                                time = it.time,
                                category = it.stringResourceId,
                                withBreakfast = it == Meals.BREAKFAST,
                                withLunch = it == Meals.LUNCH,
                                withDinner = it == Meals.DINNER
                            )
                        )
                    }
                }
            }
            else -> {}
        }
    }

    private suspend fun MedicationsRepository.updateFirstMedication(
        medication: Medication,
        key: String,
        meals: MutableList<Meals>
    ) {
        updateMedication(
            medication
                .copy(
                    key = key,
                    time = meals.first().time,
                    category = meals.first().stringResourceId,
                    withBreakfast = meals.first() == Meals.BREAKFAST,
                    withLunch = meals.first() == Meals.LUNCH,
                    withDinner = meals.first() == Meals.DINNER
                )
        )
    }

    private suspend fun MedicationsRepository.updateEditedMedication(
        meals: MutableList<Meals>,
        medication: Medication
    ) {
        val meal = meals.first()
        updateMedication(
            medication
                .copy(
                    time = meal.time,
                    category = meal.stringResourceId,
                    withBreakfast = meal == Meals.BREAKFAST,
                    withLunch = meal == Meals.LUNCH,
                    withDinner = meal == Meals.DINNER
                )
        )
        meals.remove(meal)
    }

    override suspend fun deleteMedication(key: String) {
        group.child(_session.groupId)
            .child(Constants.TABLE_MEDICATIONS)
            .child(key)
            .removeValue()
    }

    override suspend fun deleteRecurrentMedications(
        medications: List<Medication>
    ) {
        medications.forEach {
            deleteMedication(it.key)
        }
    }

    override suspend fun createMedication(medication: Medication) {
        RecurrenceHelper()
            .createMedicationForEveryRecurrenceOver24Hours(medication)
            .forEach {
                generateMedication(it)
            }
    }
    private fun generateMedication(medication: Medication) {
        val key = group.child(Constants.TABLE_MEDICATIONS).push().key
        if (key != null) {
            group.child(_session.groupId)
                .child(Constants.TABLE_MEDICATIONS)
                .child(key)
                .setValue(
                    medication.copy(key = key)
                )
        }
    }
}