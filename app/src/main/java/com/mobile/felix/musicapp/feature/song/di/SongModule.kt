package com.mobile.felix.musicapp.feature.song.di

import com.mobile.felix.musicapp.feature.song.data.player.AudioPlayerImpl
import com.mobile.felix.musicapp.feature.song.data.repository.SongRepositoryImpl
import com.mobile.felix.musicapp.feature.song.data.source.SongDataSourceImpl
import com.mobile.felix.musicapp.feature.song.domain.player.AudioPlayer
import com.mobile.felix.musicapp.feature.song.domain.repository.SongRepository
import com.mobile.felix.musicapp.feature.song.domain.source.SongDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SongModule {

    @Binds
    @Singleton
    abstract fun bindSongRepository(
        impl: SongRepositoryImpl
    ): SongRepository

    @Binds
    @Singleton
    abstract fun bindSongDataSource(
        impl: SongDataSourceImpl
    ): SongDataSource

    @Binds
    @Singleton
    abstract fun bindAudioPlayer(
        impl: AudioPlayerImpl
    ): AudioPlayer
}

