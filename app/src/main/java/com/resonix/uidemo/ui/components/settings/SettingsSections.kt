package com.resonix.uidemo.ui.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.components.GlassIconBadge
import com.resonix.uidemo.ui.components.PlayerIconButton
import com.resonix.uidemo.ui.settings.AudioFocusBehavior
import com.resonix.uidemo.ui.settings.BufferSizeOption
import com.resonix.uidemo.ui.settings.MusicFolder
import com.resonix.uidemo.ui.settings.SettingsUiState
import com.resonix.uidemo.ui.theme.GlassTokens
import kotlin.math.roundToInt

/**
 * Every callback the six Settings sections need, bundled so section
 * composables stay readable instead of taking twenty separate lambda
 * parameters. [com.resonix.uidemo.ui.screens.SettingsScreen] builds one
 * real instance wired to a [com.resonix.uidemo.ui.settings.SettingsViewModel];
 * a preview can supply a stub.
 */
@Immutable
data class SettingsActions(
    val setPurePitchBlack: (Boolean) -> Unit,
    val onOpenLiquidGlassControls: () -> Unit,
    val setDynamicAccentTinting: (Boolean) -> Unit,
    val setAudiophileStatusBadge: (Boolean) -> Unit,

    val onOpenOutputEngineSheet: () -> Unit,
    val setBitPerfectDirectAccess: (Boolean) -> Unit,
    val onOpenDeviceRouting: () -> Unit,
    val setBufferSize: (BufferSizeOption) -> Unit,

    val setParametricEqEnabled: (Boolean) -> Unit,
    val onOpenParametricEqEditor: () -> Unit,
    val onImportAutoEq: () -> Unit,
    val onClearAutoEqProfile: () -> Unit,
    val setGaplessPlayback: (Boolean) -> Unit,
    val setCrossfadeDuration: (Float) -> Unit,
    val setPeakLimiterEnabled: (Boolean) -> Unit,
    val setReplayGainEnabled: (Boolean) -> Unit,

    val onAddMusicFolder: () -> Unit,
    val onRemoveMusicFolder: (String) -> Unit,
    val setIgnoreShortClips: (Boolean) -> Unit,
    val onStartDeepScan: () -> Unit,

    val setAudioFocusBehavior: (AudioFocusBehavior) -> Unit,
    val setPauseOnHeadsetDisconnect: (Boolean) -> Unit,
    val setResumeOnHeadsetConnect: (Boolean) -> Unit,
    val setKeepScreenAwake: (Boolean) -> Unit,

    val onExportSettings: () -> Unit,
    val onImportSettings: () -> Unit,
)

@Composable
fun AppearanceThemeSection(state: SettingsUiState, actions: SettingsActions) {
    SettingsSection(title = "Appearance & theme") {
        SettingsToggleRow(
            icon = Icons.Filled.Contrast,
            title = "Pure Pitch Black (#000000)",
            subtitle = "True AMOLED black instead of a near-black surface",
            checked = state.purePitchBlack,
            onCheckedChange = actions.setPurePitchBlack,
        )
        SettingsNavigationRow(
            icon = Icons.Filled.BlurOn,
            title = "Liquid Glass Controls",
            subtitle = "Blur intensity, glass alpha, border glow",
            onClick = actions.onOpenLiquidGlassControls,
        )
        SettingsToggleRow(
            icon = Icons.Filled.Palette,
            title = "Dynamic Accent Tinting",
            subtitle = "Tint the UI from the current track's artwork",
            checked = state.dynamicAccentTinting,
            onCheckedChange = actions.setDynamicAccentTinting,
        )
        SettingsToggleRow(
            icon = Icons.Filled.HighQuality,
            title = "Audiophile Status Badge",
            subtitle = "Show format on the player, e.g. FLAC • 24-bit/192kHz",
            checked = state.audiophileStatusBadge,
            onCheckedChange = actions.setAudiophileStatusBadge,
        )
    }
}

