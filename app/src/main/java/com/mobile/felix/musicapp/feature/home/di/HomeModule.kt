package com.mobile.felix.musicapp.feature.home.di

import com.mobile.felix.musicapp.core.data.local.dao.SongDao
import com.mobile.felix.musicapp.core.data.remote.ApiService
import com.mobile.felix.musicapp.feature.home.data.repository.HomeRepositoryImpl
import com.mobile.felix.musicapp.feature.home.data.source.HomeLocalDataSourceImpl
import com.mobile.felix.musicapp.feature.home.data.source.HomeRemoteDataSourceImpl
import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import com.mobile.felix.musicapp.feature.home.domain.source.HomeLocalDataSource
import com.mobile.felix.musicapp.feature.home.domain.source.HomeRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideHomeRepository(
        dataSource: HomeRemoteDataSource,
        localDataSource: HomeLocalDataSource
    ): HomeRepository = HomeRepositoryImpl(dataSource, localDataSource)

    @Provides
    @Singleton
    fun provideHomeRemoteDataSource(
        apiService: ApiService
    ): HomeRemoteDataSource = HomeRemoteDataSourceImpl(apiService)

    @Provides
    @Singleton
    fun provideHomeLocalDataSource(
        songDao: SongDao
    ): HomeLocalDataSource = HomeLocalDataSourceImpl(songDao)
}

