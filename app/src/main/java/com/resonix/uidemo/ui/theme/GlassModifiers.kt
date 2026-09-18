package com.resonix.uidemo.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Where a card sits inside a stacked group, which decides its edge alphas. */
enum class SegmentPosition { Single, First, Middle, Last }

fun segmentPosition(index: Int, count: Int): SegmentPosition = when {
    count <= 1 -> SegmentPosition.Single
    index == 0 -> SegmentPosition.First
    index == count - 1 -> SegmentPosition.Last
    else -> SegmentPosition.Middle
}

/**
 * Top and bottom border alphas for a segment. Only the outer edges of a
 * group get the bright 20% highlight; interior edges stay dim, so a stack
 * of cards reads as one pane of glass rather than several stacked ones.
 */
fun segmentAlphas(position: SegmentPosition): Pair<Float, Float> = when (position) {
    SegmentPosition.Single -> GlassTokens.BorderTopAlpha to GlassTokens.BorderBottomAlpha
    SegmentPosition.First -> GlassTokens.BorderTopAlpha to 0.08f
    SegmentPosition.Middle -> 0.08f to 0.08f
    SegmentPosition.Last -> 0.08f to GlassTokens.BorderBottomAlpha
}

/**
 * The 0.5dp gradient hairline that makes a surface read as glass. The
 * gradient runs top-to-bottom from [topAlpha] to [bottomAlpha], so the
 * upper edge catches "light" and the lower edge nearly vanishes.
 *
 * [baseColor] supplies the hue only; its own alpha is replaced.
 */
fun Modifier.glassBorder(
    shape: Shape,
    strokeWidth: Dp = GlassTokens.BorderThickness,
    topAlpha: Float = GlassTokens.BorderTopAlpha,
    bottomAlpha: Float = GlassTokens.BorderBottomAlpha,
    baseColor: Color = Color.White,
): Modifier = if (baseColor.isSpecified && baseColor != Color.Transparent) {
    this.border(
        width = strokeWidth,
        brush = Brush.verticalGradient(
            0.0f to baseColor.copy(alpha = topAlpha),
            1.0f to baseColor.copy(alpha = bottomAlpha),
        ),
        shape = shape,
    )
} else {
    this
}

/**
 * A complete glass panel: clip, translucent fill, gradient hairline.
 * Deliberately has no drop shadow — a coloured shadow turns glass into
 * a glowing neon slab. Depth comes from the border gradient instead.
 */
@Composable
fun Modifier.glassSurface(
    shape: Shape = RoundedCornerShape(GlassTokens.CornerRadius),
    fillColor: Color = LocalGlassColors.current.glassFill,
    borderColor: Color = LocalGlassColors.current.glassBorder,
    strokeWidth: Dp = GlassTokens.BorderThickness,
    position: SegmentPosition = SegmentPosition.Single,
    topAlpha: Float? = null,
    bottomAlpha: Float? = null,
): Modifier {
    val defaults = segmentAlphas(position)
    return this
        .clip(shape)
        .background(fillColor, shape)
        .glassBorder(
            shape = shape,
            strokeWidth = strokeWidth,
            topAlpha = topAlpha ?: defaults.first,
            bottomAlpha = bottomAlpha ?: defaults.second,
            baseColor = borderColor,
        )
}

/**
 * Tap feedback that suits glass: the surface shrinks slightly under the
 * finger and springs back. No ripple — a ripple washes over a translucent
 * panel and destroys the illusion of depth.
 */
@Composable
fun Modifier.pressScaleClickable(
    enabled: Boolean = true,
    pressedScale: Float = GlassTokens.PressScale,
    onClick: () -> Unit,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val disableAnimations = LocalDisableAnimations.current

    if (disableAnimations || !enabled) {
        return this.clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick,
        )
    }

    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = GlassTokens.PressDampingRatio,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "pressScale",
    )

    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = true,
            onClick = onClick,
        )
}

/** [pressScaleClickable] with long-press and double-tap support. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Modifier.pressScaleCombinedClickable(
    enabled: Boolean = true,
    pressedScale: Float = GlassTokens.PressScale,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: () -> Unit,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val disableAnimations = LocalDisableAnimations.current

    if (disableAnimations || !enabled) {
        return this.combinedClickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onLongClick = onLongClick,
            onDoubleClick = onDoubleClick,
            onClick = onClick,
        )
    }

    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = GlassTokens.PressDampingRatio,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "pressScaleCombined",
    )

    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .combinedClickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = true,
            onLongClick = onLongClick,
            onDoubleClick = onDoubleClick,
            onClick = onClick,
        )
}

/**
 * A soft radial shadow drawn behind a transparent icon. Android's native
 * elevation shadow traces the view's rectangular bounds, which leaves an
 * obvious box behind a transparent icon; drawing the falloff ourselves
 * avoids that.
 */
fun Modifier.softIconShadow(
    alpha: Float = 0.35f,
    shadowRadius: Dp = 24.dp,
): Modifier = this.drawBehind {
    val radiusPx = shadowRadius.toPx()
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.Black.copy(alpha = alpha), Color.Transparent),
            center = center,
            radius = radiusPx,
        ),
        radius = radiusPx,
        center = center,
    )
}

/** A downward-only linear shadow for long horizontal elements (seek bars). */
fun Modifier.softLineShadow(
    alpha: Float = 0.25f,
    shadowHeight: Dp = 12.dp,
): Modifier = this.drawBehind {
    val shadowHeightPx = shadowHeight.toPx()
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Black.copy(alpha = alpha), Color.Transparent),
            startY = size.height / 2,
            endY = size.height / 2 + shadowHeightPx,
        ),
        topLeft = Offset(0f, size.height / 2),
        size = Size(size.width, shadowHeightPx),
    )
}

/** Keeps light text legible when it sits over a bright piece of artwork. */
val SoftTextShadow = Shadow(
    color = Color.Black.copy(alpha = 0.3f),
    offset = Offset(0f, 2f),
    blurRadius = 5f,
)

/**
 * The screen background: the accent bled into near-black down the page.
 * All of the colour in the UI comes from here, which is why the glass
 * panels themselves can stay almost colourless.
 */
fun Modifier.glassScreenBackground(
    accent: Color,
    topBlend: Float = 0.62f,
    midBlend: Float = 0.86f,
): Modifier = this.drawBehind {
    val top = lerp(accent, SurfaceDeep, topBlend)
    val mid = lerp(accent, SurfaceDeep, midBlend)
    val deep = lerp(accent, SurfaceNear, 0.96f)
    drawRect(
        brush = Brush.verticalGradient(
            0.0f to top,
            0.45f to mid,
            1.0f to deep,
        ),
    )
}

/** A darkening veil for the bottom of a screen, so controls stay readable. */
fun Modifier.bottomScrim(
    color: Color = Color.Black,
    maxAlpha: Float = 0.55f,
): Modifier = this.drawBehind {
    drawRect(
        brush = Brush.verticalGradient(
            0.00f to Color.Transparent,
            0.55f to color.copy(alpha = maxAlpha * 0.35f),
            0.75f to color.copy(alpha = maxAlpha * 0.75f),
            1.00f to color.copy(alpha = maxAlpha),
        ),
    )
}
