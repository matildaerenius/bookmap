package com.matildaerenius.bookmap.data.repository

import com.matildaerenius.bookmap.data.remote.api.LocationApi
import com.matildaerenius.bookmap.data.mapper.toDomain
import com.matildaerenius.bookmap.domain.model.BookLocation
import com.matildaerenius.bookmap.domain.repository.LocationRepository
import com.matildaerenius.bookmap.core.Resource
import com.matildaerenius.bookmap.core.safeApiCall
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val api: LocationApi
) : LocationRepository {

    override suspend fun getLocations(): Resource<List<BookLocation>> {
        return safeApiCall {
            api.getBookLocations().map { it.toDomain() }
        }
    }
}