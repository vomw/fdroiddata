package com.blockfusion.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.blockfusion.logic.Cell
import com.blockfusion.logic.GameConstants
import com.blockfusion.logic.Offset
import com.blockfusion.logic.SkinType

@Composable
fun BoardUI(
    board: Array<Array<Cell>>,
    clearingRows: List<Int> = emptyList(),
    clearingCols: List<Int> = emptyList(),
    skin: SkinType = SkinType.CLASSIC,
    highContrastMode: Boolean = false,
    isEnding: Boolean = false,
    isGameOver: Boolean = false,
    onCanPlace: (List<Offset>, Int, Int) -> Boolean = { _, _, _ -> true },
    modifier: Modifier = Modifier
) {
    val dragDropState = LocalDragDropState.current
    
    val hoveredCell: Pair<Int, Int>? = remember(dragDropState.isDragging, dragDropState.dragPoint) {
        if (dragDropState.isDragging) {
            var best: Pair<Int, Int>? = null
            var minDistance = Float.MAX_VALUE
            val maxSnapDistance = 135f 

            dragDropState.boardCellBounds.forEach { (pos, rect) ->
                val distance = (rect.center - dragDropState.dragPoint).getDistance()
                if (distance < maxSnapDistance) {
                    if (distance < minDistance) {
                        minDistance = distance
                        best = pos
                    }
                }
            }
            best
        } else null
    }

    val potentialClears = remember(hoveredCell, board, dragDropState.activePiece) {
        if (hoveredCell != null && dragDropState.activePiece != null) {
            val piece = dragDropState.activePiece!!
            val (hR, hC) = hoveredCell
            
            if (onCanPlace(piece.shape, hR, hC)) {
                val size = GameConstants.BOARD_SIZE
                val tempBoard = Array(size) { r -> 
                    BooleanArray(size) { c -> 
                        board[r][c].isOccupied || piece.shape.any { hR + it.y == r && hC + it.x == c }
                    }
                }
                
                val pRows = (0 until size).filter { r -> (0 until size).all { c -> tempBoard[r][c] } }
                val pCols = (0 until size).filter { c -> (0 until size).all { r -> tempBoard[r][c] } }
                Pair(pRows, pCols)
            } else null
        } else null
    }

    Column(
        modifier = modifier
            .aspectRatio(1f)
            .background(skin.boardColor, RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        for (row in 0 until GameConstants.BOARD_SIZE) {
            Row(modifier = Modifier.weight(1f)) {
                for (col in 0 until GameConstants.BOARD_SIZE) {
                    val cell = board[row][col]
                    val isClearing = row in clearingRows || col in clearingCols
                    
                    var isGhost = false
                    if (hoveredCell != null && dragDropState.activePiece != null) {
                        val piece = dragDropState.activePiece!!
                        val (hR, hC) = hoveredCell
                        if (onCanPlace(piece.shape, hR, hC)) {
                            isGhost = piece.shape.any { offset -> hR + offset.y == row && hC + offset.x == col }
                        }
                    }

                    val isPotentialClear = potentialClears?.let { (pRows, pCols) ->
                        row in pRows || col in pCols
                    } ?: false

                    val clearingScale by animateFloatAsState(
                        targetValue = if (isClearing) 0f else 1f,
                        animationSpec = tween(durationMillis = 300),
                        label = "clearingScale"
                    )

                    val cellTargetColor = when {
                        isPotentialClear -> skin.pieceColors.getOrElse(dragDropState.activePiece?.colorIndex ?: 0) { Color.Gray }
                        cell.isOccupied -> {
                            if (isEnding || isGameOver) Color.Gray 
                            else skin.pieceColors.getOrElse(cell.colorIndex) { Color.Gray }
                        }
                        isGhost -> {
                            val color = skin.pieceColors.getOrElse(dragDropState.activePiece?.colorIndex ?: 0) { Color.Gray }
                            if (isEnding || isGameOver) Color.Gray.copy(alpha = 0.3f) 
                            else color.copy(alpha = 0.3f)
                        }
                        else -> skin.cellEmptyColor
                    }

                    val animatedCellColor by animateColorAsState(
                        targetValue = cellTargetColor,
                        animationSpec = tween(durationMillis = if (isEnding || isGameOver) 1500 else 0),
                        label = "cellColor"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .graphicsLayer {
                                scaleX = clearingScale
                                scaleY = clearingScale
                                alpha = if (isClearing) 0f else 1f
                            }
                            .clip(RoundedCornerShape(4.dp))
                            .onGloballyPositioned { coords ->
                                dragDropState.updateCellBounds(row, col, coords)
                            }
                            .then(
                                if (highContrastMode && (cell.isOccupied || isGhost || isPotentialClear)) {
                                    Modifier.background(Color.White.copy(alpha = 0.2f)).padding(1.dp)
                                } else Modifier
                            )
                            .background(animatedCellColor, RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}
