package com.section.sho.di

import android.content.Context
import androidx.room.Room
import com.section.sho.local.MediaListDao
import com.section.sho.local.ShioriDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideShioriDatabase(@ApplicationContext context: Context): ShioriDatabase {
        return Room.databaseBuilder(context, ShioriDatabase::class.java, "shiori.db").build()
    }

    @Provides
    @Singleton
    fun provideMediaListDao(database: ShioriDatabase): MediaListDao {
        return database.mediaListDao()
    }
}
