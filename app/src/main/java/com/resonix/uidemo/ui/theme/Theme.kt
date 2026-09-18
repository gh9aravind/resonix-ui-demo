package com.resonix.uidemo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ResonixDarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = SurfaceRaised,
    onPrimaryContainer = NeonCyan,

    secondary = VibrantPurple,
    onSecondary = Color.White,
    secondaryContainer = SurfaceRaised,
    onSecondaryContainer = NeonCyan,

    tertiary = NeonCyan,
    onTertiary = SurfaceBlack,

    background = SurfaceBlack,
    onBackground = TextPrimary,

    surface = SurfaceNear,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceDeep,
    onSurfaceVariant = TextSecondary,
    surfaceContainerLowest = SurfaceBlack,
    surfaceContainerLow = SurfaceNear,
    surfaceContainer = SurfaceDeep,
    surfaceContainerHigh = SurfaceRaised,
    surfaceContainerHighest = SurfaceCardOpaque,

    error = ErrorRed,
    onError = Color.White,

    outline = TextDisabled,
)

private val ResonixShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

val ResonixTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 34.sp,
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)

@Composable
fun ResonixTheme(
    // Resonix is deep-dark only by design; the system light theme is ignored.
    darkTheme: Boolean = isSystemInDarkTheme(),
    accentColor: Color = ElectricBlue,
    disableAnimations: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = remember(accentColor) {
        ResonixDarkColorScheme.copy(primary = accentColor)
    }

    val glass = remember(colorScheme) { darkGlassColors(colorScheme) }

    CompositionLocalProvider(
        LocalGlassColors provides glass,
        LocalDisableAnimations provides disableAnimations,
        LocalAccentColor provides accentColor,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = ResonixTypography,
            shapes = ResonixShapes,
            content = content,
        )
    }
}

// --- Legacy brushes ---------------------------------------------------------
// Kept so the existing components keep compiling during the migration onto
// the glass design system. Remove once every screen has been moved over.

val ResonixBackgroundBrush = Brush.verticalGradient(
    colors = listOf(SurfaceBlack, SurfaceNear, SurfaceBlack),
)

val ResonixGlowBrushTopLeft = Brush.radialGradient(
    colors = listOf(ElectricBlue.copy(alpha = 0.20f), Color.Transparent),
    radius = 900f,
)

val ResonixGlowBrushBottomRight = Brush.radialGradient(
    colors = listOf(VibrantPurple.copy(alpha = 0.18f), Color.Transparent),
    radius = 900f,
)

val GlassBorderBrush = Brush.linearGradient(
    colors = listOf(
        NeonCyan.copy(alpha = 0.9f),
        ElectricBlue.copy(alpha = 0.5f),
        VibrantPurple.copy(alpha = 0.9f),
    ),
)
