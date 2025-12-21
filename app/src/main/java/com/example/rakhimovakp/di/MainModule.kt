package com.example.rakhimovakp.di

import android.content.Context
import com.example.rakhimovakp.auth.AuthManager
import com.example.rakhimovakp.data.local.dao.UserDao
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

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase) =
        appDatabase.userDao()

    @Provides
    @Singleton
    fun provideAuthManager(@ApplicationContext context: Context, userDao: UserDao) =
        AuthManager(context, userDao)

}