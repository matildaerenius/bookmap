package com.matildaerenius.bookmap.presentation.feature.onboarding

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.presentation.common.components.MapMarkerIcon
import com.matildaerenius.bookmap.presentation.common.components.ProgressBar

@Composable
fun OnboardingScreen(
    isReady: Boolean,
    onFinished: () -> Unit
) {
    var targetProgress by remember { mutableFloatStateOf(0f) }
    var hasFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        targetProgress = 0.8f
    }

    LaunchedEffect(isReady) {
        if (isReady) {
            targetProgress = 1f
        }
    }

    val progress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = if (targetProgress == 1f) 500 else 3000,
            easing = FastOutSlowInEasing
        ),
        finishedListener = { currentValue ->
            if (currentValue >= 0.99f && !hasFinished) {
                hasFinished = true
                onFinished()
            }
        },
        label = "OnboardingProgress"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.stockholm),
            contentDescription = stringResource(id = R.string.onboarding_image_desc),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.85f),
                            Color.Black.copy(alpha = 0.5f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.onboarding_title),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.onboarding_description),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }

        MapMarkerIcon(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 30.dp, y = 43.dp)
                .size(32.dp)
        )
        MapMarkerIcon(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 70.dp, y = 35.dp)
                .size(40.dp)
        )
        MapMarkerIcon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-40).dp, y = 40.dp)
                .size(28.dp)
        )
        MapMarkerIcon(
            isFavorite = true,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-95).dp, y = 40.dp)
                .size(20.dp)
        )
        MapMarkerIcon(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-15).dp, y = 53.dp)
                .size(110.dp)
        )

        ProgressBar(
            progress = progress,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnboardingScreenPreview() {
    MaterialTheme {
        OnboardingScreen(
            isReady = false,
            onFinished = {}
        )
    }
}