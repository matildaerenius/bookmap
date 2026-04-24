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
import com.matildaerenius.bookmap.presentation.common.components.MainScreen
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
                        startDestination = Routes.Main
                    ) {
                        composable<Routes.Main> {
                            MainScreen()
                        }
                    }
                }
            }
        }
    }
}