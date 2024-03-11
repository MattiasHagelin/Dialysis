package com.math3249.dialysis.dialysis.domain

sealed class DialysisEvent {
    data class UpdateItemKey(val value: String): DialysisEvent()
    data class UpdateWeightBefore(val value: String): DialysisEvent()
    data class UpdateWeightAfter(val value: String): DialysisEvent()
    data class UpdateUltrafiltration(val value: String): DialysisEvent()
    data class UpdateSelectedProgram(val value: String): DialysisEvent()
    data class UpdateExpanded(val value: Boolean):DialysisEvent()
    data object UpdateDialysisEntry: DialysisEvent()
    data object GetDialysisEntry: DialysisEvent()
    data object CreateEntry: DialysisEvent()
    data object DeleteEntry: DialysisEvent()
    data object Clear: DialysisEvent()
    data object Back: DialysisEvent()
    data object Add: DialysisEvent()
    data class Edit(val value: String): DialysisEvent()
    data object SignOut: DialysisEvent()
}