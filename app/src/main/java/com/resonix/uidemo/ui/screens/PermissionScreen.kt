package com.resonix.uidemo.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.resonix.uidemo.ui.components.GlassTile
import com.resonix.uidemo.ui.components.GlowButton
import com.resonix.uidemo.ui.theme.ElectricBlue
import com.resonix.uidemo.ui.theme.NeonCyan
import com.resonix.uidemo.ui.theme.ResonixBackgroundBrush
import com.resonix.uidemo.ui.theme.ResonixGlowBrushBottomRight
import com.resonix.uidemo.ui.theme.ResonixGlowBrushTopLeft
import com.resonix.uidemo.ui.theme.ResonixTheme
import com.resonix.uidemo.ui.theme.SuccessGreen
import com.resonix.uidemo.ui.theme.TextPrimary
import com.resonix.uidemo.ui.theme.TextSecondary
import com.resonix.uidemo.ui.theme.VibrantPurple

private data class PermissionItem(
    val permission: String,
    val title: String,
    val description: String,
    val icon: ImageVector
)

@Composable
fun PermissionScreen(
    onContinue: () -> Unit
) {
    val context = LocalContext.current

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
                    title = "Music Library Access",
                    description = "Lets Resonix scan your device for local audio files.",
                    icon = Icons.Filled.LibraryMusic
                )
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(
                    PermissionItem(
                        permission = Manifest.permission.POST_NOTIFICATIONS,
                        title = "Playback Notifications",
                        description = "Shows the now-playing controls in your notification shade.",
                        icon = Icons.Filled.Notifications
                    )
                )
            }
        }
    }

    val grantedState = remember {
        mutableStateOf(
            permissionItems.associate { item ->
                item.permission to (ContextCompat.checkSelfPermission(
                    context,
                    item.permission
                ) == PackageManager.PERMISSION_GRANTED)
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        // Re-check every permission's live status to keep all tiles in sync,
        // regardless of which single permission the launcher just resolved.
        grantedState.value = permissionItems.associate { item ->
            item.permission to (ContextCompat.checkSelfPermission(
                context,
                item.permission
            ) == PackageManager.PERMISSION_GRANTED)
        }
    }

    val allGranted = grantedState.value.values.all { it }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ResonixBackgroundBrush)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopEnd)
                .background(ResonixGlowBrushTopLeft)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomStart)
                .background(ResonixGlowBrushBottomRight)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "One Last Step",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Resonix needs a couple of permissions to play your music smoothly.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                permissionItems.forEach { item ->
                    val isGranted = grantedState.value[item.permission] == true
                    GlassTile(
                        modifier = Modifier.fillMaxWidth(),
                        accentBrush = if (isGranted) {
                            Brush.linearGradient(listOf(SuccessGreen, SuccessGreen))
                        } else {
                            Brush.linearGradient(listOf(NeonCyan, VibrantPurple))
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBlue.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = NeonCyan
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Icon(
                                imageVector = if (isGranted) {
                                    Icons.Filled.CheckCircle
                                } else {
                                    Icons.Filled.RadioButtonUnchecked
                                },
                                contentDescription = if (isGranted) "Granted" else "Not granted",
                                tint = if (isGranted) SuccessGreen else TextSecondary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (!allGranted) {
                GlowButton(
                    text = "Grant Permissions",
                    onClick = {
                        val next = permissionItems.firstOrNull { item ->
                            grantedState.value[item.permission] != true
                        }
                        next?.let { launcher.launch(it.permission) }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                GlowButton(
                    text = "Continue",
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF020207)
@Composable
private fun PermissionScreenPreview() {
    ResonixTheme {
        PermissionScreen(onContinue = {})
    }
}
