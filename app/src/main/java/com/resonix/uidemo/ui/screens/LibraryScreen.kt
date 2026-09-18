package com.resonix.uidemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
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
import com.resonix.uidemo.ui.components.PlayerIconButton
import com.resonix.uidemo.ui.model.DemoTrack
import com.resonix.uidemo.ui.model.DemoTrackCatalog
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.ResonixTheme
import com.resonix.uidemo.ui.theme.glassScreenBackground
import com.resonix.uidemo.ui.theme.pressScaleClickable
import com.resonix.uidemo.ui.util.formatMillis

@Composable
fun LibraryScreen(
    tracks: List<DemoTrack>,
    currentTrack: DemoTrack,
    isPlaying: Boolean,
    progressFraction: Float,
    onTrackClick: (DemoTrack) -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onExpandPlayer: () -> Unit,
) {
    val glass = LocalGlassColors.current
    val accent = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .glassScreenBackground(accent, topBlend = 0.72f, midBlend = 0.92f),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(
                        start = GlassTokens.ScreenPaddingH + 8.dp,
                        end = GlassTokens.ScreenPaddingH,
                        top = 20.dp,
                        bottom = 16.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Library",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = glass.textPrimary,
                    modifier = Modifier.weight(1f),
                )
                PlayerIconButton(onClick = { }, size = 44.dp) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = glass.textPrimary,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    horizontal = GlassTokens.ScreenPaddingH,
                    vertical = 4.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(GlassTokens.LibraryItemGap),
            ) {
                items(tracks) { track ->
                    LibraryTrackRow(
                        track = track,
                        isCurrent = track == currentTrack,
                        onClick = { onTrackClick(track) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            MiniPlayerBar(
                track = currentTrack,
                isPlaying = isPlaying,
                progressFraction = progressFraction,
                onPlayPauseClick = onPlayPauseClick,
                onNextClick = onNextClick,
                onExpandClick = onExpandPlayer,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(
                modifier = Modifier
                    .height(GlassTokens.SafeBottomMin)
                    .navigationBarsPadding(),
            )
        }
    }
}

@Composable
private fun LibraryTrackRow(
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
            .clip(RoundedCornerShape(GlassTokens.LibraryCardRadius))
            .padding(horizontal = 8.dp, vertical = GlassTokens.RowPaddingV),
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
                modifier = Modifier.size(22.dp),
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LibraryScreenPreview() {
    ResonixTheme {
        LibraryScreen(
            tracks = DemoTrackCatalog.queue,
            currentTrack = DemoTrackCatalog.current,
            isPlaying = true,
            progressFraction = 0.42f,
            onTrackClick = {},
            onPlayPauseClick = {},
            onNextClick = {},
            onExpandPlayer = {},
        )
    }
}
