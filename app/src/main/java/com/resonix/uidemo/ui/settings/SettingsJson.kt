package com.resonix.uidemo.ui.settings

/**
 * A tiny, dependency-free JSON round-tripper for [SettingsUiState].
 *
 * This is deliberately not a general-purpose JSON library: it only ever
 * reads back what [encode] itself wrote, one `"key": value` pair per line.
 * That's enough for a flat settings backup and avoids pulling in
 * kotlinx.serialization (and its Gradle plugin) for a handful of fields.
 *
 * Music folder URIs are intentionally excluded from the backup. A
 * content:// URI's storage permission is granted per install, so restoring
 * one after a reinstall would just be a dead link — folders are meant to
 * be re-picked instead.
 */
object SettingsJson {

    private const val SCHEMA_VERSION = "1"

    fun encode(state: SettingsUiState): String {
        val fields = linkedMapOf(
            "schemaVersion" to SCHEMA_VERSION,
            "purePitchBlack" to state.purePitchBlack.toString(),
            "blurIntensity" to state.blurIntensity.toString(),
            "glassAlpha" to state.glassAlpha.toString(),
            "borderGlow" to state.borderGlow.toString(),
            "dynamicAccentTinting" to state.dynamicAccentTinting.toString(),
            "audiophileStatusBadge" to state.audiophileStatusBadge.toString(),
            "primaryOutputEngine" to state.primaryOutputEngine.name,
            "bitPerfectDirectAccess" to state.bitPerfectDirectAccess.toString(),
            "bufferSize" to state.bufferSize.name,
            "parametricEqEnabled" to state.parametricEqEnabled.toString(),
            "parametricEqBandCount" to state.parametricEqBandCount.toString(),
            "gaplessPlayback" to state.gaplessPlayback.toString(),
            "crossfadeDurationSeconds" to state.crossfadeDurationSeconds.toString(),
            "peakLimiterEnabled" to state.peakLimiterEnabled.toString(),
            "replayGainEnabled" to state.replayGainEnabled.toString(),
            "ignoreShortClips" to state.ignoreShortClips.toString(),
            "shortClipThresholdSeconds" to state.shortClipThresholdSeconds.toString(),
            "audioFocusBehavior" to state.audioFocusBehavior.name,
            "pauseOnHeadsetDisconnect" to state.pauseOnHeadsetDisconnect.toString(),
            "resumeOnHeadsetConnect" to state.resumeOnHeadsetConnect.toString(),
            "keepScreenAwake" to state.keepScreenAwake.toString(),
        )

        val body = fields.entries.joinToString(separator = ",\n") { (key, value) ->
            "  \"$key\": ${jsonValue(value)}"
        }
        return "{\n$body\n}"
    }

    /** Applies whatever fields are present in [json] on top of [base]. */
    fun decode(json: String, base: SettingsUiState): SettingsUiState {
        val fields = parse(json)

        fun bool(key: String, default: Boolean) = fields[key]?.toBooleanStrictOrNull() ?: default
        fun float(key: String, default: Float) = fields[key]?.toFloatOrNull() ?: default
        fun int(key: String, default: Int) = fields[key]?.toIntOrNull() ?: default
        fun engine(key: String, default: AudioEngineOption) =
            fields[key]?.let { name -> AudioEngineOption.entries.find { it.name == name } } ?: default
        fun buffer(key: String, default: BufferSizeOption) =
            fields[key]?.let { name -> BufferSizeOption.entries.find { it.name == name } } ?: default
        fun focus(key: String, default: AudioFocusBehavior) =
            fields[key]?.let { name -> AudioFocusBehavior.entries.find { it.name == name } } ?: default

        return base.copy(
            purePitchBlack = bool("purePitchBlack", base.purePitchBlack),
            blurIntensity = float("blurIntensity", base.blurIntensity),
            glassAlpha = float("glassAlpha", base.glassAlpha),
            borderGlow = float("borderGlow", base.borderGlow),
            dynamicAccentTinting = bool("dynamicAccentTinting", base.dynamicAccentTinting),
            audiophileStatusBadge = bool("audiophileStatusBadge", base.audiophileStatusBadge),
            primaryOutputEngine = engine("primaryOutputEngine", base.primaryOutputEngine),
            bitPerfectDirectAccess = bool("bitPerfectDirectAccess", base.bitPerfectDirectAccess),
            bufferSize = buffer("bufferSize", base.bufferSize),
            parametricEqEnabled = bool("parametricEqEnabled", base.parametricEqEnabled),
            parametricEqBandCount = int("parametricEqBandCount", base.parametricEqBandCount),
            gaplessPlayback = bool("gaplessPlayback", base.gaplessPlayback),
            crossfadeDurationSeconds = float("crossfadeDurationSeconds", base.crossfadeDurationSeconds),
            peakLimiterEnabled = bool("peakLimiterEnabled", base.peakLimiterEnabled),
            replayGainEnabled = bool("replayGainEnabled", base.replayGainEnabled),
            ignoreShortClips = bool("ignoreShortClips", base.ignoreShortClips),
            shortClipThresholdSeconds = int("shortClipThresholdSeconds", base.shortClipThresholdSeconds),
            audioFocusBehavior = focus("audioFocusBehavior", base.audioFocusBehavior),
            pauseOnHeadsetDisconnect = bool("pauseOnHeadsetDisconnect", base.pauseOnHeadsetDisconnect),
            resumeOnHeadsetConnect = bool("resumeOnHeadsetConnect", base.resumeOnHeadsetConnect),
            keepScreenAwake = bool("keepScreenAwake", base.keepScreenAwake),
        )
    }

    private fun jsonValue(rawValue: String): String {
        val isBareValue = rawValue.toBooleanStrictOrNull() != null || rawValue.toDoubleOrNull() != null
        return if (isBareValue) rawValue else "\"${rawValue.replace("\"", "\\\"")}\""
    }

    private fun parse(json: String): Map<String, String> = json.lineSequence()
        .mapNotNull { rawLine ->
            val line = rawLine.trim().trimEnd(',')
            val separatorIndex = line.indexOf(':')
            if (separatorIndex < 0) return@mapNotNull null
            val key = line.substring(0, separatorIndex).trim().trim('"')
            val value = line.substring(separatorIndex + 1).trim().trim('"')
            key.takeIf { it.isNotEmpty() }?.let { it to value }
        }
        .toMap()
}
