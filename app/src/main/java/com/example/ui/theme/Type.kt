package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val FredokaFontFamily = FontFamily(
    Font(R.font.fredoka, FontWeight.Normal)
)

val PlusJakartaFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal)
)

fun buildAppTypography(scaleFactor: Float = 1.0f): Typography {
    return Typography(
        displaySmall = TextStyle(
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = (32 * scaleFactor).sp,
            lineHeight = (38 * scaleFactor).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = (26 * scaleFactor).sp,
            lineHeight = (32 * scaleFactor).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = (22 * scaleFactor).sp,
            lineHeight = (28 * scaleFactor).sp
        ),
        titleLarge = TextStyle(
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = (20 * scaleFactor).sp,
            lineHeight = (26 * scaleFactor).sp
        ),
        titleMedium = TextStyle(
            fontFamily = PlusJakartaFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = (16 * scaleFactor).sp,
            lineHeight = (22 * scaleFactor).sp
        ),
        titleSmall = TextStyle(
            fontFamily = PlusJakartaFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = (14 * scaleFactor).sp,
            lineHeight = (20 * scaleFactor).sp
        ),
        bodyLarge = TextStyle(
            fontFamily = PlusJakartaFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = (16 * scaleFactor).sp,
            lineHeight = (24 * scaleFactor).sp
        ),
        bodyMedium = TextStyle(
            fontFamily = PlusJakartaFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = (14 * scaleFactor).sp,
            lineHeight = (20 * scaleFactor).sp
        ),
        bodySmall = TextStyle(
            fontFamily = PlusJakartaFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = (12 * scaleFactor).sp,
            lineHeight = (18 * scaleFactor).sp
        ),
        labelLarge = TextStyle(
            fontFamily = PlusJakartaFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = (14 * scaleFactor).sp,
            lineHeight = (20 * scaleFactor).sp
        ),
        labelMedium = TextStyle(
            fontFamily = PlusJakartaFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = (12 * scaleFactor).sp,
            lineHeight = (16 * scaleFactor).sp
        ),
        labelSmall = TextStyle(
            fontFamily = PlusJakartaFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = (11 * scaleFactor).sp,
            lineHeight = (14 * scaleFactor).sp
        )
    )
}
