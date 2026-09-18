package com.resonix.uidemo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.glassSurface
import com.resonix.uidemo.ui.theme.pressScaleClickable

/**
 * A glass modal. Unlike a card sitting on a screen, a dialog floats over a
 * dark scrim with nothing behind it to tint the glass, so its fill is much
 * more opaque than [GlassCard]'s — otherwise it reads as a smudge rather
 * than a panel.
 */
@Composable
fun GlassDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissText: String? = null,
    dismissible: Boolean = true,
) {
    val glass = LocalGlassColors.current

    Dialog(
        onDismissRequest = { if (dismissible) onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = dismissible,
            dismissOnClickOutside = dismissible,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .glassSurface(
                    shape = RoundedCornerShape(GlassTokens.SegmentCornerLarge),
                    fillColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.94f),
                )
                .padding(24.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = glass.textPrimary,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = glass.textSecondary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (dismissText != null) {
                    GlassTextButton(text = dismissText, onClick = onDismiss)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                GlassButton(text = confirmText, onClick = onConfirm)
            }
        }
    }
}

/**
 * The filled primary action: a solid accent pill that shrinks under the
 * finger. Solid rather than a rainbow gradient — the accent already appears
 * in the screen background and every border, so repeating it as a gradient
 * here just adds noise.
 */
@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = Color.White,
    contentPadding: PaddingValues = PaddingValues(horizontal = 28.dp, vertical = 15.dp),
) {
    val shape = RoundedCornerShape(50)
    Box(
        modifier = modifier
            .pressScaleClickable(onClick = onClick)
            .clip(shape)
            .background(containerColor)
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

/** The outlined secondary action: a glass pill with no fill of its own. */
@Composable
fun GlassOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 28.dp, vertical = 15.dp),
) {
    val shape = RoundedCornerShape(50)
    Box(
        modifier = modifier
            .pressScaleClickable(onClick = onClick)
            .glassSurface(shape = shape)
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = LocalGlassColors.current.textPrimary,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

/** A borderless text action, for the dismiss side of a dialog. */
@Composable
fun GlassTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .pressScaleClickable(onClick = onClick)
            .clip(RoundedCornerShape(50))
            .padding(horizontal = 18.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = LocalGlassColors.current.textSecondary,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
