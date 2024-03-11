package com.math3249.dialysis.start.presentation

import com.vanpra.composematerialdialogs.MaterialDialogState

data class StartUiState(
    val shareDialogState: MaterialDialogState = MaterialDialogState(),
    val shareToEmail: String = ""
)
