package com.matildaerenius.bookmap.data.mapper

import com.matildaerenius.bookmap.data.local.entity.FavoriteWithVisit
import com.matildaerenius.bookmap.data.local.entity.BookWithDetails
import com.matildaerenius.bookmap.data.local.entity.MarkerEntity
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.model.FavoriteBook

fun BookMapMarker.toEntity(): MarkerEntity {
    return MarkerEntity(
        bookId = this.bookId,
        title = this.bookTitle,
        author = this.bookAuthor,
        description = this.description,
        coverImageUrl = this.bookImageUrl,
        locationDescription = this.locationName,
        latitude = this.latitude,
        longitude = this.longitude,
        ebook = this.ebook,
        audio = this.audio
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
        isVisited = this.visited != null,
        ebook = this.marker.ebook,
        audio = this.marker.audio
    )
}

fun FavoriteWithVisit.toDomain(): FavoriteBook {
    val mappedMarker = BookMapMarker(
        bookId = this.favorite.bookId,
        bookTitle = this.favorite.title,
        bookAuthor = this.favorite.author,
        description = this.favorite.description,
        bookImageUrl = this.favorite.imageUrl,
        locationName = this.favorite.locationName,
        latitude = this.favorite.latitude,
        longitude = this.favorite.longitude,
        ebook = this.favorite.ebook,
        audio = this.favorite.audio,
        isFavorite = true,
        isVisited = this.isVisited
    )

    return FavoriteBook(
        bookId = this.favorite.bookId,
        marker = mappedMarker
    )
}