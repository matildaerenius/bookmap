package com.matildaerenius.bookmap.data.remote.api

import com.matildaerenius.bookmap.data.remote.dto.BookDto
import retrofit2.http.GET
import retrofit2.http.Path

interface BookBeatApi {

    @GET("api/books/{bookId}")
    suspend fun getBookById(
        @Path("bookId") id: Int
    ): BookDto

}