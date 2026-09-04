package com.section.sho.di

import com.section.ori.repository.AuthRepository
import com.section.ori.repository.MediaListRepository
import com.section.sho.repository.AuthRepositoryImpl
import com.section.sho.repository.MediaListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMediaListRepository(
        mediaListRepositoryImpl: MediaListRepositoryImpl
    ): MediaListRepository
}