package com.resonix.uidemo.ui.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.pressScaleClickable

/**
 * The Settings screen's own fixed palette. Every other screen in Resonix
 * tints its glass from the currently playing track's artwork; Settings is
 * specified as a strictly-enforced, always-black utility screen instead —
 * a flat `#000000` background and a fixed cyan accent, regardless of
 * what's playing. That's why the rows in this package read from here
 * rather than from `LocalGlassColors` / `MaterialTheme.colorScheme.primary`
 * for anything colour- or fill-related.
 */
object SettingsGlass {
    val ScreenBackground = Color(0xFF000000)
    val CardGradientStart = Color(0xFF0D0D0D)
    val CardGradientEnd = Color(0xFF161616)
    const val CardAlpha = 0.5f
    val BorderColor = Color(0xFFFFFFFF)
    const val BorderAlpha = 0.12f
    val BorderWidth = 0.5.dp
    val CornerRadius = 16.dp
    val CardSpacing = 10.dp
    val Accent = Color(0xFF00E5FF)
}

/**
 * The pure-black glass card every Settings row is built from. Deliberately
 * separate from [com.resonix.uidemo.ui.components.GlassCard]: that one
 * tints itself from the current track, and this screen's palette is fixed
 * regardless of what's playing.
 */
@Composable
fun SettingsGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(SettingsGlass.CornerRadius),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = GlassTokens.SegmentPaddingH,
        vertical = GlassTokens.SegmentPaddingV,
    ),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interactionModifier = if (onClick != null) {
        Modifier.pressScaleClickable(onClick = onClick)
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .then(interactionModifier)
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SettingsGlass.CardGradientStart.copy(alpha = SettingsGlass.CardAlpha),
                        SettingsGlass.CardGradientEnd.copy(alpha = SettingsGlass.CardAlpha),
                    ),
                ),
            )
            .border(
                width = SettingsGlass.BorderWidth,
                color = SettingsGlass.BorderColor.copy(alpha = SettingsGlass.BorderAlpha),
                shape = shape,
            )
            .padding(contentPadding),
        content = content,
    )
}
