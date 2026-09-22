package com.vibelocal.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.vibelocal.app.databinding.ActivityEventDetailsBinding
import com.vibelocal.app.model.EventDto
import com.vibelocal.app.util.MockData
import com.vibelocal.app.util.toLocal
import com.vibelocal.app.data.DbProvider
import kotlinx.coroutines.launch

class EventDetailsActivity : BaseActivity() {
    private lateinit var b: ActivityEventDetailsBinding
    private var eventId = 0
    private var currentEvent: EventDto? = null

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        b = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(b.root)
        
        eventId = intent.getIntExtra("eventId", 0)
        load()
        
        b.btnSave.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val e = currentEvent ?: return@launch
                    // Local save logic for offline mode
                    DbProvider.get(this@EventDetailsActivity).eventDao().upsert(e.toLocal())
                    Toast.makeText(this@EventDetailsActivity, "Event saved to your collection", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@EventDetailsActivity, "Could not save event", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        b.btnShare.setOnClickListener {
            val e = currentEvent ?: return@setOnClickListener
            val shareText = "Check out this VibeLocal event: ${e.title}\n" +
                            "Category: ${e.category}\n" +
                            "Date: ${e.dateTime.replace('T', ' ')}\n" +
                            "Location: ${e.location}\n" +
                            "Price: ${if (e.price <= 0) "Free" else "R" + e.price.toInt()}\n" +
                            "Download VibeLocal to discover more!"
            
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, e.title)
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(intent, "Share event via"))
        }
    }

    private fun load() {
        lifecycleScope.launch {
            try {
                val e = api.event(eventId)
                currentEvent = e
                displayEvent(e)
            } catch (_: Exception) {
                val e = MockData.events.find { it.eventId == eventId }
                if (e != null) {
                    currentEvent = e
                    displayEvent(e)
                }
            }
        }
    }

    private fun displayEvent(e: EventDto) {
        b.txtTitle.text = e.title
        b.txtDescription.text = e.description
        b.txtDateTime.text = e.dateTime.replace('T', ' ')
        b.txtLocation.text = e.location
        b.txtPrice.text = if (e.price <= 0) "Free" else "R${e.price.toInt()}"
        b.txtMatch.text = e.vibeMatch?.let { "$it% Vibe Match" } ?: "VibeLocal"
    }
}
