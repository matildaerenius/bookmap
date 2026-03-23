package com.matildaerenius.bookmap.util

enum class DataError {
    NETWORK_ERROR,
    NOT_FOUND,
    UNAUTHORIZED,
    SERVER_ERROR,
    PARSING_ERROR,
    UNKNOWN_ERROR
}

sealed class Resource<T>(val data: T? = null, val error: DataError? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(error: DataError, data: T? = null) : Resource<T>(data, error)
}