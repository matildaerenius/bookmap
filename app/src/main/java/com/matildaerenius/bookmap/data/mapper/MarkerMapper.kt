package com.matildaerenius.bookmap.data.mapper

import com.matildaerenius.bookmap.data.local.entity.BookWithDetails
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

fun BookWithDetails.toDomain(): BookMapMarker {
    return BookMapMarker(
        bookId = this.marker.bookId,
        locationName = this.marker.locationDescription,
        latitude = this.marker.latitude,
        longitude = this.marker.longitude,
        description = this.marker.description,
        bookTitle = this.marker.title,
        bookAuthor = this.marker.author,
        bookImageUrl = this.marker.coverImageUrl,
        isFavorite = this.favorite != null,
        isVisited = this.visited != null
    )
}