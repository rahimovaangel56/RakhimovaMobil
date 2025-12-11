package com.example.rakhimovakp.di

import android.content.Context
import com.example.rakhimovakp.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideCarDao(appDatabase: AppDatabase) =
        appDatabase.carDao()

    @Provides
    @Singleton
    fun provideCartDao(appDatabase: AppDatabase) =
        appDatabase.cartDao()
}