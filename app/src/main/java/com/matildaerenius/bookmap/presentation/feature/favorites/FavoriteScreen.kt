package com.matildaerenius.bookmap.presentation.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.presentation.common.state.UiState
import com.matildaerenius.bookmap.presentation.feature.favorites.components.FavoriteItem

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    onNavigateToMap: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFC084FC)
                )
            }

            is UiState.Success -> {
                val favorites = state.data

                if (favorites.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.saved_favorites),
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(top = 80.dp, bottom = 100.dp)
                    ) {
                        items(
                            items = favorites,
                            key = { favorite -> favorite.bookId }
                        ) { favorite ->
                            FavoriteItem(
                                imageUrl = favorite.marker?.bookImageUrl,
                                bookTitle = favorite.marker?.bookTitle ?: "Okänd titel",
                                author = favorite.marker?.bookAuthor ?: "Okänd författare",
                                locationName = favorite.marker?.locationName ?: "Okänd plats",
                                onRemove = { viewModel.onEvent(FavoriteEvent.OnRemoveFavorite(favorite.bookId)) },
                                onClick = { onNavigateToMap(favorite.bookId) }
                            )
                        }
                    }
                }
            }

            is UiState.Error -> {
                Text(
                    text = stringResource(id = R.string.fav_could_not_load),
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}