package com.resonix.uidemo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.resonix.uidemo.ui.theme.GlassBorderBrush
import com.resonix.uidemo.ui.theme.GlassSurface
import com.resonix.uidemo.ui.theme.TextPrimary
import com.resonix.uidemo.ui.theme.TextSecondary

/**
 * A glassmorphic wrapper around Material3's ModalBottomSheet: a
 * transparent system container (so no default white/grey surface shows
 * through), a custom neon drag handle, and our own translucent glass
 * panel — gradient fill plus a 1dp neon border — behind the content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = sheetShape,
        containerColor = Color.Transparent,
        contentColor = TextPrimary,
        tonalElevation = 0.dp,
        dragHandle = null,
        scrimColor = Color.Black.copy(alpha = 0.55f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(sheetShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White.copy(alpha = 0.07f), GlassSurface)
                    )
                )
                .border(width = 1.dp, brush = GlassBorderBrush, shape = sheetShape)
                .padding(bottom = 28.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(TextSecondary.copy(alpha = 0.4f))
                )
            }

            content()
        }
    }
}
