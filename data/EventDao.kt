package com.vibelocal.app.data

import androidx.room.*

@Dao
interface EventDao {
    @Query("SELECT * FROM saved_events ORDER BY dateTime") suspend fun all(): List<LocalEvent>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(event: LocalEvent)
    @Delete suspend fun delete(event: LocalEvent)
    @Query("DELETE FROM saved_events WHERE eventId = :id") suspend fun deleteById(id: Int)
}
