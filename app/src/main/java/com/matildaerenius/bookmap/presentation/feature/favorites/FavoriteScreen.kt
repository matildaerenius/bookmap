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
import androidx.compose.ui.res.colorResource
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
            .background(colorResource(id = R.color.bg_black))
            .padding(horizontal = 16.dp)
    ) {
        when (val favState = state.favoritesState) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = colorResource(id = R.color.purple_location)
                )
            }

            is UiState.Success -> {
                val favorites = favState.data

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    Text(
                        text = stringResource(id = R.string.action_favorites),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        modifier = Modifier.padding(top = 64.dp, bottom = 24.dp, start = 4.dp)
                    )

                    if (favorites.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyFavoritesState()
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 100.dp)
                        ) {
                            items(
                                items = favorites,
                                key = { favorite -> favorite.bookId }
                            ) { favorite ->
                                if (favorite.marker != null) {
                                    FavoriteItem(
                                        marker = favorite.marker,
                                        onRemove = {
                                            viewModel.onEvent(
                                                FavoriteEvent.OnRemoveFavorite(
                                                    favorite.bookId
                                                )
                                            )
                                        },
                                        onClick = { onNavigateToMap(favorite.bookId) }
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = colorResource(id = R.color.purple_location),
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
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