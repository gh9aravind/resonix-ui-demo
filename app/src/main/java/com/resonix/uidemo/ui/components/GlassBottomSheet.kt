package com.resonix.uidemo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.theme.GlassTokens
import com.resonix.uidemo.ui.theme.LocalGlassColors
import com.resonix.uidemo.ui.theme.glassSurface

/**
 * A glass modal sheet. Material's own container is made transparent and the
 * glass panel drawn inside it, so the sheet gets the same hairline border
 * and translucent fill as every other surface.
 *
 * Like [GlassDialog], the fill here is far more opaque than a card's: a
 * sheet floats over a scrim with nothing behind it to tint the glass.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    content: @Composable ColumnScope.() -> Unit,
) {
    val glass = LocalGlassColors.current
    val shape = RoundedCornerShape(
        topStart = GlassTokens.SheetCornerRadius,
        topEnd = GlassTokens.SheetCornerRadius,
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = shape,
        containerColor = Color.Transparent,
        contentColor = glass.textPrimary,
        tonalElevation = 0.dp,
        dragHandle = null,
        scrimColor = Color.Black.copy(alpha = 0.55f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glassSurface(
                    shape = shape,
                    fillColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.96f),
                )
                .navigationBarsPadding()
                .padding(bottom = GlassTokens.SheetContentPaddingBottom),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 14.dp,
                        bottom = GlassTokens.SheetDragHandleBottomPadding,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .width(GlassTokens.SheetDragHandleWidth)
                        .height(GlassTokens.SheetDragHandleHeight)
                        .clip(RoundedCornerShape(50))
                        .background(glass.textPrimary.copy(alpha = 0.22f)),
                )
            }

            content()
        }
    }
}
