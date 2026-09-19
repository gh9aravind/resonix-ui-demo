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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.components.GlassCard
import com.resonix.uidemo.ui.components.GlassIconBadge
import com.resonix.uidemo.ui.components.GlassSwitch
import com.resonix.uidemo.ui.components.segmentShape
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.ResonixTheme
import com.resonix.uidemo.ui.theme.glassScreenBackground
import com.resonix.uidemo.ui.theme.segmentPosition

/**
 * The Settings tab. Every toggle here is local UI state only — this sandbox
 * has no DataStore or preferences layer yet, so nothing persists and
 * nothing is wired back into playback. The point is to get the look and
 * layout right before that wiring exists.
 */
@Composable
fun SettingsScreen() {
    val glass = LocalGlassColors.current

    var dynamicAccent by remember { mutableStateOf(true) }
    var pureBlack by remember { mutableStateOf(false) }
    var gaplessPlayback by remember { mutableStateOf(true) }
    var normalizeVolume by remember { mutableStateOf(true) }
    var wifiOnlyDownloads by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Settings",
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
                    bottom = 20.dp,
                ),
        )

        SettingsSection(title = "Appearance") {
            SettingsSwitchRow(
                icon = Icons.Filled.Palette,
                title = "Dynamic accent",
                subtitle = "Tint the app from the album artwork",
                checked = dynamicAccent,
                onCheckedChange = { dynamicAccent = it },
                index = 0,
                count = 2,
            )
            SettingsSwitchRow(
                icon = Icons.Filled.Contrast,
                title = "Pure black background",
                subtitle = "True black instead of near-black surfaces",
                checked = pureBlack,
                onCheckedChange = { pureBlack = it },
                index = 1,
                count = 2,
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        SettingsSection(title = "Playback") {
            SettingsSwitchRow(
                icon = Icons.Filled.Speed,
                title = "Gapless playback",
                subtitle = "No silence between consecutive tracks",
                checked = gaplessPlayback,
                onCheckedChange = { gaplessPlayback = it },
                index = 0,
                count = 3,
            )
            SettingsSwitchRow(
                icon = Icons.Filled.VolumeUp,
                title = "Normalize volume",
                subtitle = "Keep loudness consistent across tracks",
                checked = normalizeVolume,
                onCheckedChange = { normalizeVolume = it },
                index = 1,
                count = 3,
            )
            SettingsSwitchRow(
                icon = Icons.Filled.Wifi,
                title = "Download on Wi-Fi only",
                subtitle = "Avoid using mobile data for downloads",
                checked = wifiOnlyDownloads,
                onCheckedChange = { wifiOnlyDownloads = it },
                index = 2,
                count = 3,
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        SettingsSection(title = "About") {
            SettingsInfoRow(
                icon = Icons.Filled.Info,
                title = "Version",
                trailingText = "1.0 (UI sandbox)",
                index = 0,
                count = 2,
            )
            SettingsInfoRow(
                icon = Icons.Filled.Article,
                title = "Open-source licenses",
                trailingText = null,
                index = 1,
                count = 2,
            )
        }

        Spacer(modifier = Modifier.height(GlassTokens.ScreenPaddingBottom))
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    val glass = LocalGlassColors.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontSize = GlassTokens.SectionTitleSize,
            fontWeight = FontWeight.SemiBold,
            color = glass.textSecondary,
            modifier = Modifier.padding(
                start = GlassTokens.ScreenPaddingH + 6.dp,
                end = GlassTokens.ScreenPaddingH,
                bottom = 8.dp,
            ),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GlassTokens.ScreenPaddingH),
            verticalArrangement = Arrangement.spacedBy(GlassTokens.SegmentGap),
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    index: Int,
    count: Int,
) {
    val glass = LocalGlassColors.current
    val accent = MaterialTheme.colorScheme.primary

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = segmentShape(index, count),
        position = segmentPosition(index, count),
        contentPadding = PaddingValues(
            horizontal = GlassTokens.SegmentPaddingH,
            vertical = GlassTokens.SegmentPaddingV,
        ),
        onClick = { onCheckedChange(!checked) },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GlassIconBadge(icon = icon, contentDescription = null, tint = accent)
            Spacer(modifier = Modifier.width(GlassTokens.SegmentIconSpacing))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = glass.textPrimary,
                )
                Spacer(modifier = Modifier.height(GlassTokens.RowTextSpacing))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = glass.textSecondary,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            GlassSwitch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
private fun SettingsInfoRow(
    icon: ImageVector,
    title: String,
    trailingText: String?,
    index: Int,
    count: Int,
) {
    val glass = LocalGlassColors.current
    val accent = MaterialTheme.colorScheme.primary

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = segmentShape(index, count),
        position = segmentPosition(index, count),
        contentPadding = PaddingValues(
            horizontal = GlassTokens.SegmentPaddingH,
            vertical = GlassTokens.SegmentPaddingV,
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GlassIconBadge(icon = icon, contentDescription = null, tint = accent)
            Spacer(modifier = Modifier.width(GlassTokens.SegmentIconSpacing))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = glass.textPrimary,
                modifier = Modifier.weight(1f),
            )
            if (trailingText != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = trailingText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = glass.textSecondary,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SettingsScreenPreview() {
    ResonixTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .glassScreenBackground(MaterialTheme.colorScheme.primary),
        ) {
            SettingsScreen()
        }
    }
}
