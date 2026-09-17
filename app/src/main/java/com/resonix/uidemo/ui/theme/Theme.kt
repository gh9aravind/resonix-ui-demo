package com.resonix.uidemo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val ResonixDarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = DeepNavySecondary,
    onPrimaryContainer = NeonCyan,

    secondary = VibrantPurple,
    onSecondary = Color.White,
    secondaryContainer = DeepNavySecondary,
    onSecondaryContainer = NeonCyan,

    tertiary = NeonCyan,
    onTertiary = VoidBlack,

    background = VoidBlack,
    onBackground = TextPrimary,

    surface = DeepNavy,
    onSurface = TextPrimary,
    surfaceVariant = DeepNavySecondary,
    onSurfaceVariant = TextSecondary,

    error = ErrorRed,
    onError = Color.White,

    outline = TextDisabled
)

val ResonixTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 34.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

/** Base app background: pitch-dark navy, meant to sit behind every screen. */
val ResonixBackgroundBrush = Brush.verticalGradient(
    colors = listOf(VoidBlack, DeepNavy, VoidBlack)
)

/** Faint electric-blue glow, positioned near a screen's top corner. */
val ResonixGlowBrushTopLeft = Brush.radialGradient(
    colors = listOf(ElectricBlue.copy(alpha = 0.20f), Color.Transparent),
    radius = 900f
)

/** Faint purple glow, positioned near a screen's opposite corner. */
val ResonixGlowBrushBottomRight = Brush.radialGradient(
    colors = listOf(VibrantPurple.copy(alpha = 0.18f), Color.Transparent),
    radius = 900f
)

/** The 1dp neon gradient stroke used to outline every glass surface. */
val GlassBorderBrush = Brush.linearGradient(
    colors = listOf(
        NeonCyan.copy(alpha = 0.9f),
        ElectricBlue.copy(alpha = 0.5f),
        VibrantPurple.copy(alpha = 0.9f)
    )
)

@Composable
fun ResonixTheme(
    // Resonix is deep-dark only by design; system light theme is ignored.
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ResonixDarkColorScheme,
        typography = ResonixTypography,
        content = content
    )
}
