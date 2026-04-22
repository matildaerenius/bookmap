package com.matildaerenius.bookmap.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.matildaerenius.bookmap.R

@Composable
fun MapMarkerIcon(
    modifier: Modifier = Modifier,
    backgroundColor: Color = colorResource(id = R.color.marker_bg_purple),
    innerColor: Color = colorResource(id = R.color.marker_inner_purple),
    iconColor: Color = Color.Black
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .graphicsLayer {
                rotationZ = 45f
                shadowElevation = 8.dp.toPx()
                shape = RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomEndPercent = 0,
                    bottomStartPercent = 50
                )
                clip = true
            }
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer { rotationZ = -45f }
                .fillMaxSize(0.7f)
                .background(innerColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoStories,
                contentDescription = stringResource(id = R.string.marker_icon),
                tint = iconColor,
                modifier = Modifier.fillMaxSize(0.65f)
            )
        }
    }
}