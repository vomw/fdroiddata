package com.blockfusion.logic

import androidx.compose.ui.graphics.Color

/**
 * Representa una celda individual en el tablero.
 */
data class Cell(
    val isOccupied: Boolean = false,
    val colorIndex: Int = -1
)

/**
 * Coordenada relativa para definir la forma de una pieza.
 */
data class Offset(val x: Int, val y: Int)

/**
 * Definición de una pieza del juego.
 */
data class Piece(
    val id: Int,
    val shape: List<Offset>,
    val colorIndex: Int,
    val isUsed: Boolean = false
)

/**
 * Constantes del juego.
 */
object GameConstants {
    const val BOARD_SIZE = 8
}
