package com.vibelocal.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vibelocal.app.R
import com.vibelocal.app.databinding.ActivitySavedBinding
import com.vibelocal.app.model.EventDto
import com.vibelocal.app.data.DbProvider
import kotlinx.coroutines.launch

class SavedActivity : BaseActivity() {
    private lateinit var b: ActivitySavedBinding
    private val a = EventAdapter(emptyList()) {
        startActivity(Intent(this, EventDetailsActivity::class.java).putExtra("eventId", it.eventId))
    }
    
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        b = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(b.root)
        
        b.recyclerSaved.layoutManager = LinearLayoutManager(this)
        b.recyclerSaved.adapter = a
        setupBottomNav(b.bottomNavigation, R.id.nav_saved)
        load()
    }
    
    override fun onResume() {
        super.onResume()
        if (::b.isInitialized) load()
    }
    
    private fun load() {
        lifecycleScope.launch {
            try {
                val saved = api.saved(session.userId())
                a.submit(saved)
                b.txtSavedCount.text = saved.size.toString()
            } catch (_: Exception) {
                val local = DbProvider.get(this@SavedActivity).eventDao().all()
                a.submit(local.map {
                    EventDto(it.eventId, it.title, it.description, it.dateTime, it.location, it.latitude, it.longitude, it.category, it.price, null)
                })
                b.txtSavedCount.text = local.size.toString()
            }
        }
    }
}
