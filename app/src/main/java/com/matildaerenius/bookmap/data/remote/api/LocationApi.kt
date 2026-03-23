package com.matildaerenius.bookmap.data.remote.api

import com.matildaerenius.bookmap.data.remote.dto.LocationDto
import retrofit2.http.GET

interface LocationApi {
    @GET("matildaerenius/6412034116187abb7f8c8699333695d4/raw/67a7da5135801b1f7c3b9cdb3d2be719ecab4598/book_locations.json")
    suspend fun getBookLocations(): List<LocationDto>
}