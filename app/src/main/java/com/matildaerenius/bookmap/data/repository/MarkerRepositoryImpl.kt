package com.matildaerenius.bookmap.data.repository

import com.matildaerenius.bookmap.data.local.dao.MarkerDao
import com.matildaerenius.bookmap.data.mapper.toDomain
import com.matildaerenius.bookmap.data.mapper.toEntity
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MarkerRepositoryImpl @Inject constructor(
    private val markerDao: MarkerDao
) : MarkerRepository {

    override fun observeMarkers(): Flow<List<BookMapMarker>> {
        return markerDao.getAllMarkers().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun replaceCache(markers: List<BookMapMarker>) {
        markerDao.replaceAllMarkers(markers.map { it.toEntity() })
    }
}