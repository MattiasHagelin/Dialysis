package com.math3249.dialysis.session

import android.content.SharedPreferences
import com.math3249.dialysis.other.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class SessionCache(
    private val sharedPreference: SharedPreferences
): ISessionCache {

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val adapter = moshi.adapter(Session::class.java)
    override fun saveSession(session: Session) {
        sharedPreference.edit()
            .putString(Constants.SESSION, adapter.toJson(session))
            .apply()
    }

    override fun getActiveSession(): Session? {
        val json = sharedPreference.getString(Constants.SESSION, null) ?: return null
        return adapter.fromJson(json)
    }

    override fun clearSession() {
        sharedPreference.edit().remove(Constants.SESSION).apply()
    }
}