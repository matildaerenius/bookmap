package com.matildaerenius.bookmap.presentation.feature.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.matildaerenius.bookmap.domain.model.Book
import com.matildaerenius.bookmap.presentation.common.components.BookCard
import com.matildaerenius.bookmap.presentation.common.components.FullScreenLoadingIndicator
import com.matildaerenius.bookmap.presentation.common.state.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bokdetaljer") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Gå tillbaka"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    FullScreenLoadingIndicator()
                }
                is UiState.Error -> {
                    val message = (uiState as UiState.Error).message
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.onEvent(DetailEvent.OnRetryClick) }) {
                            Text("Försök igen")
                        }
                    }
                }
                is UiState.Success -> {
                    val book = (uiState as UiState.Success).data
                    DetailContent(book = book)
                }
            }
        }
    }
}

@Composable
private fun DetailContent(book: Book) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BookCard(
            title = book.title,
            author = book.author,
            modifier = Modifier,
            imageUrl = book.imageUrl
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Om boken",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )

//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = ,
//            style = MaterialTheme.typography.bodyLarge,
//            color = Color.DarkGray
//        )
    }
}
