package com.blockfusion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blockfusion.logic.SkinType

@Composable
fun ErrorPopupUI(
    title: String,
    message: String,
    currentSkin: SkinType,
    highContrastEnabled: Boolean = false,
    onClose: () -> Unit,
    onTriggerHaptic: () -> Unit = {}
) {
    val isLightAccent = currentSkin == SkinType.SILVER || currentSkin == SkinType.AMBER || currentSkin == SkinType.OCEAN || currentSkin == SkinType.PASTEL
    val closeTextColor = if (isLightAccent) Color.Black else Color.White
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp)
                .widthIn(max = 320.dp)
                .heightIn(max = 400.dp)
                .fillMaxWidth()
                .then(
                    if (highContrastEnabled) Modifier.background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp)).padding(2.dp)
                    else Modifier
                )
                .background(currentSkin.boardColor, RoundedCornerShape(24.dp))
                .verticalScroll(scrollState)
                .padding(24.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onTriggerHaptic()
                    onClose()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .then(
                        if (highContrastEnabled) Modifier.background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp)).padding(2.dp)
                        else Modifier
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = currentSkin.accentColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "CLOSE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = closeTextColor
                )
            }
        }
    }
}
