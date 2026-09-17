package com.resonix.uidemo.ui.model

import androidx.compose.ui.graphics.Color
import com.resonix.uidemo.ui.theme.ElectricBlue
import com.resonix.uidemo.ui.theme.NeonCyan
import com.resonix.uidemo.ui.theme.VibrantPurple

/**
 * A lightweight, UI-only representation of a track. This sandbox has no
 * audio engine, so DemoTrack exists purely to feed realistic-looking data
 * into the player screens while we refine their visuals.
 */
data class DemoTrack(
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val artGradient: List<Color>
)

object DemoTrackCatalog {

    val current = DemoTrack(
        title = "Neon Horizon",
        artist = "Circuit Ghost",
        album = "Afterglow",
        durationMs = 214_000L,
        artGradient = listOf(NeonCyan, ElectricBlue, VibrantPurple)
    )

    val queue = listOf(
        current,
        DemoTrack(
            title = "Glass City Rain",
            artist = "Circuit Ghost",
            album = "Afterglow",
            durationMs = 198_000L,
            artGradient = listOf(ElectricBlue, VibrantPurple)
        ),
        DemoTrack(
            title = "Midnight Frequency",
            artist = "Nova Wilder",
            album = "Static Bloom",
            durationMs = 231_000L,
            artGradient = listOf(VibrantPurple, NeonCyan)
        ),
        DemoTrack(
            title = "Static Bloom",
            artist = "Nova Wilder",
            album = "Static Bloom",
            durationMs = 176_000L,
            artGradient = listOf(NeonCyan, VibrantPurple)
        )
    )
}
