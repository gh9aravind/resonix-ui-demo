package com.resonix.uidemo.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.model.DemoTrack
import com.resonix.uidemo.ui.model.DemoTrackCatalog
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.ResonixTheme
import com.resonix.uidemo.ui.theme.glassScreenBackground
import com.resonix.uidemo.ui.theme.glassSurface
import com.resonix.uidemo.ui.theme.pressScaleClickable

/**
 * The docked glass bar showing what's playing. Tapping the body expands the
 * full player; the transport buttons handle their own taps.
 *
 * The progress line sits flush along the very top edge rather than inside
 * the bar, so the bar keeps a clean rectangular reading and the progress
 * doubles as the lit upper border of the glass.
 */
@Composable
fun MiniPlayerBar(
    track: DemoTrack,
    isPlaying: Boolean,
    progressFraction: Float,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val glass = LocalGlassColors.current
    val accent = MaterialTheme.colorScheme.primary
    val shape = RoundedCornerShape(GlassTokens.CornerRadius)

    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction.coerceIn(0f, 1f),
        label = "miniPlayerProgress",
    )

    Column(
        modifier = modifier
            .padding(horizontal = GlassTokens.ScreenPaddingH)
            .pressScaleClickable(pressedScale = 0.985f, onClick = onExpandClick)
            .glassSurface(shape = shape, fillColor = glass.glassFillStrong),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(glass.textPrimary.copy(alpha = 0.07f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(2.dp)
                    .background(accent),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(GlassTokens.LibrarySmallRadius))
                    .background(Brush.linearGradient(track.artGradient)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.92f),
                    modifier = Modifier.size(20.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = glass.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = track.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = glass.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            PlayerIconButton(
                onClick = onPlayPauseClick,
                size = 40.dp,
                iconSize = 26.dp,
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = glass.textPrimary,
                    modifier = Modifier.size(26.dp),
                )
            }

            PlayerIconButton(
                onClick = onNextClick,
                size = 40.dp,
                iconSize = 24.dp,
            ) {
                Icon(
                    imageVector = Icons.Filled.SkipNext,
                    contentDescription = "Next",
                    tint = glass.textPrimary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

/**
 * A borderless, ripple-free tap target for transport controls. Material's
 * IconButton brings a ripple with it, which smears across translucent
 * surfaces; this uses the same press-scale feedback as the rest of the glass.
 */
@Composable
fun PlayerIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 48.dp,
    iconSize: androidx.compose.ui.unit.Dp = 24.dp,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(50))
            .pressScaleClickable(enabled = enabled, pressedScale = 0.88f, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun MiniPlayerBarPreview() {
    ResonixTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .glassScreenBackground(MaterialTheme.colorScheme.primary)
                .padding(vertical = 24.dp),
        ) {
            MiniPlayerBar(
                track = DemoTrackCatalog.current,
                isPlaying = true,
                progressFraction = 0.42f,
                onPlayPauseClick = {},
                onNextClick = {},
                onExpandClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
