package com.math3249.dialysis.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.math3249.dialysis.data.repository.MedicationsRepository

class MedicationViewModel(
    private val medicationsRepository: MedicationsRepository
): ViewModel() {
}