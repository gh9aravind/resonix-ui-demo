package com.resonix.uidemo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.theme.ElectricBlue
import com.resonix.uidemo.ui.theme.GlassBorderBrush
import com.resonix.uidemo.ui.theme.GlassSurface
import com.resonix.uidemo.ui.theme.NeonCyan
import com.resonix.uidemo.ui.theme.VibrantPurple

/**
 * A reusable "liquid glass" panel: translucent dark surface, a soft neon
 * glow shadow, and a 1dp gradient border running cyan -> blue -> purple.
 *
 * This is a visual approximation of glassmorphism (Compose has no cheap
 * way to backdrop-blur arbitrary content behind a node without an extra
 * render-effect library). Layering a translucent gradient fill with a
 * bright top highlight over the deep-navy background reads convincingly
 * as frosted glass.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    borderWidth: Dp = 1.dp,
    glowElevation: Dp = 18.dp,
    contentPadding: Dp = 20.dp,
    shape: Shape = RoundedCornerShape(cornerRadius),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = glowElevation,
                shape = shape,
                ambientColor = ElectricBlue.copy(alpha = 0.35f),
                spotColor = VibrantPurple.copy(alpha = 0.35f)
            )
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.06f),
                        GlassSurface,
                        Color.Black.copy(alpha = 0.35f)
                    )
                )
            )
            .border(
                width = borderWidth,
                brush = GlassBorderBrush,
                shape = shape
            )
            .padding(contentPadding)
    ) {
        content()
    }
}

/**
 * A smaller glass "tile" variant for list rows (e.g. permission items),
 * where the caller supplies its own inner Row/Column layout.
 */
@Composable
fun GlassTile(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 18.dp,
    accentBrush: Brush = Brush.linearGradient(listOf(NeonCyan, ElectricBlue)),
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = ElectricBlue.copy(alpha = 0.25f),
                spotColor = NeonCyan.copy(alpha = 0.25f)
            )
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.05f), GlassSurface)
                )
            )
            .border(width = 1.dp, brush = accentBrush, shape = shape)
    ) {
        content()
    }
}
