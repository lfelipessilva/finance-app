package com.orbita.finad.data.local

import android.content.Context
import android.content.SharedPreferences
import com.orbita.finad.data.remote.entity.User
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val moshi = Moshi.Builder().build()
    private val userAdapter = moshi.adapter(User::class.java)

    private val _isLoggedIn =
            MutableStateFlow(sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false))
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    companion object {
        @Volatile private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return INSTANCE
                    ?: synchronized(this) {
                        INSTANCE
                                ?: SessionManager(context.applicationContext).also { INSTANCE = it }
                    }
        }

        private const val PREF_NAME = "session"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER = "user"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveSession(accessToken: String, user: User) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ACCESS_TOKEN, accessToken)
        editor.putString(KEY_USER, userAdapter.toJson(user))
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.apply()

        _isLoggedIn.value = true
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(KEY_USER, null)
        return if (userJson != null) {
            try {
                userAdapter.fromJson(userJson)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearSession() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_ACCESS_TOKEN)
        editor.remove(KEY_USER)
        editor.putBoolean(KEY_IS_LOGGED_IN, false)
        editor.apply()

        _isLoggedIn.value = false
    }
}
