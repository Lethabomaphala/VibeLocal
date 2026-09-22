package com.vibelocal.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.vibelocal.app.R
import com.vibelocal.app.databinding.ActivityProfileBinding

class ProfileActivity : BaseActivity() {
    private lateinit var b: ActivityProfileBinding
    
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        b = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(b.root)
        
        b.txtProfileName.text = session.name()
        
        b.btnInterests.setOnClickListener { startActivity(Intent(this, InterestsActivity::class.java)) }
        b.btnNotifications.setOnClickListener { startActivity(Intent(this, NotificationsActivity::class.java)) }
        
        b.btnLanguage.setOnClickListener { toggleLanguage() }
        b.btnTheme.setOnClickListener { toggleTheme() }
        
        setupBottomNav(b.bottomNavigation, R.id.nav_profile)
        
        b.btnLogout.setOnClickListener {
            session.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

    private fun toggleLanguage() {
        val current = session.getLanguage()
        val next = if (current == "en") "tn" else "en"
        session.setLanguage(next)
        
        // Restart app to apply locale
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun toggleTheme() {
        val current = session.getTheme()
        val next = if (current == AppCompatDelegate.MODE_NIGHT_YES) 
            AppCompatDelegate.MODE_NIGHT_NO 
        else 
            AppCompatDelegate.MODE_NIGHT_YES
            
        session.setTheme(next)
        AppCompatDelegate.setDefaultNightMode(next)
    }
}
