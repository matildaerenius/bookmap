package com.matildaerenius.bookmap.presentation.feature.favorites

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.core.getFormattedDistance
import com.matildaerenius.bookmap.presentation.common.state.UiState
import com.matildaerenius.bookmap.presentation.feature.favorites.components.EmptyFavoritesState
import com.matildaerenius.bookmap.presentation.feature.favorites.components.FavoriteItem

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    hasLocationPermission: Boolean,
    onNavigateToMap: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val fusedLocationClient = remember {
        getFusedLocationProviderClient(context)
    }
    var userLocation by remember {
        mutableStateOf<Location?>(null)
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            try {
                @SuppressLint("MissingPermission")
                val locationTask = fusedLocationClient.lastLocation
                locationTask.addOnSuccessListener { location ->
                    userLocation = location
                }
            } catch (e: SecurityException) {
                Log.e("BookMap", "Saknar rättighet för plats", e)
            }
        }
    }

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

                    var showClearDialog by remember { mutableStateOf(false) }

                    Text(
                        text = stringResource(id = R.string.action_favorites),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        modifier = Modifier.padding(top = 64.dp, bottom = 16.dp, start = 4.dp)
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${favorites.size} ${stringResource(id = R.string.saved)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.LightGray
                            )

                            TextButton(
                                onClick = { showClearDialog = true },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(id = R.string.clear_all),
                                    tint = colorResource(id = R.color.purple_location),
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = stringResource(id = R.string.clear_all),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colorResource(id = R.color.purple_location)
                                )
                            }
                        }

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
                                    val distanceText = if (userLocation != null) {
                                        getFormattedDistance(
                                            userLat = userLocation!!.latitude,
                                            userLng = userLocation!!.longitude,
                                            markerLat = favorite.marker.latitude,
                                            markerLng = favorite.marker.longitude
                                        )
                                    } else {
                                        null
                                    }
                                    FavoriteItem(
                                        marker = favorite.marker,
                                        distanceText = distanceText,
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

                    if (showClearDialog) {
                        AlertDialog(
                            onDismissRequest = { showClearDialog = false },
                            title = { Text(stringResource(id = R.string.clear_fav)) },
                            text = { Text(stringResource(id = R.string.fav_alertdialog)) },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        viewModel.onEvent(FavoriteEvent.OnRemoveAllFavorites)
                                        showClearDialog = false
                                    }
                                ) {
                                    Text(
                                        stringResource(id = R.string.delete),
                                        color = colorResource(id = R.color.red)
                                    )
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showClearDialog = false }
                                ) {
                                    Text(stringResource(id = R.string.cancel), color = Color.Black)
                                }
                            }
                        )
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