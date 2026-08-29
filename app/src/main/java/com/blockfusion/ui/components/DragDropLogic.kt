package com.blockfusion.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot
import com.blockfusion.logic.Piece

class DragDropState {
    var isDragging by mutableStateOf(false)
    var activePiece by mutableStateOf<Piece?>(null)
    var dragPoint by mutableStateOf(Offset.Zero)
    
    // Almacena las coordenadas de las celdas del tablero para colisión
    val boardCellBounds = mutableMapOf<Pair<Int, Int>, Rect>()

    fun updateCellBounds(row: Int, col: Int, coordinates: LayoutCoordinates) {
        boardCellBounds[row to col] = coordinates.boundsInRoot()
    }
}

val LocalDragDropState = compositionLocalOf { DragDropState() }

private fun LayoutCoordinates.boundsInRoot(): Rect {
    val position = positionInRoot()
    return Rect(position.x, position.y, position.x + size.width, position.y + size.height)
}
