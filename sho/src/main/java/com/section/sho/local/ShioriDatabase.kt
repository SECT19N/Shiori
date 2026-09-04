package com.section.sho.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MediaListEntryEntity::class], version = 1, exportSchema = false)
abstract class ShioriDatabase : RoomDatabase() {
    abstract fun mediaListDao(): MediaListDao
}
