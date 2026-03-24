package com.matildaerenius.bookmap.util

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

suspend inline fun <T> safeApiCall(crossinline apiCall: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(apiCall())
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        Resource.Error(DataError.NETWORK_ERROR)
    } catch (e: HttpException) {
        when (e.code()) {
            401, 403 -> Resource.Error(DataError.UNAUTHORIZED)
            404 -> Resource.Error(DataError.NOT_FOUND)
            in 500..599 -> Resource.Error(DataError.SERVER_ERROR)
            else -> Resource.Error(DataError.UNKNOWN_ERROR)
        }
    } catch (e: Exception) {
        Resource.Error(DataError.PARSING_ERROR)
    }
}