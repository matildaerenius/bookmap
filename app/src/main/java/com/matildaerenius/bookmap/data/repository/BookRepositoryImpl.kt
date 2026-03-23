package com.matildaerenius.bookmap.data.repository

import com.matildaerenius.bookmap.data.mapper.toDomain
import com.matildaerenius.bookmap.data.remote.api.BookBeatApi
import com.matildaerenius.bookmap.domain.model.Book
import com.matildaerenius.bookmap.domain.repository.BookRepository
import com.matildaerenius.bookmap.util.DataError
import com.matildaerenius.bookmap.util.Resource
import com.matildaerenius.bookmap.util.safeApiCall
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api: BookBeatApi
) : BookRepository {

    override suspend fun getBooksByIds(bookIds: List<Int>): Resource<List<Book>> {
        return try {
            val books = coroutineScope {
                bookIds.map { id ->
                    async {
                        val result = safeApiCall { api.getBookById(id).toDomain() }

                        if (result is Resource.Success) {
                            result.data
                        } else {
                            null
                        }
                    }
                }.awaitAll().filterNotNull()
            }

            if (books.isEmpty() && bookIds.isNotEmpty()) {
                Resource.Error(DataError.NETWORK_ERROR)
            } else {
                Resource.Success(books)
            }

        } catch (e: Exception) {
            Resource.Error(DataError.UNKNOWN_ERROR)
        }
    }
}
