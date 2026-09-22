package com.vibelocal.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vibelocal.app.R
import com.vibelocal.app.databinding.ActivitySearchBinding
import com.vibelocal.app.util.MockData
import kotlinx.coroutines.launch

class SearchActivity : BaseActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val adapter = EventAdapter(emptyList()) { event ->
        startActivity(Intent(this, EventDetailsActivity::class.java).putExtra("eventId", event.eventId))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerSearchResults.layoutManager = LinearLayoutManager(this)
        binding.recyclerSearchResults.adapter = adapter
        binding.btnFilter.setOnClickListener { startActivity(Intent(this, FiltersActivity::class.java)) }
        binding.edtSearch.setOnEditorActionListener { _, _, _ -> search(); true }
        setupBottomNav(binding.bottomNavigation, R.id.nav_search)
        search()
    }
    override fun onResume() { super.onResume(); if (::binding.isInitialized) search() }
    private fun search() {
        val keyword = binding.edtSearch.text.toString().trim().ifBlank { null }
        val category = intent.getStringExtra("category")
        val date = intent.getStringExtra("date")
        val maxPrice = intent.getStringExtra("maxPrice")?.toDoubleOrNull()
        lifecycleScope.launch {
            try { 
                adapter.submit(api.search(keyword, category, date, maxPrice, userId = session.userId())) 
            } catch (_: Exception) { 
                // Local search logic
                var results = MockData.events
                if (keyword != null) {
                    results = results.filter { it.title.contains(keyword, true) || it.description.contains(keyword, true) }
                }
                if (category != null) {
                    results = results.filter { it.category.equals(category, true) }
                }
                if (date != null) {
                    results = results.filter { it.dateTime.startsWith(date) }
                }
                if (maxPrice != null) {
                    results = results.filter { it.price <= maxPrice }
                }
                adapter.submit(results)
                Toast.makeText(this@SearchActivity, "Offline mode: Showing local matches", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
