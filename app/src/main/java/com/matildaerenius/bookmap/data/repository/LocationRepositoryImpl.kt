package com.matildaerenius.bookmap.data.repository

import com.matildaerenius.bookmap.data.remote.api.LocationApi
import com.matildaerenius.bookmap.data.mapper.toDomain
import com.matildaerenius.bookmap.domain.model.BookLocation
import com.matildaerenius.bookmap.domain.repository.LocationRepository
import com.matildaerenius.bookmap.util.DataError
import com.matildaerenius.bookmap.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val api: LocationApi
) : LocationRepository {

    override suspend fun getLocations(): Resource<List<BookLocation>> {
        return try {
            val response = api.getBookLocations()
            Resource.Success(response.map { it.toDomain() })
        } catch (e: IOException) {
            Resource.Error(DataError.NETWORK_ERROR)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                Resource.Error(DataError.NOT_FOUND)
            } else {
                Resource.Error(DataError.UNKNOWN_ERROR)
            }
        } catch (e: Exception) {
            Resource.Error(DataError.UNKNOWN_ERROR)
        }
    }
}
