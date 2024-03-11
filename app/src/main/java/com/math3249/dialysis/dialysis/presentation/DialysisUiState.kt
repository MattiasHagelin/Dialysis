package com.math3249.dialysis.dialysis.presentation

import com.math3249.dialysis.dialysis.data.DialysisEntry
import com.math3249.dialysis.dialysis.data.DialysisProgram
import com.math3249.dialysis.session.Session

data class DialysisUiState (
    val entries: MutableList<DialysisEntry> = mutableListOf(),
    val weightBefore: String = "",
    val weightAfter: String = "",
    val ultrafiltration: String = "",
    val selectedProgram: String = "",
    val programs: MutableList<DialysisProgram> = mutableListOf(),
    val showAddDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val itemKey: String = "",
    val expanded: Boolean = false,
    val programSelectExpanded: Boolean = false,
    val session: Session = Session()
)