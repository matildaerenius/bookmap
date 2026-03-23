package com.matildaerenius.bookmap

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.matildaerenius.bookmap.domain.repository.BookRepository
import com.matildaerenius.bookmap.domain.repository.LocationRepository
import com.matildaerenius.bookmap.presentation.theme.BookmapTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var bookRepository: BookRepository

    @Inject
    lateinit var locationRepository: LocationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        testFetchBooks()
        testFetchLocations()

        setContent {
            BookmapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text("Kolla Logcat för att se om det funkar")
                }
            }
        }
    }

    private fun testFetchBooks() {
        lifecycleScope.launch {
            val bookIds = listOf(1759530)

            Log.d("BookTest", "Hämtar böcker...")
            val result = bookRepository.getBooksByIds(bookIds)

            Log.d("BookTest", "Resultat: $result")
        }
    }

    private fun testFetchLocations() {
        lifecycleScope.launch {
            Log.d("LocationTest", "Hämta platser från GitHub Gist...")

            val result = locationRepository.getLocations()

            Log.d("LocationTest", "Resultat: $result")
        }
    }
}