package com.vibelocal.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_events")
data class LocalEvent(@PrimaryKey val eventId: Int, val title: String, val description: String, val dateTime: String, val location: String, val latitude: Double, val longitude: Double, val category: String, val price: Double)
