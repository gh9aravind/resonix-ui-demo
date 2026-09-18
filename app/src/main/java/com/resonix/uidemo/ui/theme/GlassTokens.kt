package com.resonix.uidemo.ui.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Single source of truth for every glass design value. Nothing in the UI
 * layer should hardcode a radius, padding, or alpha — read it from here.
 *
 * The alpha values are the important part. A glass surface reads as glass
 * because its fill is nearly transparent and its border is a *faint*
 * gradient that fades from top to bottom, mimicking light catching an
 * edge. Saturated borders and coloured drop shadows read as neon instead.
 */
object GlassTokens {

    // -- Glass surfaces ----------------------------------------------------
    val CornerRadius = 18.dp
    val BorderThickness = 0.5.dp
    val BorderTopAlpha = 0.20f
    val BorderBottomAlpha = 0.04f
    val FillAlpha = 0.08f
    val FillAlphaStrong = 0.12f

    // -- Segmented groups (settings-style stacked rows) --------------------
    val SegmentGap = 1.5.dp
    val SegmentCornerLarge = 22.dp
    val SegmentCornerSmall = 5.dp
    val SegmentMinHeight = 72.dp
    val SegmentPaddingH = 18.dp
    val SegmentPaddingV = 12.dp
    val SegmentIconBoxSize = 46.dp
    val SegmentIconSize = 25.dp
    val SegmentIconSpacing = 16.dp

    // -- Screen layout -----------------------------------------------------
    val ScreenPaddingH = 16.dp
    val ScreenPaddingBottom = 24.dp
    val SectionSpacing = 12.dp
    val SafeBottomMin = 16.dp
    val FloatingBarBottomSpacing = 8.dp

    // -- List rows ---------------------------------------------------------
    val RowPaddingH = 14.dp
    val RowPaddingV = 10.dp
    val RowIconSize = 32.dp
    val RowIconInnerSize = 20.dp
    val RowIconBgAlpha = 0.12f
    val RowIconSpacing = 14.dp
    val RowTextSpacing = 2.dp
    val ChevronSize = 18.dp

    // -- Dividers ----------------------------------------------------------
    val DividerThickness = 0.5.dp
    val DividerStartIndent = 60.dp
    val DividerAlpha = 0.30f

    // -- Bottom sheets -----------------------------------------------------
    val SheetCornerRadius = 28.dp
    val SheetPaddingH = 16.dp
    val SheetContentPaddingH = 20.dp
    val SheetContentPaddingTop = 16.dp
    val SheetContentPaddingBottom = 24.dp
    val SheetDragHandleWidth = 40.dp
    val SheetDragHandleHeight = 4.dp
    val SheetDragHandleBottomPadding = 12.dp
    val SheetTitleBottomPadding = 16.dp
    val SheetListMaxHeight = 480.dp
    val SheetListCornerRadius = 24.dp
    val SheetOptionPaddingH = 20.dp
    val SheetOptionPaddingV = 14.dp
    val SheetOptionIconSize = 20.dp
    val SheetOptionIconSpacing = 12.dp

    // -- Library -----------------------------------------------------------
    val LibraryCardRadius = 16.dp
    val LibrarySmallRadius = 12.dp
    val LibraryChipHeight = 36.dp
    val LibraryBadgeSize = 46.dp
    val LibraryItemGap = 6.dp
    val LibraryTabHeight = 44.dp
    val LibraryTabPaddingH = 18.dp

    // -- Player ------------------------------------------------------------
    val PlayerControlsPaddingH = 16.dp
    val PlayerControlsGap = 16.dp
    val PlayerCoverCornerRadius = 24.dp

    // -- Screen background veils -------------------------------------------
    val BackgroundTopAlpha = 0.22f
    val BackgroundMidAlpha = 0.06f

    // -- Text --------------------------------------------------------------
    val TextSecondaryAlpha = 0.65f
    val TextDisabledAlpha = 0.38f
    val ChevronAlpha = 0.30f
    val SubtitleSize = 11.sp
    val SubtitleLineHeight = 14.sp
    val RowTitleSize = 15.sp
    val SectionTitleSize = 12.sp

    // -- Motion ------------------------------------------------------------
    const val PressScale = 0.96f
    const val PressDampingRatio = 0.6f
}
