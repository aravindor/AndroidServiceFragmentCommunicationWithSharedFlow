package com.dve.androidservicetest.di

import com.dve.androidservicetest.utils.ServiceCommunicator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideServiceCommunicator(): ServiceCommunicator = ServiceCommunicator()
}