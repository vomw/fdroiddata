package com.blockfusion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blockfusion.logic.SkinType

@Composable
fun LogoUI(
    skin: SkinType = SkinType.CLASSIC,
    modifier: Modifier = Modifier
) {
    val logoColors = listOf(
        skin.pieceColors.getOrElse(0) { Color(0xFF42A5F5) },
        skin.pieceColors.getOrElse(1) { Color(0xFF66BB6A) },
        skin.pieceColors.getOrElse(2) { Color(0xFFFFA726) },
        skin.pieceColors.getOrElse(4) { Color(0xFFEF5350) }
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono del Logo: Cuatro bloques pequeños
        Column {
            Row {
                Block(logoColors[0])
                Block(logoColors[1])
            }
            Row {
                Block(logoColors[2])
                Block(logoColors[3])
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Texto del Logo
        Column {
            Text(
                text = "BLOCK",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
            Text(
                text = "FUSION",
                color = skin.accentColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
        }
    }
}

@Composable
private fun Block(color: Color) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .padding(1.dp)
            .background(color, RoundedCornerShape(4.dp))
    )
}
