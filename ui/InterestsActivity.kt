package com.vibelocal.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.vibelocal.app.databinding.ActivityInterestsBinding
import com.vibelocal.app.model.PreferenceDto
import kotlinx.coroutines.launch

class InterestsActivity : BaseActivity() {
    private lateinit var b: ActivityInterestsBinding
    override fun onCreate(s: Bundle?) { super.onCreate(s); b=ActivityInterestsBinding.inflate(layoutInflater); setContentView(b.root); b.btnContinue.setOnClickListener { save() } }
    private fun save(){ val map=listOf(b.chipMusic to "Music",b.chipSports to "Sports",b.chipFood to "Food",b.chipEducation to "Education",b.chipCulture to "Culture",b.chipNetworking to "Networking",b.chipEntertainment to "Entertainment",b.chipFitness to "Fitness"); val selected=map.filter{it.first.isChecked}.map{it.second}; lifecycleScope.launch { try{ api.updatePreferences(session.userId(), PreferenceDto("en","light",selected,null)); startActivity(Intent(this@InterestsActivity,MainActivity::class.java)); finish()}catch(_:Exception){startActivity(Intent(this@InterestsActivity,MainActivity::class.java));finish()} } }
}
