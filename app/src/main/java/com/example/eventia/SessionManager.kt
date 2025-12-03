package com.example.eventia

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREF_NAME = "EventiaSession"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_REMEMBER_ME = "remember_me"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveSession(context: Context, user: LoginResponse, rememberMe: Boolean) {
        val editor = getPreferences(context).edit()

        editor.putString(KEY_USER_ID, user.id.toString())
        editor.putString(KEY_USER_NAME, user.name)
        editor.putString(KEY_USER_EMAIL, user.email)
        editor.putString(KEY_USER_ROLE, user.role)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)

        editor.putBoolean(KEY_REMEMBER_ME, rememberMe)

        editor.apply()
    }

    fun getUserName(context: Context): String {
        return getPreferences(context).getString(KEY_USER_NAME, "Usuário") ?: "Usuário"
    }

    fun getEmail(context: Context): String {
        return getPreferences(context).getString(KEY_USER_EMAIL, "seu@email.com") ?: "seu@email.com"
    }

    fun getRole(context: Context): String {
        return getPreferences(context).getString(KEY_USER_ROLE, "user") ?: "user"
    }

    fun getUserId(context: Context): String {
        return getPreferences(context).getString(KEY_USER_ID, "") ?: ""
    }

    fun isLoggedIn(context: Context): Boolean {

        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false) &&
                getPreferences(context).getBoolean(KEY_REMEMBER_ME, false)
    }

    fun logout(context: Context) {
        val editor = getPreferences(context).edit()
        editor.clear()
        editor.apply()
    }
}