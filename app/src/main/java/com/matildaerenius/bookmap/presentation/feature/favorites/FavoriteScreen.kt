package com.matildaerenius.bookmap.presentation.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.matildaerenius.bookmap.presentation.feature.favorites.components.EmptyFavoritesState
import com.matildaerenius.bookmap.presentation.feature.favorites.components.FavoriteItem

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    onNavigateToMap: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        when (val favState = state.favoritesState) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFC084FC)
                )
            }

            is UiState.Success -> {
                val favorites = favState.data

                if (favorites.isEmpty()) {
                    EmptyFavoritesState(modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(top = 100.dp, bottom = 100.dp)
                    ) {
                        item {
                            Text(
                                text = stringResource(id = R.string.action_favorites),
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 24.dp, start = 4.dp)
                            )
                        }
                        items(
                            items = favorites,
                            key = { favorite -> favorite.bookId }
                        ) { favorite ->
                            FavoriteItem(
                                imageUrl = favorite.marker?.bookImageUrl,
                                bookTitle = favorite.marker?.bookTitle
                                    ?: stringResource(id = R.string.unknown_title),
                                author = favorite.marker?.bookAuthor
                                    ?: stringResource(id = R.string.unknown_author),
                                locationName = favorite.marker?.locationName
                                    ?: stringResource(id = R.string.unknown_location),
                                onRemove = {
                                    viewModel.onEvent(
                                        FavoriteEvent.OnRemoveFavorite(
                                            favorite.bookId
                                        )
                                    )
                                },
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