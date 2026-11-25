package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightMeadowScheme = lightColorScheme(
    primary = MintPrimary,
    onPrimary = Color.White,
    primaryContainer = MintContainer,
    onPrimaryContainer = OnMintContainer,
    secondary = CoralSecondary,
    onSecondary = Color.White,
    secondaryContainer = CoralContainer,
    onSecondaryContainer = OnCoralContainer,
    tertiary = SkyTertiary,
    onTertiary = Color.White,
    tertiaryContainer = SkyContainer,
    onTertiaryContainer = OnSkyContainer,
    background = MeadowCream,
    onBackground = CharcoalInk,
    surface = MeadowCardSurface,
    onSurface = CharcoalInk,
    surfaceVariant = MeadowWarmSurface,
    onSurfaceVariant = SoftSlateInk,
    outline = OutlineWarm
)

private val DarkMeadowScheme = darkColorScheme(
    primary = NightMintPrimary,
    onPrimary = Color(0xFF062E22),
    primaryContainer = NightMintContainer,
    onPrimaryContainer = Color(0xFFC8EEDF),
    secondary = Color(0xFFFFA594),
    onSecondary = Color(0xFF4B150C),
    secondaryContainer = NightPeachContainer,
    onSecondaryContainer = Color(0xFFFFDAD2),
    tertiary = Color(0xFF8CC8F0),
    onTertiary = Color(0xFF0A2E46),
    tertiaryContainer = NightSkyContainer,
    onTertiaryContainer = Color(0xFFCDE8FF),
    background = NightMeadowBg,
    onBackground = NightInk,
    surface = NightMeadowSurface,
    onSurface = NightInk,
    surfaceVariant = Color(0xFF2A303E),
    onSurfaceVariant = NightSoftInk,
    outline = NightOutline
)

private val MeadowShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(34.dp)
)

@Composable
fun PocketMeadowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    highContrast: Boolean = false,
    largerText: Boolean = false,
    content: @Composable () -> Unit
) {
    val baseScheme = if (darkTheme) DarkMeadowScheme else LightMeadowScheme
    val colorScheme = if (highContrast) {
        if (darkTheme) {
            baseScheme.copy(
                background = HighContrastDarkBg,
                onBackground = HighContrastDarkInk,
                surface = Color(0xFF121212),
                onSurface = HighContrastDarkInk,
                onSurfaceVariant = Color(0xFFE0E0E0),
                outline = Color(0xFFCCCCCC)
            )
        } else {
            baseScheme.copy(
                background = HighContrastBg,
                onBackground = HighContrastInk,
                surface = HighContrastBg,
                onSurface = HighContrastInk,
                onSurfaceVariant = Color(0xFF1E1E1E),
                outline = Color(0xFF333333)
            )
        }
    } else {
        baseScheme
    }

    val typography = buildAppTypography(scaleFactor = if (largerText) 1.15f else 1.0f)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = MeadowShapes,
        content = content
    )
}
