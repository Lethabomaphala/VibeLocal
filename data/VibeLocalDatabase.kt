package com.vibelocal.app.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalEvent::class], version = 1, exportSchema = false)
abstract class VibeLocalDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}
