package com.resonix.uidemo.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors

/**
 * A [Switch] retinted to sit on glass: a solid accent thumb/track when on,
 * a barely-there outline when off, rather than Material's default grey.
 */
@Composable
fun GlassSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    accent: Color = MaterialTheme.colorScheme.primary,
) {
    val glass = LocalGlassColors.current

    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = accent,
            checkedBorderColor = Color.Transparent,
            uncheckedThumbColor = glass.textPrimary.copy(alpha = GlassTokens.SwitchUncheckedThumbAlpha),
            uncheckedTrackColor = glass.textPrimary.copy(alpha = GlassTokens.SwitchUncheckedTrackAlpha),
            uncheckedBorderColor = glass.textPrimary.copy(alpha = 0.18f),
        ),
    )
}
