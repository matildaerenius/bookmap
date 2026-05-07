package com.matildaerenius.bookmap.presentation.feature.favorites.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.presentation.common.components.BookMediaIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteItem(
    marker: BookMapMarker,
    distanceText: String? = null,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onRemove()
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.red))
                    .padding(end = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.remove),
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        content = {
            Surface(
                color = colorResource(id = R.color.bg_grey),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .background(colorResource(id = R.color.fav_card_bg))
                            .padding(8.dp)
                    ) {
                        AsyncImage(
                            model = marker.bookImageUrl,
                            contentDescription = stringResource(id = R.string.book_cover),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(96.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = marker.bookTitle,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = marker.bookAuthor,
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = stringResource(id = R.string.map_marker),
                                tint = colorResource(id = R.color.purple_location),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = marker.locationName,
                                color = colorResource(id = R.color.purple_location),
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.height(96.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (distanceText != null) {
                            Text(
                                text = distanceText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.LightGray
                            )
                        } else {
                            Spacer(modifier = Modifier.height(1.dp))
                        }

                        BookMediaIcons(
                            hasAudio = marker.audio,
                            hasEbook = marker.ebook,
                            iconSize = 16.dp,
                            spacing = 8.dp
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun FavoriteItemPreview() {
    MaterialTheme {
        val dummyMarker = BookMapMarker(
            bookId = 1,
            locationName = "Gamla Stan",
            latitude = 59.3257,
            longitude = 18.0709,
            bookTitle = "Män som hatar kvinnor",
            bookAuthor = "Stieg Larsson",
            description = "En kort testbeskrivning",
            bookImageUrl = "",
            isFavorite = true,
            isVisited = false,
            ebook = true,
            audio = true
        )

        Box(modifier = Modifier.padding(16.dp)) {
            FavoriteItem(
                marker = dummyMarker,
                onRemove = {},
                onClick = {}
            )
        }
    }
}