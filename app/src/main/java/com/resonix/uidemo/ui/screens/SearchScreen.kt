package com.resonix.uidemo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.components.PlayerIconButton
import com.resonix.uidemo.ui.components.TrackRow
import com.resonix.uidemo.ui.model.DemoTrack
import com.resonix.uidemo.ui.model.DemoTrackCatalog
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.ResonixTheme
import com.resonix.uidemo.ui.theme.glassScreenBackground
import com.resonix.uidemo.ui.theme.glassSurface

/**
 * The Search tab: a glass search field over the shared track list. Filters
 * the in-memory demo catalogue by title, artist, or album — there is no
 * backing search index in this sandbox yet.
 */
@Composable
fun SearchScreen(
    allTracks: List<DemoTrack>,
    currentTrack: DemoTrack,
    onTrackClick: (DemoTrack) -> Unit,
) {
    val glass = LocalGlassColors.current
    var query by remember { mutableStateOf("") }

    val results = remember(query, allTracks) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            allTracks
        } else {
            allTracks.filter { track ->
                track.title.contains(trimmed, ignoreCase = true) ||
                    track.artist.contains(trimmed, ignoreCase = true) ||
                    track.album.contains(trimmed, ignoreCase = true)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Search",
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
                    bottom = 14.dp,
                ),
        )

        SearchField(
            query = query,
            onQueryChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GlassTokens.ScreenPaddingH)
                .padding(bottom = 12.dp),
        )

        if (results.isEmpty()) {
            SearchEmptyState(
                message = "No matches for \u201C${query.trim()}\u201D",
                modifier = Modifier.weight(1f),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = GlassTokens.ScreenPaddingH,
                    vertical = 4.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(GlassTokens.LibraryItemGap),
            ) {
                items(results) { track ->
                    TrackRow(
                        track = track,
                        isCurrent = track == currentTrack,
                        onClick = { onTrackClick(track) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val glass = LocalGlassColors.current
    val accent = MaterialTheme.colorScheme.primary
    val shape = RoundedCornerShape(50)

    Row(
        modifier = modifier
            .height(52.dp)
            .glassSurface(shape = shape, fillColor = glass.glassFillStrong)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = glass.textSecondary,
            modifier = Modifier.size(20.dp),
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = "Search your library",
                    style = MaterialTheme.typography.bodyLarge,
                    color = glass.textDisabled,
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = glass.textPrimary),
                cursorBrush = SolidColor(accent),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (query.isNotEmpty()) {
            Spacer(modifier = Modifier.width(6.dp))
            PlayerIconButton(
                onClick = { onQueryChange("") },
                size = 32.dp,
                iconSize = 16.dp,
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Clear search",
                    tint = glass.textSecondary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Composable
private fun SearchEmptyState(
    message: String,
    modifier: Modifier = Modifier,
) {
    val glass = LocalGlassColors.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = glass.textDisabled,
            modifier = Modifier.size(40.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = glass.textSecondary,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SearchScreenPreview() {
    ResonixTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .glassScreenBackground(MaterialTheme.colorScheme.primary),
        ) {
            SearchScreen(
                allTracks = DemoTrackCatalog.queue,
                currentTrack = DemoTrackCatalog.current,
                onTrackClick = {},
            )
        }
    }
}
