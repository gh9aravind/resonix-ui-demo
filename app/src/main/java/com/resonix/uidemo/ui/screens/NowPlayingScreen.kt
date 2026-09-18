package com.resonix.uidemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.components.PlayerIconButton
import com.resonix.uidemo.ui.components.QueueSheet
import com.resonix.uidemo.ui.model.DemoTrack
import com.resonix.uidemo.ui.model.DemoTrackCatalog
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.ResonixTheme
import com.resonix.uidemo.ui.theme.glassScreenBackground
import com.resonix.uidemo.ui.theme.glassSurface
import com.resonix.uidemo.ui.theme.pressScaleClickable
import com.resonix.uidemo.ui.theme.softIconShadow
import com.resonix.uidemo.ui.util.formatMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    track: DemoTrack = DemoTrackCatalog.current,
    queue: List<DemoTrack> = DemoTrackCatalog.queue,
    isPlaying: Boolean,
    progressMs: Long,
    onPlayPauseClick: () -> Unit,
    onSeek: (Long) -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onTrackSelectedFromQueue: (DemoTrack) -> Unit = {},
) {
    val glass = LocalGlassColors.current
    val accent = MaterialTheme.colorScheme.primary

    var shuffleEnabled by remember { mutableStateOf(false) }
    var repeatEnabled by remember { mutableStateOf(false) }
    var showQueueSheet by remember { mutableStateOf(false) }

    // While a drag is in progress the slider follows the finger rather than
    // the ticking playback position, which would otherwise yank the thumb
    // back on every tick.
    var isScrubbing by remember { mutableStateOf(false) }
    var scrubFraction by remember { mutableFloatStateOf(0f) }

    val playbackFraction = if (track.durationMs > 0) {
        (progressMs.toFloat() / track.durationMs.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }
    val displayFraction = if (isScrubbing) scrubFraction else playbackFraction
    val displayMs = (displayFraction * track.durationMs).toLong()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .glassScreenBackground(accent, topBlend = 0.48f, midBlend = 0.80f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PlayerIconButton(onClick = onBack, size = 44.dp) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Collapse player",
                        tint = glass.textPrimary,
                        modifier = Modifier.size(28.dp),
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "PLAYING FROM ALBUM",
                        style = MaterialTheme.typography.labelSmall,
                        color = glass.textSecondary,
                    )
                    Text(
                        text = track.album,
                        style = MaterialTheme.typography.labelLarge,
                        color = glass.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                PlayerIconButton(onClick = { showQueueSheet = true }, size = 44.dp) {
                    Icon(
                        imageVector = Icons.Filled.MoreHoriz,
                        contentDescription = "More",
                        tint = glass.textPrimary,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(GlassTokens.PlayerCoverCornerRadius))
                    .background(Brush.linearGradient(track.artGradient)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(96.dp),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = glass.textPrimary,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = track.artist,
                    style = MaterialTheme.typography.bodyLarge,
                    color = glass.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Slider(
                value = displayFraction,
                onValueChange = { fraction ->
                    isScrubbing = true
                    scrubFraction = fraction
                },
                onValueChangeFinished = {
                    onSeek((scrubFraction * track.durationMs).toLong())
                    isScrubbing = false
                },
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = glass.textPrimary,
                    activeTrackColor = glass.textPrimary,
                    inactiveTrackColor = glass.textPrimary.copy(alpha = 0.18f),
                ),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = formatMillis(displayMs),
                    style = MaterialTheme.typography.labelSmall,
                    color = glass.textSecondary,
                )
                Text(
                    text = formatMillis(track.durationMs),
                    style = MaterialTheme.typography.labelSmall,
                    color = glass.textSecondary,
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PlayerIconButton(onClick = { shuffleEnabled = !shuffleEnabled }) {
                    Icon(
                        imageVector = Icons.Filled.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (shuffleEnabled) accent else glass.textSecondary,
                        modifier = Modifier.size(22.dp),
                    )
                }

                PlayerIconButton(onClick = onPrevious, size = 56.dp) {
                    Icon(
                        imageVector = Icons.Filled.SkipPrevious,
                        contentDescription = "Previous",
                        tint = glass.textPrimary,
                        modifier = Modifier.size(36.dp),
                    )
                }

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .softIconShadow(alpha = 0.3f, shadowRadius = 46.dp)
                        .pressScaleClickable(pressedScale = 0.9f, onClick = onPlayPauseClick)
                        .clip(CircleShape)
                        .background(glass.textPrimary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = if (isPlaying) {
                            Icons.Filled.Pause
                        } else {
                            Icons.Filled.PlayArrow
                        },
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier.size(36.dp),
                    )
                }

                PlayerIconButton(onClick = onNext, size = 56.dp) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "Next",
                        tint = glass.textPrimary,
                        modifier = Modifier.size(36.dp),
                    )
                }

                PlayerIconButton(onClick = { repeatEnabled = !repeatEnabled }) {
                    Icon(
                        imageVector = Icons.Filled.Repeat,
                        contentDescription = "Repeat",
                        tint = if (repeatEnabled) accent else glass.textSecondary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .pressScaleClickable(
                        pressedScale = 0.98f,
                        onClick = { showQueueSheet = true },
                    )
                    .glassSurface(shape = RoundedCornerShape(50))
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.QueueMusic,
                    contentDescription = null,
                    tint = glass.textSecondary,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "Queue",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    color = glass.textPrimary,
                )
            }

            Spacer(modifier = Modifier.height(GlassTokens.ScreenPaddingBottom))
        }

        if (showQueueSheet) {
            QueueSheet(
                queue = queue,
                currentTrack = track,
                onDismissRequest = { showQueueSheet = false },
                onTrackSelected = { selected ->
                    showQueueSheet = false
                    onTrackSelectedFromQueue(selected)
                },
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun NowPlayingScreenPreview() {
    ResonixTheme {
        NowPlayingScreen(
            track = DemoTrackCatalog.current,
            isPlaying = true,
            progressMs = 68_000L,
            onPlayPauseClick = {},
            onSeek = {},
            onPrevious = {},
            onNext = {},
            onBack = {},
        )
    }
}
