package com.resonix.uidemo.ui.settings

/**
 * Every enum and data class the Settings feature reads and writes. Kept
 * free of any Compose or Android import so it can be unit-tested on its
 * own and reused if Settings ever needs a second surface.
 */

enum class AudioEngineOption(
    val displayName: String,
    val shortLabel: String,
    val description: String,
) {
    AAUDIO_EXCLUSIVE(
        displayName = "AAudio (Exclusive Mode)",
        shortLabel = "AAudio",
        description = "Lowest latency; claims the audio device for Resonix alone.",
    ),
    OPENSL_ES(
        displayName = "OpenSL ES",
        shortLabel = "OpenSL",
        description = "Broad device compatibility, slightly higher latency.",
    ),
    AUDIO_TRACK(
        displayName = "AudioTrack",
        shortLabel = "Track",
        description = "The standard Android output path.",
    ),
}

enum class BufferSizeOption(val displayName: String, val description: String) {
    LOW_LATENCY(
        displayName = "Low latency",
        description = "Smallest safe buffer for the fastest response.",
    ),
    LARGE_BUFFER(
        displayName = "Large buffer",
        description = "More headroom against underruns on busy devices.",
    ),
}

enum class AudioFocusBehavior(val displayName: String, val description: String) {
    DUCK(
        displayName = "Duck volume",
        description = "Lower the volume during a notification sound, then restore it.",
    ),
    PAUSE(
        displayName = "Pause playback",
        description = "Stop playback for the duration of the interruption.",
    ),
}

enum class RoutingDevice(val displayName: String) {
    SPEAKER("Speaker"),
    WIRED("Wired IEM / AUX"),
    BLUETOOTH("Bluetooth"),
    USB_DAC("USB DAC"),
}

/** The engine + bit-perfect choice Resonix remembers for one output device. */
data class RoutingPreference(
    val device: RoutingDevice,
    val engine: AudioEngineOption,
    val bitPerfect: Boolean,
)

/** A user-picked local folder to index, shown by its display label. */
data class MusicFolder(
    val uri: String,
    val displayLabel: String,
)

/** The full Settings screen state, owned by [SettingsViewModel]. */
data class SettingsUiState(
    // -- Appearance & Theme ----------------------------------------------
    val purePitchBlack: Boolean = true,
    val blurIntensity: Float = 0.6f,
    val glassAlpha: Float = 0.08f,
    val borderGlow: Float = 0.5f,
    val dynamicAccentTinting: Boolean = true,
    val audiophileStatusBadge: Boolean = true,

    // -- Audio Output & Drivers -------------------------------------------
    val primaryOutputEngine: AudioEngineOption = AudioEngineOption.AAUDIO_EXCLUSIVE,
    val bitPerfectDirectAccess: Boolean = false,
    val bufferSize: BufferSizeOption = BufferSizeOption.LOW_LATENCY,
    val routingPreferences: List<RoutingPreference> = RoutingDevice.entries.map { device ->
        RoutingPreference(
            device = device,
            engine = AudioEngineOption.AAUDIO_EXCLUSIVE,
            bitPerfect = device == RoutingDevice.USB_DAC,
        )
    },

    // -- DSP & Sound Processing --------------------------------------------
    val parametricEqEnabled: Boolean = false,
    val parametricEqBandCount: Int = 8,
    val autoEqProfile: String? = null,
    val gaplessPlayback: Boolean = true,
    val crossfadeDurationSeconds: Float = 0f,
    val peakLimiterEnabled: Boolean = true,
    val replayGainEnabled: Boolean = true,

    // -- Library & Storage Scanner ------------------------------------------
    val musicFolders: List<MusicFolder> = emptyList(),
    val ignoreShortClips: Boolean = true,
    val shortClipThresholdSeconds: Int = 30,
    val isScanning: Boolean = false,
    val lastScanSummary: String? = null,

    // -- System & Audio Focus --------------------------------------------------
    val audioFocusBehavior: AudioFocusBehavior = AudioFocusBehavior.DUCK,
    val pauseOnHeadsetDisconnect: Boolean = true,
    val resumeOnHeadsetConnect: Boolean = false,
    val keepScreenAwake: Boolean = false,

    // -- Data & About ---------------------------------------------------------
    val lastExportedAt: String? = null,
)

/** One-off events the ViewModel fires for the screen to surface as a snackbar. */
sealed interface SettingsEvent {
    data class Message(val text: String) : SettingsEvent
}
