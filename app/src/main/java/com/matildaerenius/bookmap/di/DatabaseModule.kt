package com.matildaerenius.bookmap.di

import android.content.Context
import androidx.room.Room
import com.matildaerenius.bookmap.data.local.BookMapDatabase
import com.matildaerenius.bookmap.data.local.dao.FavoriteDao
import com.matildaerenius.bookmap.data.local.dao.MarkerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBookMapDatabase(
        @ApplicationContext context: Context
    ): BookMapDatabase {
        return Room.databaseBuilder(
            context,
            BookMapDatabase::class.java,
            "bookmap_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideMarkerDao(database: BookMapDatabase): MarkerDao {
        return database.markerDao
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: BookMapDatabase): FavoriteDao {
        return database.favoriteDao
    }
}