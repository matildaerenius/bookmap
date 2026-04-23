package com.matildaerenius.bookmap.presentation.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.matildaerenius.bookmap.presentation.feature.map.MapScreen

enum class AppDestination(val label: String) {
    MAP("Karta"),
    FAVORITES("Favoriter")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToDetail: (Int) -> Unit
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(AppDestination.MAP.ordinal) }

    Scaffold(
        topBar = {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                AppDestination.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Text(
                                text = destination.label,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTabIndex) {
                AppDestination.MAP.ordinal -> {
                    MapScreen(
                        onNavigateToDetail = onNavigateToDetail
                    )
                }
                AppDestination.FAVORITES.ordinal -> {
                    // TODO: Visa din Favorit skärmen
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text("Favoriter visas nu!")
                    }
                }
            }
        }
    }
}