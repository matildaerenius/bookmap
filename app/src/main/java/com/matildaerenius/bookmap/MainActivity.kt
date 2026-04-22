package com.matildaerenius.bookmap

import android.os.Bundle
import android.util.Log
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
import com.matildaerenius.bookmap.presentation.feature.onboarding.OnboardingScreen
import com.matildaerenius.bookmap.presentation.navigation.Routes
import com.matildaerenius.bookmap.presentation.theme.BookmapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookmapTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Routes.Onboarding
                    ) {
                        composable<Routes.Onboarding> {
                            OnboardingScreen(
                                onContinue = {
                                    navController.navigate(Routes.Map) {
                                        popUpTo(Routes.Onboarding) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable<Routes.Map> {
                            MapScreen(
                                onNavigateToDetail = { clickedBookId ->
                                    navController.navigate(Routes.Detail(bookId = clickedBookId))
                                }
                            )
                        }

                        composable<Routes.Detail> { backStackEntry ->
                            val detailArgs = backStackEntry.toRoute<Routes.Detail>()

                            Log.d("Bookmap","TEST - detaljvyn ska visas nu för :) bok ID: ${detailArgs.bookId}")
                        }

                    }
                }
            }
        }
    }
}