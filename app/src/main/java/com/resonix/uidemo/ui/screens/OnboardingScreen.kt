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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resonix.uidemo.ui.components.GlassButton
import com.resonix.uidemo.ui.components.GlassCard
import com.resonix.uidemo.ui.components.GlassCircleBadge
import com.resonix.uidemo.ui.components.GlassIconBadge
import com.resonix.uidemo.ui.components.segmentShape
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.ResonixTheme
import com.resonix.uidemo.ui.theme.glassScreenBackground
import com.resonix.uidemo.ui.theme.segmentPosition
import com.resonix.uidemo.ui.theme.softIconShadow

private data class OnboardingFeature(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
)

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
) {
    val accent = MaterialTheme.colorScheme.primary
    val glass = LocalGlassColors.current

    val features = remember {
        listOf(
            OnboardingFeature(
                icon = Icons.Filled.GraphicEq,
                title = "Immersive playback",
                subtitle = "A glass-clear now-playing screen that takes its colour from your artwork.",
            ),
            OnboardingFeature(
                icon = Icons.Filled.Nightlight,
                title = "Built for the dark",
                subtitle = "Near-black surfaces and soft edges, tuned for late listening.",
            ),
            OnboardingFeature(
                icon = Icons.Filled.OfflineBolt,
                title = "Local and lossless",
                subtitle = "Your library plays untouched, straight off the device.",
            ),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .glassScreenBackground(accent),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = GlassTokens.ScreenPaddingH),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            GlassCircleBadge(
                icon = Icons.Filled.GraphicEq,
                contentDescription = "Resonix",
                modifier = Modifier.softIconShadow(alpha = 0.4f, shadowRadius = 54.dp),
                tint = accent,
            )

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "RESONIX",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 6.sp,
                color = glass.textPrimary,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Your music, reimagined in glass.",
                style = MaterialTheme.typography.bodyLarge,
                color = glass.textSecondary,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(GlassTokens.SegmentGap),
            ) {
                features.forEachIndexed { index, feature ->
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = segmentShape(index, features.size),
                        position = segmentPosition(index, features.size),
                        contentPadding = PaddingValues(
                            horizontal = GlassTokens.SegmentPaddingH,
                            vertical = GlassTokens.SegmentPaddingV,
                        ),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            GlassIconBadge(
                                icon = feature.icon,
                                contentDescription = null,
                                tint = accent,
                            )
                            Spacer(modifier = Modifier.width(GlassTokens.SegmentIconSpacing))
                            Column {
                                Text(
                                    text = feature.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = glass.textPrimary,
                                )
                                Spacer(modifier = Modifier.height(GlassTokens.RowTextSpacing))
                                Text(
                                    text = feature.subtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = glass.textSecondary,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            GlassButton(
                text = "Get started",
                onClick = onGetStarted,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(GlassTokens.ScreenPaddingBottom))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun OnboardingScreenPreview() {
    ResonixTheme {
        OnboardingScreen(onGetStarted = {})
    }
}
