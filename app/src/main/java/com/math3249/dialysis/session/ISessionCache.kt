package com.math3249.dialysis.session

interface ISessionCache {
    fun saveSession(session: Session)
    fun getActiveSession(): Session?
    fun clearSession()
}