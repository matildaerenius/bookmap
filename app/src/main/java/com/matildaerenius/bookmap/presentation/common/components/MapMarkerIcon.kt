package com.matildaerenius.bookmap.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BookMarkerIcon(
    modifier: Modifier = Modifier,
    iconSize: Dp = 18.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    iconColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(iconSize * 2)
            .shadow(4.dp, CircleShape)
            .background(backgroundColor, CircleShape)
            .border(2.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AutoStories,
            contentDescription = "Book icon",
            tint = iconColor,
            modifier = Modifier.size(iconSize)
        )
    }
}