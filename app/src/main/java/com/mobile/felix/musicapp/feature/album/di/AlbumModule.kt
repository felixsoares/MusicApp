package com.mobile.felix.musicapp.feature.album.di

import com.mobile.felix.musicapp.core.data.remote.ApiService
import com.mobile.felix.musicapp.feature.album.data.repository.AlbumRepositoryImpl
import com.mobile.felix.musicapp.feature.album.data.source.AlbumDataSourceImpl
import com.mobile.felix.musicapp.feature.album.domain.repository.AlbumRepository
import com.mobile.felix.musicapp.feature.album.domain.source.AlbumDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlbumModule {

    @Provides
    @Singleton
    fun provideAlbumRepository(
        dataSource: AlbumDataSource
    ): AlbumRepository = AlbumRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun bindAlbumDataSource(
        apiService: ApiService
    ): AlbumDataSource = AlbumDataSourceImpl(apiService)
}