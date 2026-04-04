package com.matildaerenius.bookmap.data.mapper

import com.matildaerenius.bookmap.data.local.entity.MarkerEntity
import com.matildaerenius.bookmap.domain.model.BookMapMarker

fun BookMapMarker.toEntity(): MarkerEntity {
    return MarkerEntity(
        bookId = this.bookId,
        title = this.bookTitle,
        author = this.bookAuthor,
        description = this.description,
        coverImageUrl = this.bookImageUrl,
        locationDescription = this.locationName,
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun MarkerEntity.toDomain(): BookMapMarker {
    return BookMapMarker(
        bookId = this.bookId,
        locationName = this.locationDescription,
        latitude = this.latitude,
        longitude = this.longitude,
        description = this.description,
        bookTitle = this.title,
        bookAuthor = this.author,
        bookImageUrl = this.coverImageUrl
    )
}