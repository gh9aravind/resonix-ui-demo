package com.resonix.uidemo.ui.components

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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.model.DemoTrack
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.pressScaleClickable
import com.resonix.uidemo.ui.util.formatMillis

/**
 * A single track row: artwork badge, title/artist, duration. Shared by the
 * library list, search results, and the queue sheet, so the three read as
 * one consistent list rather than three slightly different ones.
 */
@Composable
fun TrackRow(
    track: DemoTrack,
    isCurrent: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(GlassTokens.LibraryCardRadius),
    contentPaddingH: Dp = 8.dp,
    contentPaddingV: Dp = GlassTokens.RowPaddingV,
) {
    val glass = LocalGlassColors.current
    val accent = MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .pressScaleClickable(pressedScale = 0.98f, onClick = onClick)
            .clip(shape)
            .padding(horizontal = contentPaddingH, vertical = contentPaddingV),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(GlassTokens.LibraryBadgeSize)
                .clip(RoundedCornerShape(GlassTokens.LibrarySmallRadius))
                .background(Brush.linearGradient(track.artGradient)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (isCurrent) Icons.Filled.GraphicEq else Icons.Filled.MusicNote,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.92f),
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.width(GlassTokens.RowIconSpacing))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isCurrent) accent else glass.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(GlassTokens.RowTextSpacing))
            Text(
                text = track.artist,
                style = MaterialTheme.typography.bodyMedium,
                color = glass.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = formatMillis(track.durationMs),
            style = MaterialTheme.typography.labelSmall,
            color = glass.textDisabled,
        )
    }
}
