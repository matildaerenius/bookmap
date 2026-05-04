package com.matildaerenius.bookmap.data.repository

import com.matildaerenius.bookmap.data.local.dao.MarkerDao
import com.matildaerenius.bookmap.data.local.dao.VisitedDao
import com.matildaerenius.bookmap.data.local.entity.VisitedEntity
import com.matildaerenius.bookmap.data.mapper.toDomain
import com.matildaerenius.bookmap.data.mapper.toEntity
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MarkerRepositoryImpl @Inject constructor(
    private val markerDao: MarkerDao,
    private val visitedDao: VisitedDao
) : MarkerRepository {

    override fun observeMarkers(): Flow<List<BookMapMarker>> {
        return markerDao.getMarkersWithDetails().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun upsertMarkers(markers: List<BookMapMarker>) {
        val localIds = markerDao.getAllMarkerIds()
        val incomingIds = markers.map { it.bookId }
        val idsToDelete = localIds.filterNot { it in incomingIds }

        if (idsToDelete.isNotEmpty()) {
            markerDao.deleteMarkersByIds(idsToDelete)
        }

        markerDao.upsertMarkers(markers.map { it.toEntity() })
    }

    override suspend fun updateVisitedStatus(bookId: Int, isVisited: Boolean) {
        if (isVisited) {
            visitedDao.insertVisited(VisitedEntity(bookId = bookId))
        } else {
            visitedDao.deleteVisited(bookId)
        }
    }
}