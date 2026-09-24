package com.resonix.uidemo.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.resonix.uidemo.ui.components.PlayerIconButton
import com.resonix.uidemo.ui.components.settings.AppearanceThemeSection
import com.resonix.uidemo.ui.components.settings.AudioOutputSection
import com.resonix.uidemo.ui.components.settings.DataAboutSection
import com.resonix.uidemo.ui.components.settings.DeviceRoutingSheet
import com.resonix.uidemo.ui.components.settings.DspSection
import com.resonix.uidemo.ui.components.settings.LibraryScannerSection
import com.resonix.uidemo.ui.components.settings.LiquidGlassControlsSheet
import com.resonix.uidemo.ui.components.settings.OutputEngineSheet
import com.resonix.uidemo.ui.components.settings.SettingsActions
import com.resonix.uidemo.ui.components.settings.SettingsGlass
import com.resonix.uidemo.ui.components.settings.SettingsNavigationRow
import com.resonix.uidemo.ui.components.settings.SystemAudioFocusSection
import com.resonix.uidemo.ui.settings.MusicFolder
import com.resonix.uidemo.ui.settings.SettingsEvent
import com.resonix.uidemo.ui.settings.SettingsViewModel
import com.resonix.uidemo.ui.settings.settingsQueryMatches
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.ResonixTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private enum class SettingsSheetType { OutputEngine, DeviceRouting, LiquidGlass }

private data class SettingsSearchEntry(
    val title: String,
    val keywords: String,
    val sectionIndex: Int,
    val sectionLabel: String,
)

private val SettingsSearchIndex: List<SettingsSearchEntry> = listOf(
    SettingsSearchEntry("Pure Pitch Black", "amoled black background oled pitch", 0, "Appearance & theme"),
    SettingsSearchEntry("Liquid Glass Controls", "blur alpha border glow glass", 0, "Appearance & theme"),
    SettingsSearchEntry("Dynamic Accent Tinting", "album artwork color accent tint", 0, "Appearance & theme"),
    SettingsSearchEntry("Audiophile Status Badge", "flac bit depth sample rate badge quality", 0, "Appearance & theme"),

    SettingsSearchEntry("Primary Output Engine", "aaudio opensl audiotrack driver output", 1, "Audio output & drivers"),
    SettingsSearchEntry("Bit-Perfect Direct Hardware Access", "audioflinger bypass bit perfect", 1, "Audio output & drivers"),
    SettingsSearchEntry("Device-Based Routing", "speaker wired iem bluetooth usb dac routing", 1, "Audio output & drivers"),
    SettingsSearchEntry("Hardware Buffer Size", "latency buffer low large", 1, "Audio output & drivers"),

    SettingsSearchEntry("Parametric EQ", "peq biquad iir filter bands equalizer", 2, "DSP & sound processing"),
    SettingsSearchEntry("AutoEQ Import", "headphone iem profile txt import eq", 2, "DSP & sound processing"),
    SettingsSearchEntry("Gapless Playback", "silence transition", 2, "DSP & sound processing"),
    SettingsSearchEntry("Crossfade", "fade transition seconds", 2, "DSP & sound processing"),
    SettingsSearchEntry("Peak Limiter", "clipping loud volume limiter", 2, "DSP & sound processing"),
    SettingsSearchEntry("ReplayGain", "normalize loudness volume", 2, "DSP & sound processing"),

    SettingsSearchEntry("Music Folder Selector", "directory picker library folder scan index", 3, "Library & storage scanner"),
    SettingsSearchEntry("Deep NDK Scanner", "ffmpeg tags rescan native scanner", 3, "Library & storage scanner"),
    SettingsSearchEntry("Ignore Short Audio Clips", "ringtone voice note filter short", 3, "Library & storage scanner"),

    SettingsSearchEntry("Audio Focus Behavior", "duck pause notification focus", 4, "System & audio focus"),
    SettingsSearchEntry("Headset Actions", "pause disconnect resume plug headphones", 4, "System & audio focus"),
    SettingsSearchEntry("Keep Screen Awake", "screen on display wake", 4, "System & audio focus"),

    SettingsSearchEntry("Export Settings", "backup json export config", 5, "Data & about"),
    SettingsSearchEntry("Import Settings", "restore json import config", 5, "Data & about"),
    SettingsSearchEntry("About Resonix", "ndk ffmpeg oboe version engine build", 5, "Data & about"),
)

