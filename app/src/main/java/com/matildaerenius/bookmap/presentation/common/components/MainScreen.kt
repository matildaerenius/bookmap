package com.matildaerenius.bookmap.presentation.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.matildaerenius.bookmap.presentation.feature.map.MapScreen
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.presentation.feature.favorites.FavoriteScreen
import com.matildaerenius.bookmap.presentation.feature.map.MapEvent
import com.matildaerenius.bookmap.presentation.feature.map.MapViewModel

enum class FloatingAction(@param:StringRes val labelResId: Int, val icon: ImageVector) {
    MAP(R.string.action_map, Icons.Default.Map),
    FAVORITES(R.string.action_favorites, Icons.Default.Favorite)
}

@Composable
fun MainScreen(
    mapViewModel: MapViewModel = hiltViewModel()
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(FloatingAction.MAP.ordinal) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (selectedTabIndex) {
                    FloatingAction.MAP.ordinal -> {
                        MapScreen(viewModel = mapViewModel)
                    }
                    FloatingAction.FAVORITES.ordinal -> {
                        FavoriteScreen(
                            onNavigateToMap = { bookId ->
                                selectedTabIndex = FloatingAction.MAP.ordinal

                                mapViewModel.onEvent(MapEvent.OnMarkerClick(bookId))
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 48.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FloatingAction.entries.forEachIndexed { index, action ->
                    FloatingActionButtonItem(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                        },
                        icon = {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = stringResource(id = action.labelResId),
                                tint = Color.Black
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingActionButtonItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .shadow(4.dp, CircleShape)
            .clip(CircleShape)
            .background(if (selected) Color(0xFFE0E0E0) else Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}