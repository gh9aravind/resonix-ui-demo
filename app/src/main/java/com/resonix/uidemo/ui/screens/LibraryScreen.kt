package com.resonix.uidemo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.components.TrackRow
import com.resonix.uidemo.ui.model.DemoTrack
import com.resonix.uidemo.ui.model.DemoTrackCatalog
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.ResonixTheme
import com.resonix.uidemo.ui.theme.glassScreenBackground

/**
 * The Home tab: a plain scrollable track list. This screen draws only its
 * own header — the background, mini player, and nav bar all come from
 * [com.resonix.uidemo.ui.components.MainScaffold], which hosts it.
 */
@Composable
fun LibraryScreen(
    tracks: List<DemoTrack>,
    currentTrack: DemoTrack,
    onTrackClick: (DemoTrack) -> Unit,
) {
    val glass = LocalGlassColors.current

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Library",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = glass.textPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    start = GlassTokens.ScreenPaddingH + 8.dp,
                    end = GlassTokens.ScreenPaddingH,
                    top = 20.dp,
                    bottom = 16.dp,
                ),
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = GlassTokens.ScreenPaddingH,
                vertical = 4.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(GlassTokens.LibraryItemGap),
        ) {
            items(tracks) { track ->
                TrackRow(
                    track = track,
                    isCurrent = track == currentTrack,
                    onClick = { onTrackClick(track) },
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LibraryScreenPreview() {
    ResonixTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .glassScreenBackground(MaterialTheme.colorScheme.primary),
        ) {
            LibraryScreen(
                tracks = DemoTrackCatalog.queue,
                currentTrack = DemoTrackCatalog.current,
                onTrackClick = {},
            )
        }
    }
}
