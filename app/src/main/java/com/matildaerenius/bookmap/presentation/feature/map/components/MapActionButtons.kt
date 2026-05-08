package com.matildaerenius.bookmap.presentation.feature.map.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.presentation.common.components.FloatingActionButtonItem

@Composable
fun MapActionButtons(
    modifier: Modifier = Modifier,
    hasLocationPermission: Boolean,
    isFilterActive: Boolean,
    onMyLocationClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (hasLocationPermission) {
            FloatingActionButtonItem(
                selected = false,
                onClick = onMyLocationClick,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.location_icon),
                        contentDescription = stringResource(id = R.string.go_to_my_location),
                        tint = Color.Black
                    )
                }
            )
        }

        FloatingActionButtonItem(
            selected = isFilterActive,
            onClick = onFilterClick,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.filter_icon),
                    contentDescription = stringResource(id = R.string.filter_map),
                    tint = Color.Black
                )
            }
        )
    }
}