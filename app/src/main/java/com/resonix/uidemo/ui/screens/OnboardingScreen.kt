package com.resonix.uidemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resonix.uidemo.ui.components.GlassCard
import com.resonix.uidemo.ui.components.GlowButton
import com.resonix.uidemo.ui.theme.ElectricBlue
import com.resonix.uidemo.ui.theme.NeonCyan
import com.resonix.uidemo.ui.theme.ResonixBackgroundBrush
import com.resonix.uidemo.ui.theme.ResonixGlowBrushBottomRight
import com.resonix.uidemo.ui.theme.ResonixGlowBrushTopLeft
import com.resonix.uidemo.ui.theme.ResonixTheme
import com.resonix.uidemo.ui.theme.TextPrimary
import com.resonix.uidemo.ui.theme.TextSecondary
import com.resonix.uidemo.ui.theme.VibrantPurple

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ResonixBackgroundBrush)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopStart)
                .background(ResonixGlowBrushTopLeft)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomEnd)
                .background(ResonixGlowBrushBottomRight)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                NeonCyan.copy(alpha = 0.25f),
                                VibrantPurple.copy(alpha = 0.25f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.GraphicEq,
                    contentDescription = "Resonix logo",
                    tint = NeonCyan,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "RESONIX",
                style = TextStyle(
                    brush = Brush.horizontalGradient(
                        listOf(NeonCyan, ElectricBlue, VibrantPurple)
                    ),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Your music, reimagined in liquid glass.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    OnboardingFeatureRow(
                        title = "Immersive Playback",
                        subtitle = "A glowing, glass-clear now-playing experience."
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OnboardingFeatureRow(
                        title = "Deep Dark Design",
                        subtitle = "Built for late-night listening sessions."
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OnboardingFeatureRow(
                        title = "Local & Lossless",
                        subtitle = "Your library, untouched and uncompressed."
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            GlowButton(
                text = "Get Started",
                onClick = onGetStarted,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun OnboardingFeatureRow(title: String, subtitle: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF020207)
@Composable
private fun OnboardingScreenPreview() {
    ResonixTheme {
        OnboardingScreen(onGetStarted = {})
    }
}
