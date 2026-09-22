package com.vibelocal.app.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vibelocal.app.R
import com.vibelocal.app.api.ApiClient
import com.vibelocal.app.util.LocaleHelper
import com.vibelocal.app.util.SessionManager

abstract class BaseActivity : AppCompatActivity() {
    protected val session by lazy { SessionManager(this) }
    protected val api by lazy { ApiClient.service(this) }

    override fun attachBaseContext(newBase: Context) {
        val session = SessionManager(newBase)
        val lang = session.getLanguage()
        super.attachBaseContext(LocaleHelper.wrap(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(session.getTheme())
    }

    protected fun setupBottomNav(nav: BottomNavigationView, currentItemId: Int) {
        nav.selectedItemId = currentItemId
        nav.setOnItemSelectedListener {
            if (it.itemId == currentItemId) return@setOnItemSelectedListener true
            when (it.itemId) {
                R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_search -> startActivity(Intent(this, SearchActivity::class.java))
                R.id.nav_map -> startActivity(Intent(this, MapActivity::class.java))
                R.id.nav_saved -> startActivity(Intent(this, SavedActivity::class.java))
                R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            }
            true
        }
    }
}
