package com.matildaerenius.bookmap.di

import com.matildaerenius.bookmap.data.repository.BookRepositoryImpl
import com.matildaerenius.bookmap.data.repository.LocationRepositoryImpl
import com.matildaerenius.bookmap.domain.repository.BookRepository
import com.matildaerenius.bookmap.domain.repository.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBookRepository(
        bookRepositoryImpl: BookRepositoryImpl
    ): BookRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository
}