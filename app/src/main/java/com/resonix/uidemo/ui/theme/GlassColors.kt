package com.resonix.uidemo.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * The derived colours every glass surface reads from. These are computed
 * from the active Material scheme rather than hardcoded, so when the accent
 * changes (per track, per theme) the glass retints with it automatically.
 *
 * Note on [glassBorder]: this holds the border's *hue* at full opacity.
 * The alpha is applied by [Modifier.glassBorder], which fades it from
 * [GlassTokens.BorderTopAlpha] down to [GlassTokens.BorderBottomAlpha].
 * Passing a pre-faded colour here would have no effect, since the modifier
 * overwrites alpha rather than multiplying it.
 */
@Immutable
data class GlassColors(
    val glassFill: Color,
    val glassFillStrong: Color,
    val glassBorder: Color,
    val cardOpaque: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textDisabled: Color,
    val ripple: Color,
)

fun darkGlassColors(scheme: ColorScheme): GlassColors = GlassColors(
    glassFill = scheme.onSurface.copy(alpha = GlassTokens.FillAlpha),
    glassFillStrong = scheme.onSurface.copy(alpha = GlassTokens.FillAlphaStrong),
    glassBorder = scheme.primary,
    cardOpaque = SurfaceCardOpaque,
    textPrimary = scheme.onSurface,
    textSecondary = scheme.onSurface.copy(alpha = GlassTokens.TextSecondaryAlpha),
    textDisabled = scheme.onSurface.copy(alpha = GlassTokens.TextDisabledAlpha),
    ripple = scheme.onSurface.copy(alpha = 0.16f),
)

fun lightGlassColors(scheme: ColorScheme): GlassColors = GlassColors(
    glassFill = Color.White.copy(alpha = 0.65f),
    glassFillStrong = Color.White.copy(alpha = 0.78f),
    glassBorder = scheme.primary,
    cardOpaque = scheme.surfaceContainerLow,
    textPrimary = scheme.onSurface,
    textSecondary = scheme.onSurface.copy(alpha = GlassTokens.TextSecondaryAlpha),
    textDisabled = scheme.onSurface.copy(alpha = GlassTokens.TextDisabledAlpha),
    ripple = scheme.onSurface.copy(alpha = 0.12f),
)

val LocalGlassColors = staticCompositionLocalOf<GlassColors> {
    error("No GlassColors provided. Wrap your content in ResonixTheme.")
}

/** Shorthand for reading the glass palette inside a composable. */
val glassColors: GlassColors
    @Composable
    @ReadOnlyComposable
    get() = LocalGlassColors.current

/** True when animations should collapse to instant snaps (accessibility). */
val LocalDisableAnimations = staticCompositionLocalOf { false }

/** The accent the current screen tints its background and borders with. */
val LocalAccentColor = staticCompositionLocalOf { ElectricBlue }

@Composable
@ReadOnlyComposable
fun currentAccent(): Color = MaterialTheme.colorScheme.primary
