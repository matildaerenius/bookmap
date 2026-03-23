package com.matildaerenius.bookmap.data.mapper

import com.matildaerenius.bookmap.data.remote.dto.LocationDto
import com.matildaerenius.bookmap.domain.model.BookLocation

fun LocationDto.toDomain(): BookLocation {
    return BookLocation(
        bookId = this.bookId,
        locationName = this.locationName,
        latitude = this.latitude,
        longitude = this.longitude,
        description = this.description
    )
}