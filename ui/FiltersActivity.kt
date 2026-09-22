package com.vibelocal.app.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.vibelocal.app.databinding.ActivityFiltersBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class FiltersActivity : BaseActivity() {
    private lateinit var binding: ActivityFiltersBinding
    private var selectedDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFiltersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.spinnerCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            listOf("All", "Music", "Sports", "Food", "Education", "Culture", "Networking", "Entertainment", "Fitness"))
        binding.btnDate.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                selectedDate = "%04d-%02d-%02d".format(y, m + 1, d)
                binding.btnDate.text = selectedDate
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.seekDistance.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: android.widget.SeekBar?, p: Int, fromUser: Boolean) { binding.txtDistance.text = "Within ${p.coerceAtLeast(1)} km" }
            override fun onStartTrackingTouch(s: android.widget.SeekBar?) = Unit
            override fun onStopTrackingTouch(s: android.widget.SeekBar?) = Unit
        })
        binding.btnApplyFilters.setOnClickListener { applyFilters() }
    }
    private fun applyFilters() {
        val category = binding.spinnerCategory.selectedItem.toString().takeUnless { it == "All" }
        val maxPrice = binding.edtMaxPrice.text.toString().toDoubleOrNull()
        
        // Pass filters to SearchActivity directly (Offline friendly)
        startActivity(Intent(this@FiltersActivity, SearchActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("category", category)
            putExtra("date", selectedDate)
            putExtra("maxPrice", maxPrice?.toString())
        })
        finish()
    }
}
