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
import com.matildaerenius.bookmap.domain.usecase.GetBookMarkersUseCase
import com.matildaerenius.bookmap.presentation.theme.BookmapTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getBookMarkersUseCase: GetBookMarkersUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        testFetchCombinedData()

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

    private fun testFetchCombinedData() {
        lifecycleScope.launch {
            Log.d("UseCaseTest", "Börjar hämta och kombinera data...")

            val result = getBookMarkersUseCase()

            Log.d("UseCaseTest", "Resultat: $result")
        }
    }
}