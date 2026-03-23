package com.matildaerenius.bookmap.domain.repository

import com.matildaerenius.bookmap.domain.model.BookLocation
import com.matildaerenius.bookmap.util.Resource

interface LocationRepository {
    suspend fun getLocations(): Resource<List<BookLocation>>
}