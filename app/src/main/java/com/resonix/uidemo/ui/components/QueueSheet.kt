package com.resonix.uidemo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.model.DemoTrack
import com.resonix.uidemo.ui.model.DemoTrackCatalog
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.ResonixTheme

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
                TrackRow(
                    track = track,
                    isCurrent = track == currentTrack,
                    onClick = { onTrackSelected(track) },
                    contentPaddingH = GlassTokens.SheetOptionPaddingH,
                    contentPaddingV = GlassTokens.SheetOptionPaddingV,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun QueueRowsPreview() {
    ResonixTheme {
        Column {
            DemoTrackCatalog.queue.forEach { track ->
                TrackRow(
                    track = track,
                    isCurrent = track == DemoTrackCatalog.current,
                    onClick = {},
                    contentPaddingH = GlassTokens.SheetOptionPaddingH,
                    contentPaddingV = GlassTokens.SheetOptionPaddingV,
                )
            }
        }
    }
}
