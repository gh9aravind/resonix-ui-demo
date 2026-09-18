package com.resonix.uidemo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.SegmentPosition
import com.resonix.uidemo.ui.theme.glassSurface
import com.resonix.uidemo.ui.theme.pressScaleClickable
import com.resonix.uidemo.ui.theme.segmentPosition

/**
 * Corner radii for a card inside a stacked group. Outer corners stay large
 * while the corners facing a neighbour tighten right down, so a column of
 * cards reads as one continuous pane that has been scored into rows.
 */
fun segmentShape(index: Int, count: Int): RoundedCornerShape {
    val large = GlassTokens.SegmentCornerLarge
    val small = GlassTokens.SegmentCornerSmall
    return when (segmentPosition(index, count)) {
        SegmentPosition.Single -> RoundedCornerShape(large)
        SegmentPosition.First -> RoundedCornerShape(
            topStart = large,
            topEnd = large,
            bottomStart = small,
            bottomEnd = small,
        )
        SegmentPosition.Middle -> RoundedCornerShape(small)
        SegmentPosition.Last -> RoundedCornerShape(
            topStart = small,
            topEnd = small,
            bottomStart = large,
            bottomEnd = large,
        )
    }
}

/**
 * A liquid-glass panel: translucent fill plus a half-dp hairline that fades
 * from lit at the top edge to nearly invisible at the bottom.
 *
 * There is no drop shadow here on purpose. A coloured elevation shadow turns
 * a translucent panel into a glowing slab, which is the single biggest thing
 * that makes "glassmorphism" read as a neon sign instead of glass.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(GlassTokens.CornerRadius),
    contentPadding: PaddingValues = PaddingValues(20.dp),
    position: SegmentPosition = SegmentPosition.Single,
    fillColor: Color = LocalGlassColors.current.glassFill,
    borderColor: Color = LocalGlassColors.current.glassBorder,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interaction = if (onClick != null) {
        Modifier.pressScaleClickable(onClick = onClick)
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .then(interaction)
            .glassSurface(
                shape = shape,
                fillColor = fillColor,
                borderColor = borderColor,
                position = position,
            )
            .padding(contentPadding),
        content = content,
    )
}

/**
 * The rounded icon container that sits at the start of most glass rows.
 * The background is the accent at ~12% alpha, which tints without competing
 * with the row's text.
 */
@Composable
fun GlassIconBadge(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = GlassTokens.SegmentIconBoxSize,
    iconSize: Dp = GlassTokens.SegmentIconSize,
    tint: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = tint.copy(alpha = GlassTokens.RowIconBgAlpha),
    shape: Shape = RoundedCornerShape(size / 3f),
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(iconSize),
        )
    }
}

/** Circular variant of [GlassIconBadge], for avatars and app marks. */
@Composable
fun GlassCircleBadge(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 84.dp,
    iconSize: Dp = 38.dp,
    tint: Color = MaterialTheme.colorScheme.primary,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(tint.copy(alpha = GlassTokens.RowIconBgAlpha)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(iconSize),
        )
    }
}
