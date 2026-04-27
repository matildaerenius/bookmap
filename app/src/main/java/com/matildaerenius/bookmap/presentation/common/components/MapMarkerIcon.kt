package com.matildaerenius.bookmap.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.matildaerenius.bookmap.R

@Composable
fun MapMarkerIcon(
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false
) {

    val iconResId = if (isFavorite) {
        R.drawable.fav_map_marker
    } else {
        R.drawable.book_map_marker
    }

    Image(
        painter = painterResource(id = iconResId),
        contentDescription = stringResource(id = R.string.map_marker),
        modifier = modifier
    )
}