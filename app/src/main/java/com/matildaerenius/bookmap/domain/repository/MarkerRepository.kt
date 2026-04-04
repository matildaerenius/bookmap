package com.matildaerenius.bookmap.domain.repository

import com.matildaerenius.bookmap.domain.model.BookMapMarker
import kotlinx.coroutines.flow.Flow

interface MarkerRepository {
    fun observeMarkers(): Flow<List<BookMapMarker>>
    suspend fun replaceCache(markers: List<BookMapMarker>)}