package com.matildaerenius.bookmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.matildaerenius.bookmap.presentation.feature.map.MapScreen
import com.matildaerenius.bookmap.presentation.navigation.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Routes.Map
                    ) {

                        composable<Routes.Map> {
                            MapScreen(
                                onNavigateToDetail = { clickedBookId ->
                                    navController.navigate(Routes.Detail(bookId = clickedBookId))
                                }
                            )
                        }

                        composable<Routes.Detail> { backStackEntry ->
                            val detailArgs = backStackEntry.toRoute<Routes.Detail>()

                            println("TEST - detaljvyn ska visas nu för :) bok ID: ${detailArgs.bookId}")
                        }

                    }
                }
            }
        }
    }
}