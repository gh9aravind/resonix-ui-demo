package com.resonix.uidemo.ui.components.settings

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.components.GlassIconBadge
import com.resonix.uidemo.ui.components.GlassSwitch
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.pressScaleClickable
import kotlin.math.roundToInt

/**
 * A section header (uppercase label) plus a column of [SettingsGlassCard]
 * rows, spaced as independent cards rather than a single scored pane —
 * see [SettingsGlass.CardSpacing].
 */
@Composable
fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val glass = LocalGlassColors.current
    Column(modifier = modifier.fillMaxWidth()) {
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
            verticalArrangement = Arrangement.spacedBy(SettingsGlass.CardSpacing),
        ) {
            content()
        }
    }
}

/**
 * The icon + title/subtitle layout shared by every row below. Text reads
 * from [LocalGlassColors] rather than [SettingsGlass] on purpose: those
 * colours are already neutral white/grey (`onSurface`-based, not
 * accent-tinted), so reusing them keeps Settings typography consistent
 * with the rest of the app without pulling in any per-track colour.
 */
@Composable
private fun SettingsRowContent(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    iconTint: Color,
    titleColor: Color,
    trailing: @Composable () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        GlassIconBadge(icon = icon, contentDescription = null, tint = iconTint)
        Spacer(modifier = Modifier.width(GlassTokens.SegmentIconSpacing))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = titleColor,
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(GlassTokens.RowTextSpacing))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LocalGlassColors.current.textSecondary,
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        trailing()
    }
}

/** A row whose entire card toggles a [GlassSwitch] when tapped anywhere. */
@Composable
fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accent: Color = SettingsGlass.Accent,
) {
    val glass = LocalGlassColors.current
    SettingsGlassCard(
        modifier = modifier,
        onClick = if (enabled) {
            { onCheckedChange(!checked) }
        } else {
            null
        },
    ) {
        SettingsRowContent(
            icon = icon,
            title = title,
            subtitle = subtitle,
            iconTint = if (enabled) accent else glass.textDisabled,
            titleColor = if (enabled) glass.textPrimary else glass.textDisabled,
        ) {
            GlassSwitch(
                checked = checked,
                onCheckedChange = if (enabled) onCheckedChange else null,
                accent = accent,
            )
        }
    }
}

/** A row that opens something else — a sheet, a screen, a system picker. */
@Composable
fun SettingsNavigationRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailingText: String? = null,
    accent: Color = SettingsGlass.Accent,
    enabled: Boolean = true,
) {
    val glass = LocalGlassColors.current
    SettingsGlassCard(modifier = modifier, onClick = if (enabled) onClick else null) {
        SettingsRowContent(
            icon = icon,
            title = title,
            subtitle = subtitle,
            iconTint = if (enabled) accent else glass.textDisabled,
            titleColor = if (enabled) glass.textPrimary else glass.textDisabled,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (trailingText != null) {
                    Text(
                        text = trailingText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = glass.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 130.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = glass.textSecondary.copy(alpha = GlassTokens.ChevronAlpha),
                    modifier = Modifier.size(GlassTokens.ChevronSize),
                )
            }
        }
    }
}

/** A row with a small pill-shaped action button, e.g. "Browse" or "Scan now". */
@Composable
fun SettingsActionRow(
    icon: ImageVector,
    title: String,
    actionLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    accent: Color = SettingsGlass.Accent,
    isLoading: Boolean = false,
) {
    val glass = LocalGlassColors.current
    SettingsGlassCard(
        modifier = modifier,
        onClick = if (isLoading) null else onClick,
    ) {
        SettingsRowContent(
            icon = icon,
            title = title,
            subtitle = subtitle,
            iconTint = accent,
            titleColor = glass.textPrimary,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = accent,
                )
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(accent.copy(alpha = 0.16f))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = actionLabel,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = accent,
                    )
                }
            }
        }
    }
}

/** A static, non-interactive row — e.g. "Version — 1.0". */
@Composable
fun SettingsInfoRow(
    icon: ImageVector,
    title: String,
    trailingText: String,
    modifier: Modifier = Modifier,
    accent: Color = SettingsGlass.Accent,
) {
    val glass = LocalGlassColors.current
    SettingsGlassCard(modifier = modifier) {
        SettingsRowContent(
            icon = icon,
            title = title,
            subtitle = null,
            iconTint = accent,
            titleColor = glass.textPrimary,
        ) {
            Text(
                text = trailingText,
                style = MaterialTheme.typography.bodyMedium,
                color = glass.textSecondary,
                textAlign = TextAlign.End,
            )
        }
    }
}

/** A row holding an inline segmented control — for small, fixed option sets. */
@Composable
fun <T> SettingsSegmentedRow(
    icon: ImageVector,
    title: String,
    options: List<T>,
    selected: T,
    optionLabel: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    accent: Color = SettingsGlass.Accent,
) {
    val glass = LocalGlassColors.current
    SettingsGlassCard(modifier = modifier) {
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
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(GlassTokens.RowTextSpacing))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = glass.textSecondary,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.06f))
                .padding(3.dp),
        ) {
            options.forEach { option ->
                val isSelected = option == selected
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) accent else Color.Transparent)
                        .pressScaleClickable(pressedScale = 0.96f) { onSelect(option) }
                        .padding(vertical = 9.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = optionLabel(option),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) Color.Black else glass.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

/** A standalone slider with a title and live value label above it. */
@Composable
fun SettingsSliderRow(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    valueLabel: (Float) -> String = { "${(it * 100).roundToInt()}%" },
    accent: Color = SettingsGlass.Accent,
    enabled: Boolean = true,
    helperText: String? = null,
) {
    val glass = LocalGlassColors.current
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (enabled) glass.textPrimary else glass.textDisabled,
            )
            Text(
                text = valueLabel(value),
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) accent else glass.textDisabled,
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            enabled = enabled,
            colors = SliderDefaults.colors(
                thumbColor = accent,
                activeTrackColor = accent,
                inactiveTrackColor = Color.White.copy(alpha = 0.15f),
                disabledThumbColor = glass.textDisabled,
                disabledActiveTrackColor = glass.textDisabled,
                disabledInactiveTrackColor = Color.White.copy(alpha = 0.08f),
            ),
        )
        if (helperText != null) {
            Text(
                text = helperText,
                style = MaterialTheme.typography.bodyMedium,
                color = glass.textSecondary,
            )
        }
    }
}
