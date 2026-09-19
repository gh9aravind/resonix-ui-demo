package com.resonix.uidemo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.navigation.MainTab
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.ResonixTheme
import com.resonix.uidemo.ui.theme.glassScreenBackground
import com.resonix.uidemo.ui.theme.glassSurface
import com.resonix.uidemo.ui.theme.pressScaleClickable

private data class NavDestination(
    val tab: MainTab,
    val icon: ImageVector,
    val contentDescription: String,
)

private val NavDestinations = listOf(
    NavDestination(MainTab.Home, Icons.Filled.Home, "Home"),
    NavDestination(MainTab.Search, Icons.Filled.Search, "Search"),
    NavDestination(MainTab.Settings, Icons.Filled.Settings, "Settings"),
)

/**
 * The primary navigation surface: a floating glass pill inset from the
 * screen edges, so it reads as an object resting on the content rather
 * than a strip of chrome flush against the bottom of the screen.
 */
@Composable
fun FloatingNavBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val glass = LocalGlassColors.current
    val accent = MaterialTheme.colorScheme.primary
    val shape = RoundedCornerShape(50)

    Row(
        modifier = modifier
            .glassSurface(shape = shape, fillColor = glass.glassFillStrong)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        NavDestinations.forEach { destination ->
            NavBarItem(
                icon = destination.icon,
                contentDescription = destination.contentDescription,
                selected = destination.tab == currentTab,
                accent = accent,
                onClick = { onTabSelected(destination.tab) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun NavBarItem(
    icon: ImageVector,
    contentDescription: String,
    selected: Boolean,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val glass = LocalGlassColors.current
    val pillShape = RoundedCornerShape(50)

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(pillShape)
            .background(if (selected) accent.copy(alpha = 0.16f) else Color.Transparent)
            .pressScaleClickable(pressedScale = 0.90f, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (selected) accent else glass.textSecondary,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun FloatingNavBarPreview() {
    ResonixTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .glassScreenBackground(MaterialTheme.colorScheme.primary)
                .padding(20.dp),
        ) {
            FloatingNavBar(
                currentTab = MainTab.Home,
                onTabSelected = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
