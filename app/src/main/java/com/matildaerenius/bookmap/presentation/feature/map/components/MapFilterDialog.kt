package com.matildaerenius.bookmap.presentation.feature.map.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.presentation.feature.map.MapFilter

@Composable
fun MapFilterDialog(
    currentFilter: MapFilter,
    onFilterSelected: (MapFilter) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.filter_map)) },
        text = {
            Column {
                FilterOptionRow(
                    text = stringResource(id = R.string.show_all_books),
                    selected = currentFilter == MapFilter.ALL,
                    onClick = { onFilterSelected(MapFilter.ALL) }
                )
                FilterOptionRow(
                    text = stringResource(id = R.string.show_only_favorites),
                    selected = currentFilter == MapFilter.FAVORITES_ONLY,
                    onClick = { onFilterSelected(MapFilter.FAVORITES_ONLY) }
                )
                FilterOptionRow(
                    text = stringResource(id = R.string.show_only_visited),
                    selected = currentFilter == MapFilter.VISITED_ONLY,
                    onClick = { onFilterSelected(MapFilter.VISITED_ONLY) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.close))
            }
        }
    )
}

@Composable
private fun FilterOptionRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}