@Composable
fun AudioOutputSection(state: SettingsUiState, actions: SettingsActions) {
    SettingsSection(title = "Audio output & drivers") {
        SettingsNavigationRow(
            icon = Icons.Filled.Output,
            title = "Primary Output Engine",
            subtitle = state.primaryOutputEngine.description,
            trailingText = state.primaryOutputEngine.shortLabel,
            onClick = actions.onOpenOutputEngineSheet,
        )
        SettingsToggleRow(
            icon = Icons.Filled.Bolt,
            title = "Bit-Perfect Direct Hardware Access",
            subtitle = "Bypass Android AudioFlinger entirely",
            checked = state.bitPerfectDirectAccess,
            onCheckedChange = actions.setBitPerfectDirectAccess,
        )
        SettingsNavigationRow(
            icon = Icons.Filled.DeviceHub,
            title = "Device-Based Routing",
            subtitle = "Speaker, wired, Bluetooth, and USB DAC mappings",
            onClick = actions.onOpenDeviceRouting,
        )
        SettingsSegmentedRow(
            icon = Icons.Filled.Memory,
            title = "Hardware Buffer Size",
            subtitle = "Trade latency for underrun safety",
            options = BufferSizeOption.entries,
            selected = state.bufferSize,
            optionLabel = { it.displayName },
            onSelect = actions.setBufferSize,
        )
    }
}

@Composable
fun DspSection(state: SettingsUiState, actions: SettingsActions) {
    SettingsSection(title = "DSP & sound processing") {
        SettingsToggleRow(
            icon = Icons.Filled.Equalizer,
            title = "Parametric EQ (PEQ)",
            subtitle = "64-bit biquad IIR filters",
            checked = state.parametricEqEnabled,
            onCheckedChange = actions.setParametricEqEnabled,
        )
        SettingsNavigationRow(
            icon = Icons.Filled.Tune,
            title = "Edit filter bands",
            subtitle = "${state.parametricEqBandCount} bands configured",
            enabled = state.parametricEqEnabled,
            onClick = actions.onOpenParametricEqEditor,
        )
        SettingsActionRow(
            icon = Icons.Filled.UploadFile,
            title = "AutoEQ Import (.txt)",
            subtitle = state.autoEqProfile ?: "No headphone/IEM profile imported",
            actionLabel = if (state.autoEqProfile != null) "Remove" else "Import",
            onClick = if (state.autoEqProfile != null) actions.onClearAutoEqProfile else actions.onImportAutoEq,
        )
        SettingsToggleRow(
            icon = Icons.Filled.Sync,
            title = "Gapless Playback",
            subtitle = "No silence between consecutive tracks",
            checked = state.gaplessPlayback,
            onCheckedChange = actions.setGaplessPlayback,
        )
        SettingsGlassCard {
            SettingsSliderRow(
                title = "Crossfade",
                value = state.crossfadeDurationSeconds,
                onValueChange = actions.setCrossfadeDuration,
                valueRange = 0f..12f,
                valueLabel = { seconds -> if (seconds <= 0f) "Off" else "${seconds.roundToInt()}s" },
                helperText = "0s stays gapless; higher values blend between tracks",
            )
        }
        SettingsToggleRow(
            icon = Icons.Filled.GraphicEq,
            title = "Peak Limiter",
            subtitle = "Prevent digital clipping on loud passages",
            checked = state.peakLimiterEnabled,
            onCheckedChange = actions.setPeakLimiterEnabled,
        )
        SettingsToggleRow(
            icon = Icons.Filled.VolumeUp,
            title = "ReplayGain",
            subtitle = "Normalize loudness across tracks",
            checked = state.replayGainEnabled,
            onCheckedChange = actions.setReplayGainEnabled,
        )
    }
}

@Composable
fun LibraryScannerSection(state: SettingsUiState, actions: SettingsActions) {
    SettingsSection(title = "Library & storage scanner") {
        SettingsActionRow(
            icon = Icons.Filled.Folder,
            title = "Music Folder Selector",
            subtitle = if (state.musicFolders.isEmpty()) {
                "No folders selected yet"
            } else {
                "${state.musicFolders.size} folder${if (state.musicFolders.size == 1) "" else "s"} selected"
            },
            actionLabel = "Add",
            onClick = actions.onAddMusicFolder,
        )
        MusicFolderChips(
            folders = state.musicFolders,
            onRemove = actions.onRemoveMusicFolder,
        )
        SettingsActionRow(
            icon = Icons.Filled.DocumentScanner,
            title = "Deep NDK Scanner",
            subtitle = state.lastScanSummary
                ?: "Simulated in this sandbox — no native scanner is linked",
            actionLabel = if (state.isScanning) "Scanning" else "Scan now",
            isLoading = state.isScanning,
            onClick = actions.onStartDeepScan,
        )
        SettingsToggleRow(
            icon = Icons.Filled.FilterAlt,
            title = "Ignore Short Audio Clips",
            subtitle = "Skip tracks under ${state.shortClipThresholdSeconds}s (ringtones, voice notes)",
            checked = state.ignoreShortClips,
            onCheckedChange = actions.setIgnoreShortClips,
        )
    }
}

