package com.vibelocal.app.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("vibelocal_session", Context.MODE_PRIVATE)
    fun save(userId: Int, token: String, name: String) = prefs.edit().putInt("userId", userId).putString("token", token).putString("name", name).apply()
    fun userId(): Int = prefs.getInt("userId", 0)
    fun token(): String? = prefs.getString("token", null)
    fun name(): String = prefs.getString("name", "there") ?: "there"
    fun clear() = prefs.edit().clear().apply()

    fun setLanguage(lang: String) = prefs.edit().putString("language", lang).apply()
    fun getLanguage(): String = prefs.getString("language", "en") ?: "en"

    fun setTheme(mode: Int) = prefs.edit().putInt("theme", mode).apply()
    fun getTheme(): Int = prefs.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
}
