package com.blockfusion.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AutoResizableText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: TextUnit = 32.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign = TextAlign.Center
) {
    var fontSizeValue by remember(text, fontSize) { mutableStateOf(fontSize) }
    var readyToDraw by remember(text, fontSizeValue) { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val maxWidthPx = constraints.maxWidth.toFloat()

        Text(
            text = text,
            color = color,
            fontSize = fontSizeValue,
            fontWeight = fontWeight,
            letterSpacing = letterSpacing,
            textAlign = textAlign,
            softWrap = false,
            maxLines = 1,
            modifier = Modifier.drawWithContent {
                if (readyToDraw) drawContent()
            },
            onTextLayout = { layoutResult ->
                if (layoutResult.didOverflowWidth && maxWidthPx > 0) {
                    // Si se pasa, calculamos un nuevo tamaño con un margen del 5% para seguridad
                    val ratio = (maxWidthPx / layoutResult.size.width.toFloat()) * 0.95f
                    val newSize = (fontSizeValue.value * ratio).sp
                    
                    if (newSize.value < fontSizeValue.value && newSize.value > 5f) {
                        fontSizeValue = newSize
                        readyToDraw = false // Volvemos a medir con el nuevo tamaño
                    } else {
                        readyToDraw = true
                    }
                } else {
                    // Si entra bien, solo lo mostramos si el ancho es razonable (no durante el inicio de una animación)
                    if (maxWidthPx > 10f || !layoutResult.didOverflowWidth) {
                        readyToDraw = true
                    }
                }
            }
        )
    }
}
