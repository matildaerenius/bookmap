package com.matildaerenius.bookmap.presentation.feature.onboarding

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.presentation.common.components.MapMarkerIcon
import com.matildaerenius.bookmap.presentation.common.components.ProgressBar

@Composable
fun OnboardingScreen(
    onContinue: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    var hasNavigated by remember { mutableStateOf(false) }

    val safeNavigate = {
        if (!hasNavigated) {
            hasNavigated = true
            onContinue()
        }
    }

    val progress by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2500,
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            safeNavigate()
        },
        label = "OnboardingProgress"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { safeNavigate() }
    ) {
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
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    lineHeight = 44.sp
                ),
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
                .offset(x = 30.dp, y = 30.dp)
                .size(32.dp),
            backgroundColor = colorResource(id = R.color.marker_bg_purple).copy(alpha = 0.8f)
        )

        MapMarkerIcon(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 70.dp, y = 12.dp)
                .size(40.dp),
            backgroundColor = colorResource(id = R.color.marker_bg_purple).copy(alpha = 0.9f)
        )

        MapMarkerIcon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-40).dp, y = 25.dp)
                .size(28.dp),
            backgroundColor = colorResource(id = R.color.marker_bg_purple).copy(alpha = 0.7f)
        )

        MapMarkerIcon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-100).dp, y = 30.dp)
                .size(20.dp),
            backgroundColor = colorResource(id = R.color.marker_bg_purple).copy(alpha = 0.6f)
        )

        MapMarkerIcon(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-20).dp, y = 40.dp)
                .size(110.dp),
            backgroundColor = colorResource(id = R.color.marker_bg_purple),
            iconColor = Color.Black
        )

        ProgressBar(
            progress = progress,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}