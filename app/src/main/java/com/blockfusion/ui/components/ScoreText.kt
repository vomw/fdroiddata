package com.blockfusion.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun ScoreText(
    score: Long,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: TextUnit = 64.sp,
    fontWeight: FontWeight = FontWeight.Light
) {
    AutoResizableText(
        text = score.toString(),
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight
    )
}
