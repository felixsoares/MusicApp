package com.mobile.felix.musicapp.feature.song.di

import android.content.Context
import com.mobile.felix.musicapp.core.data.local.dao.SongDao
import com.mobile.felix.musicapp.feature.song.data.player.AudioPlayerImpl
import com.mobile.felix.musicapp.feature.song.data.repository.SongRepositoryImpl
import com.mobile.felix.musicapp.feature.song.data.source.SongLocalDataSourceImpl
import com.mobile.felix.musicapp.feature.song.domain.player.AudioPlayer
import com.mobile.felix.musicapp.feature.song.domain.repository.SongRepository
import com.mobile.felix.musicapp.feature.song.domain.source.SongLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SongModule {

    @Provides
    @Singleton
    fun provideSongRepository(
        dataSource: SongLocalDataSource
    ): SongRepository = SongRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideSongLocalDataSource(
        songDao: SongDao
    ): SongLocalDataSource = SongLocalDataSourceImpl(songDao)

    @Provides
    @Singleton
    fun provideAudioPlayer(
        @ApplicationContext context: Context
    ): AudioPlayer = AudioPlayerImpl(context)
}

