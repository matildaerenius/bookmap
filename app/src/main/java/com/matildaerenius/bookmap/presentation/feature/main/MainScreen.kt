package com.matildaerenius.bookmap.presentation.feature.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.matildaerenius.bookmap.presentation.common.components.LocationPermissionDialog
import com.matildaerenius.bookmap.presentation.common.state.UiState
import com.matildaerenius.bookmap.presentation.feature.favorites.FavoriteScreen
import com.matildaerenius.bookmap.presentation.feature.main.components.FloatingAction
import com.matildaerenius.bookmap.presentation.feature.main.components.MainNavigationMenu
import com.matildaerenius.bookmap.presentation.feature.map.MapEvent
import com.matildaerenius.bookmap.presentation.feature.map.MapScreen
import com.matildaerenius.bookmap.presentation.feature.map.MapViewModel
import com.matildaerenius.bookmap.presentation.feature.onboarding.OnboardingScreen

@Composable
fun MainScreen(
    mapViewModel: MapViewModel = hiltViewModel()
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(FloatingAction.MAP.ordinal) }
    val state by mapViewModel.uiState.collectAsState()
    var isMapTilesLoaded by rememberSaveable { mutableStateOf(false) }
    var showOnboarding by rememberSaveable { mutableStateOf(true) }

    val isDataReady = state.markersState is UiState.Success || state.markersState is UiState.Error
    val isEverythingReady = isMapTilesLoaded && isDataReady

    val context = LocalContext.current
    val activity = context as? Activity
    var showSettingsDialog by remember { mutableStateOf(false) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        hasLocationPermission = granted

        if (!granted && activity != null) {
            val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (!shouldShowRationale) {
                showSettingsDialog = true
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    if (showSettingsDialog) {
        LocationPermissionDialog(
            context = context,
            onDismiss = { showSettingsDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (selectedTabIndex) {
                        FloatingAction.MAP.ordinal -> {
                            MapScreen(
                                viewModel = mapViewModel,
                                hasLocationPermission = hasLocationPermission,
                                onMapLoaded = { isMapTilesLoaded = true }
                            )
                        }

                        FloatingAction.FAVORITES.ordinal -> {
                            FavoriteScreen(
                                hasLocationPermission = hasLocationPermission,
                                onNavigateToMap = { bookId ->
                                    selectedTabIndex = FloatingAction.MAP.ordinal
                                    mapViewModel.onEvent(MapEvent.OnMarkerClick(bookId))
                                }
                            )
                        }
                    }
                }

                MainNavigationMenu(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 48.dp, end = 16.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = showOnboarding,
            exit = fadeOut(animationSpec = tween(1000)),
            modifier = Modifier.zIndex(1f)
        ) {
            OnboardingScreen(
                isReady = isEverythingReady,
                onFinished = { showOnboarding = false }
            )
        }
    }
}