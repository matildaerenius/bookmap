package com.matildaerenius.bookmap.data.repository

import android.util.Log
import com.matildaerenius.bookmap.data.mapper.toDomain
import com.matildaerenius.bookmap.data.remote.api.BookBeatApi
import com.matildaerenius.bookmap.domain.model.Book
import com.matildaerenius.bookmap.domain.repository.BookRepository
import com.matildaerenius.bookmap.util.DataError
import com.matildaerenius.bookmap.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import retrofit2.HttpException
import java.io.IOException

class BookRepositoryImpl @Inject constructor(
    private val api: BookBeatApi
) : BookRepository {

    override suspend fun getBooksByIds(bookIds: List<Int>): Resource<List<Book>> {
        return try {
            val books = coroutineScope {
                bookIds.map { id ->
                    async {
                        try {
                            api.getBookById(id).toDomain()
                        } catch (e: IOException) {
                            Log.e("BookRepositoryImpl", "Network error for book id:: $id", e)
                            null
                        } catch (e: HttpException) {
                            Log.e("BookRepositoryImpl", "Http error ${e.code()} for book id: $id", e)
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

        } catch (e: IOException) {
            Resource.Error(DataError.NETWORK_ERROR)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                Resource.Error(DataError.NOT_FOUND)
            } else {
                Resource.Error(DataError.UNKNOWN_ERROR)
            }
        } catch (e: Exception) {
            Log.e("BookRepositoryImpl", "Unknown error", e)
            Resource.Error(DataError.UNKNOWN_ERROR)
        }
    }
}
