package com.resonix.uidemo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.resonix.uidemo.ui.theme.pressScaleClickable
import com.resonix.uidemo.ui.util.formatMillis

/** The playback queue, presented in a glass sheet. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueSheet(
    queue: List<DemoTrack>,
    currentTrack: DemoTrack,
    onDismissRequest: () -> Unit,
    onTrackSelected: (DemoTrack) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    val glass = LocalGlassColors.current

    GlassBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = GlassTokens.SheetContentPaddingH,
                    vertical = 4.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Up next",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = glass.textPrimary,
            )
            Text(
                text = "${queue.size} tracks",
                style = MaterialTheme.typography.labelSmall,
                color = glass.textSecondary,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = GlassTokens.SheetListMaxHeight),
        ) {
            items(queue) { track ->
                QueueRow(
                    track = track,
                    isCurrent = track == currentTrack,
                    onClick = { onTrackSelected(track) },
                )
            }
        }
    }
}

@Composable
private fun QueueRow(
    track: DemoTrack,
    isCurrent: Boolean,
    onClick: () -> Unit,
) {
    val glass = LocalGlassColors.current
    val accent = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pressScaleClickable(pressedScale = 0.98f, onClick = onClick)
            .padding(
                horizontal = GlassTokens.SheetOptionPaddingH,
                vertical = GlassTokens.SheetOptionPaddingV,
            ),
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

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun QueueRowsPreview() {
    ResonixTheme {
        Column {
            DemoTrackCatalog.queue.forEach { track ->
                QueueRow(
                    track = track,
                    isCurrent = track == DemoTrackCatalog.current,
                    onClick = {},
                )
            }
        }
    }
}
