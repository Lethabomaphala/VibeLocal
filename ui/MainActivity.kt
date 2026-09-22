package com.vibelocal.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vibelocal.app.R
import com.vibelocal.app.databinding.ActivityHomeBinding
import com.vibelocal.app.util.MockData
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    private lateinit var b: ActivityHomeBinding
    private val nearbyAdapter=EventAdapter(emptyList()){ startActivity(Intent(this,EventDetailsActivity::class.java).putExtra("eventId",it.eventId)) }
    private val recommendedAdapter=EventAdapter(emptyList()){ startActivity(Intent(this,EventDetailsActivity::class.java).putExtra("eventId",it.eventId)) }
    
    override fun onCreate(s: Bundle?) { 
        super.onCreate(s)
        b = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(b.root)
        
        b.txtGreeting.text = "Hi ${session.name()} 👋"
        b.recyclerNearby.layoutManager = LinearLayoutManager(this)
        b.recyclerNearby.adapter = nearbyAdapter
        b.recyclerRecommended.layoutManager = LinearLayoutManager(this)
        b.recyclerRecommended.adapter = recommendedAdapter
        
        b.searchBar.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
        b.btnNotifications.setOnClickListener { startActivity(Intent(this, NotificationsActivity::class.java)) }
        
        setupBottomNav(b.bottomNavigation, R.id.nav_home)
        
        load()
    }
    
    private fun load() {
        lifecycleScope.launch {
            try {
                val events = api.events(session.userId())
                nearbyAdapter.submit(events)
                recommendedAdapter.submit(events)
            } catch (_: Exception) {
                val events = MockData.events
                nearbyAdapter.submit(events)
                recommendedAdapter.submit(events)
            }
        }
    }
}
