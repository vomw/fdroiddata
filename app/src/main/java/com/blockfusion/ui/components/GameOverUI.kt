package com.blockfusion.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.blockfusion.logic.SkinType

@Composable
fun GameOverUI(
    score: Long,
    highScore: Long,
    highScoreAtStart: Long = 0L,
    currentSkin: SkinType = SkinType.CLASSIC,
    highContrastMode: Boolean = false,
    onRestart: () -> Unit,
    onTriggerHaptic: () -> Unit = {}
) {
    val buttonTextColor = if (currentSkin == SkinType.SILVER || currentSkin == SkinType.AMBER || currentSkin == SkinType.OCEAN || currentSkin == SkinType.PASTEL) Color.Black else Color.White

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val scrollState = rememberScrollState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .safeDrawingPadding()
                .padding(32.dp)
                .widthIn(max = 360.dp)
                .heightIn(max = 500.dp)
                .then(
                    if (highContrastMode) Modifier.background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp)).padding(2.dp)
                    else Modifier
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            currentSkin.boardColor,
                            currentSkin.backgroundColor
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 40.dp, vertical = 32.dp)
        ) {
            AutoResizableText(
                text = "GAME OVER",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                modifier = Modifier.padding(bottom = 24.dp).fillMaxWidth()
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "SCORE",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                ScoreText(
                    score = score,
                    color = Color.White,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Light
                )
                
                if (score > highScoreAtStart && score > 0L) {
                    Text(
                        text = if (highScoreAtStart == 0L) "FIRST RECORD!" else "NEW RECORD!",
                        color = if (highContrastMode) Color(0xFFFFB74D) else Color(0xFFFFA726),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                } else {
                    Text(
                        text = "BEST: $highScore",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
                
                Button(
                    onClick = {
                        onTriggerHaptic()
                        onRestart()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = currentSkin.accentColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                        .then(
                            if (highContrastMode) Modifier.background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp)).padding(2.dp)
                            else Modifier
                        )
                ) {
                    Text(
                        text = "TRY AGAIN",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = buttonTextColor
                    )
                }
            }
        }
    }
}
