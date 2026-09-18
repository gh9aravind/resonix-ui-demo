package com.resonix.uidemo.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.resonix.uidemo.ui.components.GlassButton
import com.resonix.uidemo.ui.components.GlassCard
import com.resonix.uidemo.ui.components.GlassIconBadge
import com.resonix.uidemo.ui.components.segmentShape
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.ResonixTheme
import com.resonix.uidemo.ui.theme.SuccessGreen
import com.resonix.uidemo.ui.theme.glassScreenBackground
import com.resonix.uidemo.ui.theme.segmentPosition

private data class PermissionItem(
    val permission: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
)

@Composable
fun PermissionScreen(
    onContinue: () -> Unit,
) {
    val context = LocalContext.current
    val accent = MaterialTheme.colorScheme.primary
    val glass = LocalGlassColors.current

    val audioPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionItems = remember {
        buildList {
            add(
                PermissionItem(
                    permission = audioPermission,
                    title = "Music library",
                    description = "Lets Resonix find the audio files already on your device.",
                    icon = Icons.Filled.LibraryMusic,
                ),
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(
                    PermissionItem(
                        permission = Manifest.permission.POST_NOTIFICATIONS,
                        title = "Playback controls",
                        description = "Shows what's playing in your notification shade.",
                        icon = Icons.Filled.Notifications,
                    ),
                )
            }
        }
    }

    fun readGrantState(): Map<String, Boolean> = permissionItems.associate { item ->
        item.permission to (
            ContextCompat.checkSelfPermission(context, item.permission) ==
                PackageManager.PERMISSION_GRANTED
            )
    }

    var grantState by remember { mutableStateOf(readGrantState()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) {
        // Re-read every permission rather than just the one that resolved, so
        // a grant made from system settings mid-flow is picked up too.
        grantState = readGrantState()
    }

    val allGranted = grantState.values.all { it }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .glassScreenBackground(accent),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = GlassTokens.ScreenPaddingH),
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "One last step",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = glass.textPrimary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Resonix needs a couple of permissions before it can play anything.",
                style = MaterialTheme.typography.bodyMedium,
                color = glass.textSecondary,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(GlassTokens.SegmentGap),
            ) {
                permissionItems.forEachIndexed { index, item ->
                    val isGranted = grantState[item.permission] == true
                    val rowAccent = if (isGranted) SuccessGreen else accent

                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = segmentShape(index, permissionItems.size),
                        position = segmentPosition(index, permissionItems.size),
                        borderColor = rowAccent,
                        contentPadding = PaddingValues(
                            horizontal = GlassTokens.SegmentPaddingH,
                            vertical = GlassTokens.SegmentPaddingV,
                        ),
                        onClick = if (isGranted) null else {
                            { launcher.launch(item.permission) }
                        },
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            GlassIconBadge(
                                icon = item.icon,
                                contentDescription = null,
                                tint = rowAccent,
                            )

                            Spacer(modifier = Modifier.width(GlassTokens.SegmentIconSpacing))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = glass.textPrimary,
                                )
                                Spacer(modifier = Modifier.height(GlassTokens.RowTextSpacing))
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = glass.textSecondary,
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Icon(
                                imageVector = if (isGranted) {
                                    Icons.Filled.CheckCircle
                                } else {
                                    Icons.Filled.RadioButtonUnchecked
                                },
                                contentDescription = if (isGranted) "Granted" else "Not granted",
                                tint = if (isGranted) SuccessGreen else glass.textDisabled,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (allGranted) {
                GlassButton(
                    text = "Continue",
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                GlassButton(
                    text = "Grant permissions",
                    onClick = {
                        val next = permissionItems.firstOrNull { item ->
                            grantState[item.permission] != true
                        }
                        next?.let { launcher.launch(it.permission) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(GlassTokens.ScreenPaddingBottom))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PermissionScreenPreview() {
    ResonixTheme {
        PermissionScreen(onContinue = {})
    }
}
