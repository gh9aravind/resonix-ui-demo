package com.resonix.uidemo.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.text.SimpleDateFormat
import java.util.Date
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Owns [SettingsUiState] for the Settings screen. Every toggle in this
 * sandbox writes to local state only — there is no DataStore layer and no
 * real audio engine underneath, so nothing persists across process death
 * yet and nothing is wired into actual playback. The scan and export/import
 * flows are real (a coroutine runs, a file is actually read or written);
 * what they *act on* — a native scanner, a DSP chain — is simulated.
 *
 * This ViewModel deliberately never touches `Context`/`ContentResolver`.
 * File I/O (writing the exported JSON, reading an imported file or an
 * AutoEQ profile) happens in the Composable via Activity Result contracts,
 * which then hands this ViewModel a plain String or URI string to store.
 */
class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _events = Channel<SettingsEvent>(Channel.BUFFERED)
    val events: Flow<SettingsEvent> = _events.receiveAsFlow()

    private fun update(transform: (SettingsUiState) -> SettingsUiState) {
        _uiState.value = transform(_uiState.value)
    }

    private fun notify(text: String) {
        _events.trySend(SettingsEvent.Message(text))
    }

    // -- Appearance & Theme ------------------------------------------------

    fun setPurePitchBlack(enabled: Boolean) = update { it.copy(purePitchBlack = enabled) }
    fun setBlurIntensity(value: Float) = update { it.copy(blurIntensity = value.coerceIn(0f, 1f)) }
    fun setGlassAlpha(value: Float) = update { it.copy(glassAlpha = value.coerceIn(0.02f, 0.30f)) }
    fun setBorderGlow(value: Float) = update { it.copy(borderGlow = value.coerceIn(0f, 1f)) }
    fun setDynamicAccentTinting(enabled: Boolean) = update { it.copy(dynamicAccentTinting = enabled) }
    fun setAudiophileStatusBadge(enabled: Boolean) = update { it.copy(audiophileStatusBadge = enabled) }

    // -- Audio Output & Drivers ----------------------------------------------

    fun setPrimaryOutputEngine(engine: AudioEngineOption) =
        update { it.copy(primaryOutputEngine = engine) }

    fun setBitPerfectDirectAccess(enabled: Boolean) =
        update { it.copy(bitPerfectDirectAccess = enabled) }

    fun setBufferSize(option: BufferSizeOption) = update { it.copy(bufferSize = option) }

    fun setRoutingEngine(device: RoutingDevice, engine: AudioEngineOption) = update { state ->
        state.copy(
            routingPreferences = state.routingPreferences.map { pref ->
                if (pref.device == device) pref.copy(engine = engine) else pref
            },
        )
    }

    fun setRoutingBitPerfect(device: RoutingDevice, enabled: Boolean) = update { state ->
        state.copy(
            routingPreferences = state.routingPreferences.map { pref ->
                if (pref.device == device) pref.copy(bitPerfect = enabled) else pref
            },
        )
    }

    // -- DSP & Sound Processing ----------------------------------------------

    fun setParametricEqEnabled(enabled: Boolean) = update { it.copy(parametricEqEnabled = enabled) }
    fun onAutoEqProfileSelected(profileName: String?) = update { it.copy(autoEqProfile = profileName) }
    fun clearAutoEqProfile() = update { it.copy(autoEqProfile = null) }
    fun setGaplessPlayback(enabled: Boolean) = update { it.copy(gaplessPlayback = enabled) }

    fun setCrossfadeDuration(seconds: Float) =
        update { it.copy(crossfadeDurationSeconds = seconds.coerceIn(0f, 12f)) }

    fun setPeakLimiterEnabled(enabled: Boolean) = update { it.copy(peakLimiterEnabled = enabled) }
    fun setReplayGainEnabled(enabled: Boolean) = update { it.copy(replayGainEnabled = enabled) }

    // -- Library & Storage Scanner --------------------------------------------

    fun onMusicFolderAdded(folder: MusicFolder) = update { state ->
        if (state.musicFolders.any { it.uri == folder.uri }) {
            state
        } else {
            state.copy(musicFolders = state.musicFolders + folder)
        }
    }

    fun onMusicFolderRemoved(uri: String) = update { state ->
        state.copy(musicFolders = state.musicFolders.filterNot { it.uri == uri })
    }

    fun setIgnoreShortClips(enabled: Boolean) = update { it.copy(ignoreShortClips = enabled) }

    /**
     * Simulates a rescan. The real app will hand this to the native
     * media-tag reader described in the spec; this UI sandbox has no such
     * engine, so it just runs a timed progress state and reports a summary
     * against however many folders are currently configured.
     */
    fun startDeepScan() {
        if (_uiState.value.isScanning) return
        viewModelScope.launch {
            update { it.copy(isScanning = true, lastScanSummary = null) }
            delay(2200)
            val folderCount = _uiState.value.musicFolders.size
            val summary = if (folderCount == 0) {
                "No folders selected yet"
            } else {
                "Scanned $folderCount folder${if (folderCount == 1) "" else "s"}"
            }
            update { it.copy(isScanning = false, lastScanSummary = summary) }
            notify(summary)
        }
    }

    // -- System & Audio Focus ---------------------------------------------------

    fun setAudioFocusBehavior(behavior: AudioFocusBehavior) =
        update { it.copy(audioFocusBehavior = behavior) }

    fun setPauseOnHeadsetDisconnect(enabled: Boolean) =
        update { it.copy(pauseOnHeadsetDisconnect = enabled) }

    fun setResumeOnHeadsetConnect(enabled: Boolean) =
        update { it.copy(resumeOnHeadsetConnect = enabled) }

    fun setKeepScreenAwake(enabled: Boolean) = update { it.copy(keepScreenAwake = enabled) }

    // -- Data & About -----------------------------------------------------------

    fun exportSettingsJson(): String = SettingsJson.encode(_uiState.value)

    /** Call after the UI has successfully written [exportSettingsJson]'s output to disk. */
    fun onSettingsExported() {
        val timestamp = SimpleDateFormat.getDateTimeInstance().format(Date())
        update { it.copy(lastExportedAt = timestamp) }
        notify("Settings exported")
    }

    fun importSettingsJson(json: String) {
        runCatching { SettingsJson.decode(json, _uiState.value) }
            .onSuccess { decoded ->
                _uiState.value = decoded
                notify("Settings imported")
            }
            .onFailure {
                notify("That file couldn't be read as Resonix settings")
            }
    }
}
