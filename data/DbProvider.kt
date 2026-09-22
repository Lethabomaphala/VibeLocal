package com.vibelocal.app.data

import android.content.Context
import androidx.room.Room

object DbProvider {
    @Volatile private var db: VibeLocalDatabase? = null
    fun get(context: Context): VibeLocalDatabase = db ?: synchronized(this) { db ?: Room.databaseBuilder(context.applicationContext, VibeLocalDatabase::class.java, "vibelocal.db").build().also { db = it } }
}
