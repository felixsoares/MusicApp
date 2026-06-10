package com.mobile.felix.musicapp.feature.album.di

import com.mobile.felix.musicapp.feature.album.data.repository.AlbumRepositoryImpl
import com.mobile.felix.musicapp.feature.album.data.source.AlbumDataSourceImpl
import com.mobile.felix.musicapp.feature.album.domain.repository.AlbumRepository
import com.mobile.felix.musicapp.feature.album.domain.source.AlbumDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumModule {

    @Binds
    @Singleton
    abstract fun bindAlbumRepository(
        impl: AlbumRepositoryImpl
    ): AlbumRepository

    @Binds
    @Singleton
    abstract fun bindAlbumDataSource(
        impl: AlbumDataSourceImpl
    ): AlbumDataSource
}