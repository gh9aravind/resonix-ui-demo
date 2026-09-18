package com.resonix.uidemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.resonix.uidemo.ui.model.DemoTrack
import com.resonix.uidemo.ui.model.DemoTrackCatalog
import com.resonix.uidemo.ui.screens.LibraryScreen
import com.resonix.uidemo.ui.screens.NowPlayingScreen
import com.resonix.uidemo.ui.screens.OnboardingScreen
import com.resonix.uidemo.ui.screens.PermissionScreen
import com.resonix.uidemo.ui.theme.ResonixTheme
import kotlinx.coroutines.delay

/**
 * Which screen the in-app preview switcher currently shows. This is a
 * temporary, UI-sandbox-only mechanism — the real Resonix app will drive
 * navigation via NavHost once this UI ships upstream.
 */
private enum class PreviewScreen {
    Onboarding,
    Permissions,
    Library,
    NowPlaying,
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResonixUiPlayground()
        }
    }
}

@Composable
private fun ResonixUiPlayground() {
    var currentScreen by remember { mutableStateOf(PreviewScreen.Onboarding) }

    // Faked playback state — there is no audio engine in this sandbox.
    val queue = DemoTrackCatalog.queue
    var currentTrack by remember { mutableStateOf(DemoTrackCatalog.current) }
    var isPlaying by remember { mutableStateOf(true) }
    var progressMs by remember { mutableLongStateOf(45_000L) }

    // The accent drives the whole palette: every border, background gradient
    // and highlight retints with it. In the real app it will be extracted
    // from the album artwork; here the demo track supplies it directly.
    val accent: Color = currentTrack.artGradient.firstOrNull() ?: Color(0xFF0066FF)

    fun selectTrack(track: DemoTrack) {
        currentTrack = track
        progressMs = 0L
        isPlaying = true
    }

    fun skipBy(offset: Int) {
        val index = queue.indexOf(currentTrack)
        if (index < 0) return
        val nextIndex = ((index + offset) % queue.size + queue.size) % queue.size
        selectTrack(queue[nextIndex])
    }

    // Ticks the fake position forward while playing; restarts on track change
    // so the loop always checks the new track's duration.
    LaunchedEffect(isPlaying, currentTrack) {
        while (isPlaying) {
            delay(500)
            progressMs = if (progressMs + 500 >= currentTrack.durationMs) {
                0L
            } else {
                progressMs + 500
            }
        }
    }

    val progressFraction = if (currentTrack.durationMs > 0) {
        (progressMs.toFloat() / currentTrack.durationMs.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    ResonixTheme(accentColor = accent) {
        Surface(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                PreviewScreen.Onboarding -> OnboardingScreen(
                    onGetStarted = { currentScreen = PreviewScreen.Permissions },
                )

                PreviewScreen.Permissions -> PermissionScreen(
                    onContinue = { currentScreen = PreviewScreen.Library },
                )

                PreviewScreen.Library -> LibraryScreen(
                    tracks = queue,
                    currentTrack = currentTrack,
                    isPlaying = isPlaying,
                    progressFraction = progressFraction,
                    onTrackClick = { track -> selectTrack(track) },
                    onPlayPauseClick = { isPlaying = !isPlaying },
                    onNextClick = { skipBy(1) },
                    onExpandPlayer = { currentScreen = PreviewScreen.NowPlaying },
                )

                PreviewScreen.NowPlaying -> NowPlayingScreen(
                    track = currentTrack,
                    queue = queue,
                    isPlaying = isPlaying,
                    progressMs = progressMs,
                    onPlayPauseClick = { isPlaying = !isPlaying },
                    onSeek = { progressMs = it.coerceIn(0L, currentTrack.durationMs) },
                    onPrevious = { skipBy(-1) },
                    onNext = { skipBy(1) },
                    onBack = { currentScreen = PreviewScreen.Library },
                    onTrackSelectedFromQueue = { track -> selectTrack(track) },
                )
            }
        }
    }
}
