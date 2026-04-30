package com.matildaerenius.bookmap.presentation.feature.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource

@Composable
fun BookSummarySheet(
    marker: BookMapMarker,
    onClose: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleVisit: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
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

            Spacer(modifier = Modifier.height(32.dp))

            AsyncImage(
                model = marker.bookImageUrl,
                contentDescription = marker.bookTitle,
                modifier = Modifier
                    .size(240.dp, 240.dp)
                    .background(Color.DarkGray),
                contentScale = ContentScale.Fit
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

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onToggleVisit() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (marker.isVisited) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = stringResource(id = if(marker.isVisited) R.string.has_visit else R.string.delete),
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onToggleFavorite() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (marker.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(id = if (marker.isFavorite) R.string.remove_from_fav else R.string.add_to_fav),
                        tint = if (marker.isFavorite) Color.Red else Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }
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
            isVisited = false
        )

        BookSummarySheet(
            marker = dummyMarker,
            onToggleFavorite = {},
            onClose = {},
            onToggleVisit = {}
        )
    }
}
