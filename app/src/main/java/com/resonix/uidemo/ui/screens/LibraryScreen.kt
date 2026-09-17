package com.resonix.uidemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.components.MiniPlayerBar
import com.resonix.uidemo.ui.model.DemoTrack
import com.resonix.uidemo.ui.model.DemoTrackCatalog
import com.resonix.uidemo.ui.theme.NeonCyan
import com.resonix.uidemo.ui.theme.ResonixBackgroundBrush
import com.resonix.uidemo.ui.theme.ResonixGlowBrushBottomRight
import com.resonix.uidemo.ui.theme.ResonixGlowBrushTopLeft
import com.resonix.uidemo.ui.theme.ResonixTheme
import com.resonix.uidemo.ui.theme.TextPrimary
import com.resonix.uidemo.ui.theme.TextSecondary
import com.resonix.uidemo.ui.util.formatMillis

/**
 * The library "home" screen: a scrollable track list with a [MiniPlayerBar]
 * docked at the bottom. Tapping a row plays that track (in this sandbox,
 * just switches the fake "current track" state); tapping the mini player
 * expands to the full [NowPlayingScreen].
 */
@Composable
fun LibraryScreen(
    tracks: List<DemoTrack>,
    currentTrack: DemoTrack,
    isPlaying: Boolean,
    progressFraction: Float,
    onTrackClick: (DemoTrack) -> Unit,
    onPlayPauseClick: () -> Unit,
    onExpandPlayer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ResonixBackgroundBrush)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopEnd)
                .background(ResonixGlowBrushTopLeft)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomStart)
                .background(ResonixGlowBrushBottomRight)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Your Library",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                items(tracks) { track ->
                    val isCurrent = track == currentTrack
                    LibraryTrackRow(
                        track = track,
                        isCurrent = isCurrent,
                        onClick = { onTrackClick(track) }
                    )
                }
            }

            MiniPlayerBar(
                track = currentTrack,
                isPlaying = isPlaying,
                progressFraction = progressFraction,
                onPlayPauseClick = onPlayPauseClick,
                onExpandClick = onExpandPlayer,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun LibraryTrackRow(
    track: DemoTrack,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Brush.linearGradient(track.artGradient)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isCurrent) Icons.Filled.GraphicEq else Icons.Filled.MusicNote,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                style = MaterialTheme.typography.titleMedium,
                color = if (isCurrent) NeonCyan else TextPrimary,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artist,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = formatMillis(track.durationMs),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF020207)
@Composable
private fun LibraryScreenPreview() {
    ResonixTheme {
        LibraryScreen(
            tracks = DemoTrackCatalog.queue,
            currentTrack = DemoTrackCatalog.current,
            isPlaying = true,
            progressFraction = 0.4f,
            onTrackClick = {},
            onPlayPauseClick = {},
            onExpandPlayer = {}
        )
    }
}
