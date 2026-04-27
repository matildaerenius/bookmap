package com.matildaerenius.bookmap.presentation.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.matildaerenius.bookmap.R


enum class FloatingAction(@param:StringRes val labelResId: Int, val icon: ImageVector) {
    MAP(R.string.action_map, Icons.Default.Map),
    FAVORITES(R.string.action_favorites, Icons.Default.Favorite)
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