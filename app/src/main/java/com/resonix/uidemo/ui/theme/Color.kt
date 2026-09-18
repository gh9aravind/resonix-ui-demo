package com.resonix.uidemo.ui.theme

import androidx.compose.ui.graphics.Color

// --- Resonix seed accents ---------------------------------------------------
// These seed the Material colour scheme. In the real app the accent is
// extracted from the album artwork; here we seed it from the demo track.
val ElectricBlue = Color(0xFF0066FF)
val NeonCyan = Color(0xFF00F0FF)
val VibrantPurple = Color(0xFF8A2BE2)

// --- Neutral bases the glass sits on ----------------------------------------
// Glass needs a genuinely neutral, near-black base. A saturated navy base
// fights the accent tint that the background gradient layers on top.
val SurfaceBlack = Color(0xFF000000)
val SurfaceNear = Color(0xFF0A0A0A)
val SurfaceDeep = Color(0xFF101010)
val SurfaceRaised = Color(0xFF141416)
val SurfaceCardOpaque = Color(0xFF1C1C1E)

// --- Text -------------------------------------------------------------------
val TextPrimary = Color(0xFFF5F7FF)
val TextSecondary = Color(0xFFA8B0C4)
val TextDisabled = Color(0xFF565D75)

// --- Status -----------------------------------------------------------------
val ErrorRed = Color(0xFFFF4D6D)
val SuccessGreen = Color(0xFF00E6A0)

// --- Legacy aliases ---------------------------------------------------------
// Kept so the existing components keep compiling while the screens are
// migrated onto the glass design system. Remove once migration is done.
val VoidBlack = SurfaceBlack
val DeepNavy = SurfaceNear
val DeepNavySecondary = SurfaceDeep
val GlassSurface = Color(0x1A080A12)