@Composable
fun SystemAudioFocusSection(state: SettingsUiState, actions: SettingsActions) {
    SettingsSection(title = "System & audio focus") {
        SettingsSegmentedRow(
            icon = Icons.Filled.NotificationsActive,
            title = "Audio Focus Behavior",
            subtitle = "What happens when another app makes a sound",
            options = AudioFocusBehavior.entries,
            selected = state.audioFocusBehavior,
            optionLabel = { it.displayName },
            onSelect = actions.setAudioFocusBehavior,
        )
        SettingsToggleRow(
            icon = Icons.Filled.Headset,
            title = "Pause on Disconnect",
            subtitle = "Stop playback when headphones are unplugged",
            checked = state.pauseOnHeadsetDisconnect,
            onCheckedChange = actions.setPauseOnHeadsetDisconnect,
        )
        SettingsToggleRow(
            icon = Icons.Filled.Headset,
            title = "Resume on Reconnect",
            subtitle = "Start playback again when headphones are plugged back in",
            checked = state.resumeOnHeadsetConnect,
            onCheckedChange = actions.setResumeOnHeadsetConnect,
        )
        SettingsToggleRow(
            icon = Icons.Filled.Brightness7,
            title = "Keep Screen Awake",
            subtitle = "Keep the screen on while the player is visible",
            checked = state.keepScreenAwake,
            onCheckedChange = actions.setKeepScreenAwake,
        )
    }
}

@Composable
fun DataAboutSection(state: SettingsUiState, actions: SettingsActions) {
    SettingsSection(title = "Data & about") {
        SettingsActionRow(
            icon = Icons.Filled.FileDownload,
            title = "Export Settings",
            subtitle = state.lastExportedAt?.let { "Last exported $it" }
                ?: "Save your configuration and EQ presets as JSON",
            actionLabel = "Export",
            onClick = actions.onExportSettings,
        )
        SettingsActionRow(
            icon = Icons.Filled.FileUpload,
            title = "Import Settings",
            subtitle = "Restore from a previously exported JSON file",
            actionLabel = "Import",
            onClick = actions.onImportSettings,
        )
        AboutEngineCard()
    }
}

@Composable
private fun AboutEngineCard() {
    SettingsGlassCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GlassIconBadge(icon = Icons.Filled.Info, contentDescription = null, tint = SettingsGlass.Accent)
            Spacer(modifier = Modifier.width(GlassTokens.SegmentIconSpacing))
            Text(
                text = "About Resonix",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        EngineInfoLine(label = "App version", value = "1.0 (UI sandbox)")
        EngineInfoLine(label = "NDK", value = "Not linked in this sandbox")
        EngineInfoLine(label = "FFmpeg", value = "Not linked in this sandbox")
        EngineInfoLine(label = "Oboe", value = "Not linked in this sandbox")

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "This build is a pure Kotlin/Compose UI sandbox — no C++, NDK, or native " +
                "audio engine is compiled in. These rows will report real build info once " +
                "Resonix's audio engine lands in the main app.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.55f),
        )
    }
}

@Composable
private fun EngineInfoLine(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.65f),
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.85f),
        )
    }
}

@Composable
private fun MusicFolderChips(
    folders: List<MusicFolder>,
    onRemove: (String) -> Unit,
) {
    if (folders.isEmpty()) return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SettingsGlass.CornerRadius))
            .background(Color.White.copy(alpha = 0.04f))
            .padding(vertical = 4.dp),
    ) {
        folders.forEachIndexed { index, folder ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.FolderOpen,
                    contentDescription = null,
                    tint = SettingsGlass.Accent,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = folder.displayLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                PlayerIconButton(onClick = { onRemove(folder.uri) }, size = 28.dp, iconSize = 14.dp) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Remove folder",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
            if (index != folders.lastIndex) {
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.06f),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 14.dp),
                )
            }
        }
    }
}
