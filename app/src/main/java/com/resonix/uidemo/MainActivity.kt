package com.resonix.uidemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.resonix.uidemo.ui.screens.OnboardingScreen
import com.resonix.uidemo.ui.screens.PermissionScreen
import com.resonix.uidemo.ui.theme.ResonixTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * Which screen the in-app preview switcher currently shows. This is a
 * temporary, UI-sandbox-only mechanism — the real Resonix app will drive
 * navigation via NavHost once this UI ships upstream.
 */
private enum class PreviewScreen {
    Onboarding,
    Permissions
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResonixTheme {
                ResonixUiPlayground()
            }
        }
    }
}

@Composable
private fun ResonixUiPlayground() {
    var currentScreen by remember { mutableStateOf(PreviewScreen.Onboarding) }

    Surface(modifier = Modifier.fillMaxSize()) {
        when (currentScreen) {
            PreviewScreen.Onboarding -> OnboardingScreen(
                onGetStarted = { currentScreen = PreviewScreen.Permissions }
            )
            PreviewScreen.Permissions -> PermissionScreen(
                onContinue = {
                    // Loops back to Onboarding — no player screen exists yet
                    // in this standalone visual sandbox.
                    currentScreen = PreviewScreen.Onboarding
                }
            )
        }
    }
}
