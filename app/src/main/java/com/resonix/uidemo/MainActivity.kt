package com.resonix.uidemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
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
import com.resonix.uidemo.ui.components.MainScaffold
import com.resonix.uidemo.ui.model.DemoTrack
import com.resonix.uidemo.ui.model.DemoTrackCatalog
import com.resonix.uidemo.ui.navigation.MainTab
import com.resonix.uidemo.ui.screens.LibraryScreen
import com.resonix.uidemo.ui.screens.NowPlayingScreen
import com.resonix.uidemo.ui.screens.OnboardingScreen
import com.resonix.uidemo.ui.screens.PermissionScreen
import com.resonix.uidemo.ui.screens.SearchScreen
import com.resonix.uidemo.ui.screens.SettingsScreen
import com.resonix.uidemo.ui.theme.ResonixTheme
import kotlinx.coroutines.delay

private enum class AppStage {
    Onboarding,
    Permissions,
    MainApp,
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
    var appStage by remember { mutableStateOf(AppStage.Onboarding) }
    var currentTab by remember { mutableStateOf(MainTab.Home) }
    var showNowPlaying by remember { mutableStateOf(false) }

    val queue = DemoTrackCatalog.queue
    var currentTrack by remember { mutableStateOf(DemoTrackCatalog.current) }
    var isPlaying by remember { mutableStateOf(true) }
    var progressMs by remember { mutableLongStateOf(45_000L) }

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
            when (appStage) {
                AppStage.Onboarding -> OnboardingScreen(
                    onGetStarted = { appStage = AppStage.Permissions },
                )

                AppStage.Permissions -> PermissionScreen(
                    onContinue = { appStage = AppStage.MainApp },
                )

                AppStage.MainApp -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MainScaffold(
                            currentTab = currentTab,
                            onTabSelected = { currentTab = it },
                            currentTrack = currentTrack,
                            isPlaying = isPlaying,
                            progressFraction = progressFraction,
                            onPlayPauseClick = { isPlaying = !isPlaying },
                            onNextClick = { skipBy(1) },
                            onExpandPlayer = { showNowPlaying = true },
                            accent = accent,
                        ) {
                            when (currentTab) {
                                MainTab.Home -> LibraryScreen(
                                    tracks = queue,
                                    currentTrack = currentTrack,
                                    onTrackClick = { track -> selectTrack(track) },
                                )

                                MainTab.Search -> SearchScreen(
                                    allTracks = queue,
                                    currentTrack = currentTrack,
                                    onTrackClick = { track -> selectTrack(track) },
                                )

                                MainTab.Settings -> SettingsScreen(
                                    onClose = { currentTab = MainTab.Home },
                                )
                            }
                        }

                        if (showNowPlaying) {
                            NowPlayingScreen(
                                track = currentTrack,
                                queue = queue,
                                isPlaying = isPlaying,
                                progressMs = progressMs,
                                onPlayPauseClick = { isPlaying = !isPlaying },
                                onSeek = { progressMs = it.coerceIn(0L, currentTrack.durationMs) },
                                onPrevious = { skipBy(-1) },
                                onNext = { skipBy(1) },
                                onBack = { showNowPlaying = false },
                                onTrackSelectedFromQueue = { track -> selectTrack(track) },
                            )
                        }
                    }
                }
            }
        }
    }
}
