package com.matildaerenius.bookmap.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.matildaerenius.bookmap.R

val HafferFontFamily = FontFamily(
    Font(R.font.haffer_bookbeat_light, FontWeight.Light),
    Font(R.font.haffer_bookbeat_regular, FontWeight.Normal),
    Font(R.font.haffer_bookbeat_bold, FontWeight.Bold)
)

val TeodorFontFamily = FontFamily(
    Font(R.font.teodor_bookbeat_regular, FontWeight.Normal),
    Font(R.font.teodor_bookbeat_bold, FontWeight.Bold)
)
val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = TeodorFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = TeodorFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = TeodorFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = HafferFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    titleLarge = TextStyle(
        fontFamily = TeodorFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = HafferFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = HafferFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    )
)