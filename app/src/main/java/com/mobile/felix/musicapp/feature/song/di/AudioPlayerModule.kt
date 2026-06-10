package com.mobile.felix.musicapp.feature.song.di

import android.content.Context
import com.mobile.felix.musicapp.feature.song.data.player.AudioPlayerImpl
import com.mobile.felix.musicapp.feature.song.domain.player.AudioPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AudioPlayerModule {

    @Provides
    @ViewModelScoped
    fun provideAudioPlayer(
        @ApplicationContext context: Context
    ): AudioPlayer = AudioPlayerImpl(context)
}

