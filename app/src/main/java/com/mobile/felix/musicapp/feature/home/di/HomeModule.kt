package com.mobile.felix.musicapp.feature.home.di

import com.mobile.felix.musicapp.feature.home.data.repository.HomeRepositoryImpl
import com.mobile.felix.musicapp.feature.home.data.source.HomeDataSourceImpl
import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import com.mobile.felix.musicapp.feature.home.domain.source.HomeDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindHomeDataSource(
        impl: HomeDataSourceImpl
    ): HomeDataSource
}

