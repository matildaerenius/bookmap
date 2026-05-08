package com.matildaerenius.bookmap.presentation.feature.main.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.presentation.common.components.FloatingActionButtonItem

sealed class IconType {
    data class Vector(val imageVector: ImageVector) : IconType()
    data class Drawable(val resId: Int) : IconType()
}

enum class FloatingAction(@StringRes val labelResId: Int, val icon: IconType) {
    MAP(R.string.action_map, IconType.Drawable(R.drawable.map_icon)),
    FAVORITES(R.string.action_favorites, IconType.Drawable(R.drawable.heart_icon))
}

@Composable
fun MainNavigationMenu(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FloatingAction.entries.forEachIndexed { index, action ->
            FloatingActionButtonItem(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                icon = {
                    when (val iconType = action.icon) {
                        is IconType.Vector -> {
                            Icon(
                                imageVector = iconType.imageVector,
                                contentDescription = stringResource(id = action.labelResId),
                                tint = Color.Black,
                            )
                        }

                        is IconType.Drawable -> {
                            Icon(
                                painter = painterResource(id = iconType.resId),
                                contentDescription = stringResource(id = action.labelResId),
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            )
        }
    }
}