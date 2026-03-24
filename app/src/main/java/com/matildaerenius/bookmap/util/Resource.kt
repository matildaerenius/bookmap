package com.matildaerenius.bookmap.util

enum class DataError {
    NETWORK_ERROR,
    NOT_FOUND,
    UNAUTHORIZED,
    SERVER_ERROR,
    PARSING_ERROR,
    UNKNOWN_ERROR
}

sealed interface Resource<T> {
    class Success<T>(val data: T) : Resource<T>
    class Error<T>(val error: DataError, val data: T? = null) : Resource<T>
}