package com.example.storemanager.module

import android.content.Context
import androidx.room.Room
import com.example.storemanager.db.StoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideStoreDatabase(@ApplicationContext context: Context) : StoreDatabase{
        return Room.databaseBuilder(
            context,
            StoreDatabase::class.java,
            "StoreDatabase"
        ).build()
    }
}