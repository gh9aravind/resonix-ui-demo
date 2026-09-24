package com.resonix.uidemo.ui.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.components.GlassIconBadge
import com.resonix.uidemo.ui.components.GlassSwitch
import com.resonix.uidemo.ui.settings.AudioEngineOption
import com.resonix.uidemo.ui.settings.RoutingDevice
import com.resonix.uidemo.ui.settings.RoutingPreference
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.pressScaleClickable

/** A titled header row shared by every sheet in this file. */
@Composable
private fun SheetHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = GlassTokens.SheetContentPaddingH)
            .padding(bottom = GlassTokens.SheetTitleBottomPadding),
    )
}

/** Lets the person choose Resonix's primary decode-to-output engine. */
@Composable
fun OutputEngineSheet(
    selected: AudioEngineOption,
    onSelect: (AudioEngineOption) -> Unit,
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    SettingsBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState) {
        SheetHeader(title = "Primary output engine")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GlassTokens.SheetContentPaddingH),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AudioEngineOption.entries.forEach { engine ->
                EngineOptionRow(
                    engine = engine,
                    isSelected = engine == selected,
                    onClick = { onSelect(engine) },
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun EngineOptionRow(
    engine: AudioEngineOption,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(SettingsGlass.CornerRadius)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (isSelected) SettingsGlass.Accent.copy(alpha = 0.12f) else Color.Transparent)
            .pressScaleClickable(pressedScale = 0.98f, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = engine.displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) SettingsGlass.Accent else Color.White,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = engine.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.65f),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
            contentDescription = if (isSelected) "Selected" else "Not selected",
            tint = if (isSelected) SettingsGlass.Accent else Color.White.copy(alpha = 0.35f),
            modifier = Modifier.size(22.dp),
        )
    }
}

/** Per-device engine and bit-perfect mappings: Speaker, Wired, Bluetooth, USB DAC. */
@Composable
fun DeviceRoutingSheet(
    routingPreferences: List<RoutingPreference>,
    onEngineChange: (RoutingDevice, AudioEngineOption) -> Unit,
    onBitPerfectChange: (RoutingDevice, Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    SettingsBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState) {
        SheetHeader(title = "Device-based routing")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GlassTokens.SheetContentPaddingH),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            routingPreferences.forEach { preference ->
                DeviceRoutingCard(
                    preference = preference,
                    onEngineChange = { engine -> onEngineChange(preference.device, engine) },
                    onBitPerfectChange = { enabled -> onBitPerfectChange(preference.device, enabled) },
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
    }
}

private fun iconForDevice(device: RoutingDevice): ImageVector = when (device) {
    RoutingDevice.SPEAKER -> Icons.Filled.Speaker
    RoutingDevice.WIRED -> Icons.Filled.Headphones
    RoutingDevice.BLUETOOTH -> Icons.Filled.Bluetooth
    RoutingDevice.USB_DAC -> Icons.Filled.Usb
}

@Composable
private fun DeviceRoutingCard(
    preference: RoutingPreference,
    onEngineChange: (AudioEngineOption) -> Unit,
    onBitPerfectChange: (Boolean) -> Unit,
) {
    val shape = RoundedCornerShape(SettingsGlass.CornerRadius)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color.White.copy(alpha = 0.05f))
            .padding(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GlassIconBadge(
                icon = iconForDevice(preference.device),
                contentDescription = null,
                tint = SettingsGlass.Accent,
                size = 38.dp,
                iconSize = 20.dp,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = preference.device.displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "Bit-perfect",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.65f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            GlassSwitch(
                checked = preference.bitPerfect,
                onCheckedChange = onBitPerfectChange,
                accent = SettingsGlass.Accent,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.06f))
                .padding(3.dp),
        ) {
            AudioEngineOption.entries.forEach { engine ->
                val isSelected = engine == preference.engine
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) SettingsGlass.Accent else Color.Transparent)
                        .pressScaleClickable(pressedScale = 0.96f) { onEngineChange(engine) }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = engine.shortLabel,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) Color.Black else Color.White.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}

/**
 * Live-adjustable glass tuning: blur intensity, glass fill alpha, and
 * border glow, with a small preview swatch that redraws as the sliders
 * move. This sandbox has no real backdrop-blur pipeline shared across the
 * whole app yet (see the note at the bottom of the sheet) — the preview
 * uses [androidx.compose.ui.draw.blur], which is hardware-accelerated on
 * API 31+ and a harmless no-op below that, exactly like the rest of
 * Resonix's glass, which is built from alpha and gradients rather than a
 * real backdrop blur.
 */
@Composable
fun LiquidGlassControlsSheet(
    blurIntensity: Float,
    glassAlpha: Float,
    borderGlow: Float,
    onBlurIntensityChange: (Float) -> Unit,
    onGlassAlphaChange: (Float) -> Unit,
    onBorderGlowChange: (Float) -> Unit,
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    SettingsBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState) {
        SheetHeader(title = "Liquid glass controls")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GlassTokens.SheetContentPaddingH),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            GlassTuningPreview(
                blurIntensity = blurIntensity,
                glassAlpha = glassAlpha,
                borderGlow = borderGlow,
            )

            SettingsSliderRow(
                title = "Blur intensity",
                value = blurIntensity,
                onValueChange = onBlurIntensityChange,
            )
            SettingsSliderRow(
                title = "Glass alpha",
                value = glassAlpha,
                onValueChange = onGlassAlphaChange,
                valueRange = 0.02f..0.30f,
            )
            SettingsSliderRow(
                title = "Border glow",
                value = borderGlow,
                onValueChange = onBorderGlowChange,
            )

            Text(
                text = "Preview only for now — these values are saved, but retinting every " +
                    "screen's glass from them is a follow-up pass.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.5f),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun GlassTuningPreview(
    blurIntensity: Float,
    glassAlpha: Float,
    borderGlow: Float,
) {
    val outerShape = RoundedCornerShape(SettingsGlass.CornerRadius)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clip(outerShape)
            .background(Color(0xFF050505)),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .blur(radius = (blurIntensity * 22).dp)
                .background(
                    Brush.linearGradient(
                        listOf(SettingsGlass.Accent, Color(0xFF8A2BE2), Color(0xFF0066FF)),
                    ),
                ),
        )

        val innerShape = RoundedCornerShape(14.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(56.dp)
                .clip(innerShape)
                .background(Color.White.copy(alpha = glassAlpha))
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        0f to Color.White.copy(alpha = (0.12f + borderGlow * 0.55f).coerceIn(0f, 1f)),
                        1f to Color.White.copy(alpha = 0.02f),
                    ),
                    shape = innerShape,
                ),
        )
    }
}
