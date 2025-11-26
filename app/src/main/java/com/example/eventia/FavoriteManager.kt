package com.example.eventia

import android.content.Context

class FavoriteManager(context: Context, userId: String) {

    private val prefs = context.getSharedPreferences("FavoritePrefs_User_$userId", Context.MODE_PRIVATE)

    fun addFavorite(eventoId: Int) {
        prefs.edit().putBoolean(eventoId.toString(), true).apply()
    }

    fun removeFavorite(eventoId: Int) {
        prefs.edit().remove(eventoId.toString()).apply()
    }

    fun isFavorite(eventoId: Int): Boolean {
        return prefs.getBoolean(eventoId.toString(), false)
    }

    fun toggleFavorite(eventoId: Int) {
        if (isFavorite(eventoId)) {
            removeFavorite(eventoId)
        } else {
            addFavorite(eventoId)
        }
    }
}