@Composable
fun SettingsScreen(
    onClose: () -> Unit,
    viewModel: SettingsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    var isSearching by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var activeSheet by remember { mutableStateOf<SettingsSheetType?>(null) }
    var pendingScrollSection by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is SettingsEvent.Message -> snackbarHostState.showSnackbar(event.text)
            }
        }
    }

    LaunchedEffect(pendingScrollSection, isSearching) {
        val target = pendingScrollSection
        if (target != null && !isSearching) {
            listState.scrollToItem(target)
            pendingScrollSection = null
        }
    }

    val folderPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        runCatching {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION,
            )
        }
        viewModel.onMusicFolderAdded(MusicFolder(uri = uri.toString(), displayLabel = readableTreeLabel(uri)))
    }

    val autoEqPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        coroutineScope.launch {
            val name = withContext(Dispatchers.IO) { queryDisplayName(context, uri) } ?: readableTreeLabel(uri)
            viewModel.onAutoEqProfileSelected(name)
        }
    }

    val exportPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json"),
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val json = viewModel.exportSettingsJson()
        coroutineScope.launch {
            val wrote = withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openOutputStream(uri)?.use { stream ->
                        stream.write(json.toByteArray(Charsets.UTF_8))
                    }
                }.isSuccess
            }
            if (wrote) {
                viewModel.onSettingsExported()
            } else {
                snackbarHostState.showSnackbar("Couldn't write that file")
            }
        }
    }

    val importPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        coroutineScope.launch {
            val text = withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        stream.readBytes().toString(Charsets.UTF_8)
                    }
                }.getOrNull()
            }
            if (text != null) {
                viewModel.importSettingsJson(text)
            } else {
                snackbarHostState.showSnackbar("Couldn't read that file")
            }
        }
    }

    val actions = remember(viewModel) {
        SettingsActions(
            setPurePitchBlack = viewModel::setPurePitchBlack,
            onOpenLiquidGlassControls = { activeSheet = SettingsSheetType.LiquidGlass },
            setDynamicAccentTinting = viewModel::setDynamicAccentTinting,
            setAudiophileStatusBadge = viewModel::setAudiophileStatusBadge,

            onOpenOutputEngineSheet = { activeSheet = SettingsSheetType.OutputEngine },
            setBitPerfectDirectAccess = viewModel::setBitPerfectDirectAccess,
            onOpenDeviceRouting = { activeSheet = SettingsSheetType.DeviceRouting },
            setBufferSize = viewModel::setBufferSize,

            setParametricEqEnabled = viewModel::setParametricEqEnabled,
            onOpenParametricEqEditor = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Full band editor isn't built in this sandbox yet")
                }
            },
            onImportAutoEq = { autoEqPicker.launch(arrayOf("text/plain")) },
            onClearAutoEqProfile = viewModel::clearAutoEqProfile,
            setGaplessPlayback = viewModel::setGaplessPlayback,
            setCrossfadeDuration = viewModel::setCrossfadeDuration,
            setPeakLimiterEnabled = viewModel::setPeakLimiterEnabled,
            setReplayGainEnabled = viewModel::setReplayGainEnabled,

            onAddMusicFolder = { folderPicker.launch(null) },
            onRemoveMusicFolder = viewModel::onMusicFolderRemoved,
            setIgnoreShortClips = viewModel::setIgnoreShortClips,
            onStartDeepScan = viewModel::startDeepScan,

            setAudioFocusBehavior = viewModel::setAudioFocusBehavior,
            setPauseOnHeadsetDisconnect = viewModel::setPauseOnHeadsetDisconnect,
            setResumeOnHeadsetConnect = viewModel::setResumeOnHeadsetConnect,
            setKeepScreenAwake = viewModel::setKeepScreenAwake,

            onExportSettings = { exportPicker.launch("resonix-settings.json") },
            onImportSettings = { importPicker.launch(arrayOf("application/json")) },
        )
    }

    val searchResults = remember(query) {
        if (query.isBlank()) {
            emptyList()
        } else {
            SettingsSearchIndex.filter { entry -> settingsQueryMatches(query, entry.title, entry.keywords) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SettingsGlass.ScreenBackground),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SettingsTopBar(
                isSearching = isSearching,
                query = query,
                onQueryChange = { query = it },
                onToggleSearch = {
                    isSearching = !isSearching
                    if (!isSearching) query = ""
                },
                onClose = onClose,
            )

            if (isSearching && query.isNotBlank()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentPadding = PaddingValues(horizontal = GlassTokens.ScreenPaddingH, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(SettingsGlass.CardSpacing),
                ) {
                    if (searchResults.isEmpty()) {
                        item { NoSearchResults(query) }
                    } else {
                        items(searchResults) { entry ->
                            SettingsNavigationRow(
                                icon = Icons.Filled.Search,
                                title = entry.title,
                                subtitle = "In ${entry.sectionLabel}",
                                onClick = {
                                    pendingScrollSection = entry.sectionIndex
                                    isSearching = false
                                    query = ""
                                },
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                ) {
                    item { AppearanceThemeSection(uiState, actions) }
                    item { AudioOutputSection(uiState, actions) }
                    item { DspSection(uiState, actions) }
                    item { LibraryScannerSection(uiState, actions) }
                    item { SystemAudioFocusSection(uiState, actions) }
                    item { DataAboutSection(uiState, actions) }
                    item { Spacer(modifier = Modifier.height(GlassTokens.ScreenPaddingBottom)) }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )

        when (activeSheet) {
            SettingsSheetType.OutputEngine -> OutputEngineSheet(
                selected = uiState.primaryOutputEngine,
                onSelect = viewModel::setPrimaryOutputEngine,
                onDismissRequest = { activeSheet = null },
            )

            SettingsSheetType.DeviceRouting -> DeviceRoutingSheet(
                routingPreferences = uiState.routingPreferences,
                onEngineChange = viewModel::setRoutingEngine,
                onBitPerfectChange = viewModel::setRoutingBitPerfect,
                onDismissRequest = { activeSheet = null },
            )

            SettingsSheetType.LiquidGlass -> LiquidGlassControlsSheet(
                blurIntensity = uiState.blurIntensity,
                glassAlpha = uiState.glassAlpha,
                borderGlow = uiState.borderGlow,
                onBlurIntensityChange = viewModel::setBlurIntensity,
                onGlassAlphaChange = viewModel::setGlassAlpha,
                onBorderGlowChange = viewModel::setBorderGlow,
                onDismissRequest = { activeSheet = null },
            )

            null -> Unit
        }
    }
}

@Composable
private fun SettingsTopBar(
    isSearching: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    onToggleSearch: () -> Unit,
    onClose: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = GlassTokens.ScreenPaddingH + 8.dp,
                    end = GlassTokens.ScreenPaddingH,
                    top = 20.dp,
                    bottom = if (isSearching) 10.dp else 16.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f),
            )
            PlayerIconButton(onClick = onToggleSearch, size = 44.dp) {
                Icon(
                    imageVector = if (isSearching) Icons.Filled.Close else Icons.Filled.Search,
                    contentDescription = if (isSearching) "Close search" else "Search settings",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            PlayerIconButton(onClick = onClose, size = 44.dp) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close settings",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp),
                )
            }
        }

        if (isSearching) {
            SettingsSearchField(
                query = query,
                onQueryChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = GlassTokens.ScreenPaddingH)
                    .padding(bottom = 14.dp),
            )
        }
    }
}

@Composable
private fun SettingsSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(50)
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(Color.White.copy(alpha = 0.06f))
            .border(
                width = SettingsGlass.BorderWidth,
                color = SettingsGlass.BorderColor.copy(alpha = SettingsGlass.BorderAlpha),
                shape = shape,
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.5f),
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = "Search settings",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.35f),
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                cursorBrush = SolidColor(SettingsGlass.Accent),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun NoSearchResults(query: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.25f),
            modifier = Modifier.size(36.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "No settings match \u201C${query.trim()}\u201D",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.55f),
        )
    }
}

private fun readableTreeLabel(uri: Uri): String {
    val raw = uri.lastPathSegment ?: return "Selected folder"
    val afterColon = raw.substringAfterLast(':')
    val trimmed = afterColon.substringAfterLast('/')
    return trimmed.ifBlank { afterColon.ifBlank { raw } }
}

private fun queryDisplayName(context: Context, uri: Uri): String? = runCatching {
    context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index >= 0) cursor.getString(index) else null
        } else {
            null
        }
    }
}.getOrNull()

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SettingsScreenPreview() {
    ResonixTheme {
        SettingsScreen(onClose = {}, viewModel = remember { SettingsViewModel() })
    }
}
