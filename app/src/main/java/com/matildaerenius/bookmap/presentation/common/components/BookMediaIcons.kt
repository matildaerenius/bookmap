package com.matildaerenius.bookmap.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.matildaerenius.bookmap.R

@Composable
fun BookMediaIcons(
    hasAudio: Boolean,
    hasEbook: Boolean,
    modifier: Modifier = Modifier,
    iconSize: Dp = 14.dp,
    spacing: Dp = 8.dp
) {
    if (!hasAudio && !hasEbook) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasAudio) {
            Image(
                painter = painterResource(id = R.drawable.audiobook),
                contentDescription = stringResource(id = R.string.audiobook),
                modifier = Modifier.size(iconSize)
            )
        }

        if (hasAudio && hasEbook) {
            Spacer(modifier = Modifier.width(spacing))
        }

        if (hasEbook) {
            Image(
                painter = painterResource(id = R.drawable.ebook),
                contentDescription = stringResource(id = R.string.ebook),
                modifier = Modifier.size(iconSize)
            )
        }
    }
}