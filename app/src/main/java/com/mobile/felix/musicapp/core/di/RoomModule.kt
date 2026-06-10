package com.mobile.felix.musicapp.core.di

import android.content.Context
import androidx.room.Room
import com.mobile.felix.musicapp.core.data.local.SongDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    const val MUSICAPP_DATABASE_NAME = "musicapp_database"

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        SongDatabase::class.java,
        MUSICAPP_DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideMovieDao(
        database: SongDatabase
    ) = database.songDao()

}