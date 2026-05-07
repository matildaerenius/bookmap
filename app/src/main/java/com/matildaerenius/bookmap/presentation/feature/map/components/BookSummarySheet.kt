package com.matildaerenius.bookmap.presentation.feature.map.components

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.presentation.common.components.BookMediaIcons
import androidx.core.net.toUri
import com.matildaerenius.bookmap.presentation.common.components.FloatingActionButtonItem
import com.matildaerenius.bookmap.presentation.feature.map.MapConstants

@Composable
fun BookSummarySheet(
    marker: BookMapMarker,
    distanceText: String? = null,
    onClose: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleVisit: () -> Unit
) {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        colorResource(id = R.color.bg_black_gradient_1),
                        colorResource(id = R.color.bg_black_gradient_2),
                        colorResource(id = R.color.bg_black_gradient_3),
                    ),
                    startY = 200f,
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.close),
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.End)
                    .size(32.dp)
                    .clickable { onClose() }
            )

            Text(
                text = marker.locationName,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (distanceText != null || marker.audio || marker.ebook) {
                Row(
                    modifier = Modifier
                        .width(240.dp)
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (distanceText != null) {
                        Text(
                            text = distanceText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    BookMediaIcons(
                        hasAudio = marker.audio,
                        hasEbook = marker.ebook,
                        iconSize = 14.dp,
                        spacing = 10.dp
                    )
                }
            }

            AsyncImage(
                model = marker.bookImageUrl,
                contentDescription = marker.bookTitle,
                modifier = Modifier
                    .size(240.dp, 240.dp)
                    .clip((RoundedCornerShape(4.dp)))
                    .background(Color.Transparent),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = marker.description,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButtonItem(
                    selected = false,
                    buttonSize = 68.dp,
                    onClick = { onToggleVisit() },
                    icon = {
                        Icon(
                            painter = painterResource(id = if (marker.isVisited) R.drawable.check_icon else R.drawable.add_icon),
                            contentDescription = stringResource(id = if (marker.isVisited) R.string.has_visit else R.string.unmark_visit),
                            tint = Color.Black
                        )
                    }
                )

                Spacer(modifier = Modifier.width(20.dp))

                FloatingActionButtonItem(
                    selected = false,
                    buttonSize = 68.dp,
                    onClick = { onToggleFavorite() },
                    icon = {
                        Icon(
                            painter = painterResource(id = if (marker.isFavorite) R.drawable.filled_heart_icon else R.drawable.bigger_heart_icon),
                            contentDescription = stringResource(id = if (marker.isFavorite) R.string.remove_from_fav else R.string.add_to_fav),
                            tint = Color.Unspecified
                        )
                    }
                )

                Spacer(modifier = Modifier.width(20.dp))

                FloatingActionButtonItem(
                    selected = false,
                    buttonSize = 68.dp,
                    onClick = {
                        val uri =
                            MapConstants.getNavigationUri(marker.latitude, marker.longitude).toUri()
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage(MapConstants.GOOGLE_MAPS_PACKAGE)

                        try {
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            val fallbackUri =
                                MapConstants.getFallbackWebUrl(marker.latitude, marker.longitude)
                                    .toUri()
                            val webIntent = Intent(Intent.ACTION_VIEW, fallbackUri)
                            context.startActivity(webIntent)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Directions,
                            contentDescription = stringResource(id = R.string.map_directions),
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun BookSummarySheetPreview() {
    MaterialTheme {
        val dummyMarker = BookMapMarker(
            bookId = 1,
            locationName = "Gamla Stan",
            latitude = 59.3257,
            longitude = 18.0709,
            bookTitle = "Män som hatar kvinnor",
            bookAuthor = "Stieg Larsson",
            description = "Den ryska prickskytten Sokol jagar Leila Bolt genom de trånga gränderna i Gamla Stan i en livsfarlig katt och råtta lek.",
            bookImageUrl = "",
            isFavorite = false,
            isVisited = false,
            ebook = true,
            audio = true
        )

        BookSummarySheet(
            marker = dummyMarker,
            onToggleFavorite = {},
            onClose = {},
            onToggleVisit = {}
        )
    }
}
