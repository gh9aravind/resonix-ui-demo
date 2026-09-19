package com.resonix.uidemo.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.resonix.uidemo.ui.model.DemoTrack
import com.resonix.uidemo.ui.navigation.MainTab
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.glassScreenBackground

/**
 * The shared frame every main tab (Home, Search, Settings) sits inside:
 * the accent-tinted background, the docked mini player, and the floating
 * nav bar. Individual tab screens supply only their own header and content
 * — the background and bottom chrome live here, once, so the tabs can
 * never drift out of sync with each other.
 */
@Composable
fun MainScaffold(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    currentTrack: DemoTrack,
    isPlaying: Boolean,
    progressFraction: Float,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onExpandPlayer: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Color = MaterialTheme.colorScheme.primary,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .glassScreenBackground(accent, topBlend = 0.74f, midBlend = 0.92f),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                content()
            }

            MiniPlayerBar(
                track = currentTrack,
                isPlaying = isPlaying,
                progressFraction = progressFraction,
                onPlayPauseClick = onPlayPauseClick,
                onNextClick = onNextClick,
                onExpandClick = onExpandPlayer,
                modifier = Modifier.fillMaxWidth(),
            )

            FloatingNavBar(
                currentTab = currentTab,
                onTabSelected = onTabSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        horizontal = GlassTokens.ScreenPaddingH,
                        vertical = GlassTokens.FloatingBarBottomSpacing,
                    ),
            )
        }
    }
}
