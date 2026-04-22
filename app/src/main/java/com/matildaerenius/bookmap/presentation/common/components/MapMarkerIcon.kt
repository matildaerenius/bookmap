package com.matildaerenius.bookmap.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.matildaerenius.bookmap.R

@Composable
fun MapMarkerIcon(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.map_marker),
        contentDescription = "Map marker",
        modifier = modifier
    )
}