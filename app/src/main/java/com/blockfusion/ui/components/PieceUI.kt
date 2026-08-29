package com.blockfusion.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset as GeometryOffset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import com.blockfusion.logic.Piece
import com.blockfusion.logic.SkinType

@Composable
fun PieceUI(
    piece: Piece,
    onPlace: (Int, Int) -> Unit,
    skin: SkinType = SkinType.CLASSIC,
    highContrastMode: Boolean = false,
    enabled: Boolean = true,
    isEnding: Boolean = false,
    isGameOver: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (piece.isUsed) {
        Box(modifier = modifier)
        return
    }

    val dragDropState = LocalDragDropState.current
    val targetPieceColor = if (isEnding || isGameOver) Color.Gray else skin.pieceColors.getOrElse(piece.colorIndex) { Color.Gray }
    val animatedPieceColor by animateColorAsState(
        targetValue = targetPieceColor,
        animationSpec = tween(durationMillis = if (isEnding || isGameOver) 1500 else 0),
        label = "pieceColor"
    )
    
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    
    // Posición del bloque ancla (0,0) detectada en tiempo real
    var currentAnchorInRoot by remember { mutableStateOf(GeometryOffset.Zero) }

    LaunchedEffect(enabled) {
        if (!enabled) {
            isDragging = false
            dragDropState.isDragging = false
            dragDropState.activePiece = null
            offsetX = 0f
            offsetY = 0f
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isDragging) 1.1f else 0.7f,
        animationSpec = spring(),
        label = "scale"
    )
    
    val visualOffsetY = if (isDragging) -180f else 0f
    val animatedVisualOffset by animateFloatAsState(targetValue = visualOffsetY, label = "visualOffset")

    val blockSizeDp = 30.dp
    val minX = piece.shape.minOf { it.x }
    val maxX = piece.shape.maxOf { it.x }
    val minY = piece.shape.minOf { it.y }
    val maxY = piece.shape.maxOf { it.y }
    
    val widthCount = maxX - minX + 1
    val heightCount = maxY - minY + 1

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(if (enabled) Modifier.pointerInput(piece.id) {
                detectDragGestures(
                    onDragStart = {
                        if (dragDropState.isDragging) return@detectDragGestures
                        isDragging = true
                        dragDropState.isDragging = true
                        dragDropState.activePiece = piece
                    },
                    onDragEnd = {
                        if (!isDragging) return@detectDragGestures
                        isDragging = false
                        dragDropState.isDragging = false
                        dragDropState.activePiece = null
                        
                        val dropPoint = dragDropState.dragPoint
                        var bestCell: Pair<Int, Int>? = null
                        var minDistance = Float.MAX_VALUE
                        val threshold = 70.dp.toPx() 
                        
                        dragDropState.boardCellBounds.forEach { (pos, rect) ->
                            val distance = (rect.center - dropPoint).getDistance()
                            if (distance < threshold) {
                                if (distance < minDistance) {
                                    minDistance = distance
                                    bestCell = pos
                                }
                            }
                        }

                        bestCell?.let { (r, c) ->
                            onPlace(r, c)
                        }

                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDragCancel = {
                        if (!isDragging) return@detectDragGestures
                        isDragging = false
                        dragDropState.isDragging = false
                        dragDropState.activePiece = null
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        if (!isDragging) return@detectDragGestures
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                        
                        // El dragPoint es la posición del ancla + los offsets actuales
                        val halfBlockPx = 15.dp.toPx()
                        dragDropState.dragPoint = GeometryOffset(
                            currentAnchorInRoot.x + halfBlockPx,
                            currentAnchorInRoot.y + halfBlockPx
                        )
                    }
                )
            } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize(unbounded = true)
                .graphicsLayer {
                    translationX = offsetX
                    translationY = offsetY + animatedVisualOffset
                    scaleX = scale
                    scaleY = scale
                }
        ) {
            Column {
                for (y in 0 until heightCount) {
                    Row {
                        for (x in 0 until widthCount) {
                            val isAnchor = (x + minX == 0 && y + minY == 0)
                            val hasBlock = piece.shape.any { it.x == x + minX && it.y == y + minY }
                            
                            Box(
                                modifier = Modifier
                                    .size(blockSizeDp)
                                    .padding(1.dp)
                                    .onGloballyPositioned { coords ->
                                        if (isAnchor) {
                                            currentAnchorInRoot = coords.positionInRoot()
                                        }
                                    }
                                    .then(
                                        if (highContrastMode && hasBlock) {
                                            Modifier.background(Color.White.copy(alpha = 0.2f)).padding(1.dp)
                                        } else Modifier
                                    )
                                    .background(
                                        if (hasBlock) animatedPieceColor else Color.Transparent,
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}
