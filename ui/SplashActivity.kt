package com.vibelocal.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vibelocal.app.databinding.ActivitySplashBinding
import com.vibelocal.app.util.SessionManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(s: Bundle?) { super.onCreate(s); val b=ActivitySplashBinding.inflate(layoutInflater); setContentView(b.root); b.root.postDelayed({ startActivity(Intent(this, if(SessionManager(this).userId()>0) MainActivity::class.java else LoginActivity::class.java)); finish() }, 1200) }
}
