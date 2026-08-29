package com.blockfusion.logic

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

class PieceGenerator {

    private val shapes = mapOf(
        "single" to listOf(listOf(Offset(0, 0))),
        "v-2" to listOf(listOf(Offset(0, 0), Offset(0, 1))),
        "h-2" to listOf(listOf(Offset(0, 0), Offset(1, 0))),
        "v-3" to listOf(listOf(Offset(0, 0), Offset(0, 1), Offset(0, 2))),
        "h-3" to listOf(listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0))),
        "v-4" to listOf(listOf(Offset(0, 0), Offset(0, 1), Offset(0, 2), Offset(0, 3))),
        "h-4" to listOf(listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0), Offset(3, 0))),
        "v-5" to listOf(listOf(Offset(0, 0), Offset(0, 1), Offset(0, 2), Offset(0, 3), Offset(0, 4))),
        "h-5" to listOf(listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0), Offset(3, 0), Offset(4, 0))),
        "sq-2x2" to listOf(listOf(Offset(0, 0), Offset(1, 0), Offset(0, 1), Offset(1, 1))),
        "sq-3x3" to listOf(
            listOf(
                Offset(0,0), Offset(1,0), Offset(2,0),
                Offset(0,1), Offset(1,1), Offset(2,1),
                Offset(0,2), Offset(1,2), Offset(2,2)
            )
        ),
        "rect-2x3" to listOf(
            listOf(
                Offset(0, 0), Offset(1, 0),
                Offset(0, 1), Offset(1, 1),
                Offset(0, 2), Offset(1, 2)
            )
        ),
        "rect-3x2" to listOf(
            listOf(
                Offset(0, 0), Offset(1, 0), Offset(2, 0),
                Offset(0, 1), Offset(1, 1), Offset(2, 1)
            )
        ),
        "L-small" to listOf(
            listOf(Offset(0, 0), Offset(0, 1), Offset(1, 1)),
            listOf(Offset(1, 0), Offset(1, 1), Offset(0, 1)),
            listOf(Offset(0, 0), Offset(1, 0), Offset(1, 1)),
            listOf(Offset(0, 0), Offset(1, 0), Offset(0, 1))
        ),
        "L-big" to listOf(
            listOf(Offset(0, 0), Offset(0, 1), Offset(0, 2), Offset(1, 2)),
            listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0), Offset(0, 1)),
            listOf(Offset(0, 0), Offset(1, 0), Offset(1, 1), Offset(1, 2)),
            listOf(Offset(2, 0), Offset(0, 1), Offset(1, 1), Offset(2, 1)),
            // Mirrored (J-shapes)
            listOf(Offset(1, 0), Offset(1, 1), Offset(1, 2), Offset(0, 2)),
            listOf(Offset(0, 0), Offset(0, 1), Offset(1, 1), Offset(2, 1)),
            listOf(Offset(0, 0), Offset(1, 0), Offset(0, 1), Offset(0, 2)),
            listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0), Offset(2, 1))
        ),
        "T-shape" to listOf(
            listOf(Offset(1, 0), Offset(0, 1), Offset(1, 1), Offset(2, 1)),
            listOf(Offset(0, 0), Offset(0, 1), Offset(0, 2), Offset(1, 1)),
            listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0), Offset(1, 1)),
            listOf(Offset(1, 0), Offset(1, 1), Offset(1, 2), Offset(0, 1))
        ),
        "Z-shape" to listOf(
            listOf(Offset(0, 0), Offset(1, 0), Offset(1, 1), Offset(2, 1)),
            listOf(Offset(1, 0), Offset(0, 1), Offset(1, 1), Offset(0, 2))
        ),
        "S-shape" to listOf(
            listOf(Offset(1, 0), Offset(2, 0), Offset(0, 1), Offset(1, 1)),
            listOf(Offset(0, 0), Offset(0, 1), Offset(1, 1), Offset(1, 2))
        ),
        "plus" to listOf(
            listOf(Offset(1, 0), Offset(0, 1), Offset(1, 1), Offset(2, 1), Offset(1, 2))
        ),
        "corner-giant" to listOf(
            listOf(Offset(0, 0), Offset(0, 1), Offset(0, 2), Offset(1, 0), Offset(2, 0)),
            listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0), Offset(2, 1), Offset(2, 2)),
            listOf(Offset(0, 2), Offset(1, 2), Offset(2, 2), Offset(2, 1), Offset(2, 0)),
            listOf(Offset(0, 0), Offset(0, 1), Offset(0, 2), Offset(1, 2), Offset(2, 2))
        ),
        "stair" to listOf(
            listOf(Offset(0, 0), Offset(1, 0), Offset(1, 1), Offset(2, 1), Offset(2, 2)),
            listOf(Offset(2, 0), Offset(2, 1), Offset(1, 1), Offset(1, 2), Offset(0, 2)),
            listOf(Offset(0, 0), Offset(0, 1), Offset(1, 1), Offset(1, 2), Offset(2, 2)),
            listOf(Offset(1, 0), Offset(2, 0), Offset(0, 1), Offset(1, 1), Offset(0, 2))
        ),
        "diag-2" to listOf(
            listOf(Offset(0, 0), Offset(1, 1)),
            listOf(Offset(1, 0), Offset(0, 1))
        ),
        "diag-3" to listOf(
            listOf(Offset(0, 0), Offset(1, 1), Offset(2, 2)),
            listOf(Offset(2, 0), Offset(1, 1), Offset(0, 2))
        ),
        "u-shape" to listOf(
            listOf(Offset(0, 0), Offset(0, 1), Offset(1, 1), Offset(2, 1), Offset(2, 0)), // U hacia arriba
            listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0), Offset(0, 1), Offset(2, 1)), // U hacia abajo
            listOf(Offset(0, 0), Offset(1, 0), Offset(1, 1), Offset(1, 2), Offset(0, 2)), // U hacia izquierda
            listOf(Offset(0, 0), Offset(1, 0), Offset(0, 1), Offset(0, 2), Offset(1, 2))  // U hacia derecha
        ),
        "diag-4" to listOf(
            listOf(Offset(0, 0), Offset(1, 1), Offset(2, 2), Offset(3, 3)),
            listOf(Offset(3, 0), Offset(2, 1), Offset(1, 2), Offset(0, 3))
        ),
        "cross-empty" to listOf(
            listOf(Offset(1, 0), Offset(0, 1), Offset(2, 1), Offset(1, 2)) // Cruz sin centro (3x3 con hueco en 1,1)
        ),
        "L-long-4x2" to listOf(
            listOf(Offset(0, 0), Offset(0, 1), Offset(0, 2), Offset(0, 3), Offset(1, 3)),
            listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0), Offset(3, 0), Offset(0, 1)),
            listOf(Offset(0, 0), Offset(1, 0), Offset(1, 1), Offset(1, 2), Offset(1, 3)),
            listOf(Offset(3, 0), Offset(0, 1), Offset(1, 1), Offset(2, 1), Offset(3, 1)),
            // Mirrored
            listOf(Offset(1, 0), Offset(1, 1), Offset(1, 2), Offset(1, 3), Offset(0, 3)),
            listOf(Offset(0, 0), Offset(0, 1), Offset(1, 1), Offset(2, 1), Offset(3, 1)),
            listOf(Offset(0, 0), Offset(1, 0), Offset(0, 1), Offset(0, 2), Offset(0, 3)),
            listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0), Offset(3, 0), Offset(3, 1))
        ),
        "frame-3x3" to listOf(
            listOf(Offset(0,0), Offset(1,0), Offset(2,0), Offset(0,1), Offset(2,1), Offset(0,2), Offset(1,2), Offset(2,2))
        ),
        "w-shape" to listOf(
            listOf(Offset(0,0), Offset(2,0), Offset(0,1), Offset(2,1), Offset(0,2), Offset(1,2), Offset(2,2)), // W
            listOf(Offset(0,0), Offset(1,0), Offset(2,0), Offset(0,1), Offset(0,2), Offset(1,2), Offset(2,2)), // E / M rotada
            listOf(Offset(0,0), Offset(1,0), Offset(2,0), Offset(0,1), Offset(2,1), Offset(0,2), Offset(2,2)), // M
            listOf(Offset(0,0), Offset(1,0), Offset(2,0), Offset(2,1), Offset(0,2), Offset(1,2), Offset(2,2))  // 3 / M rotada
        ),
        "snake-3x3" to listOf(
            listOf(Offset(0,0), Offset(2,0), Offset(1,1), Offset(0,2), Offset(2,2))
        ),
        "rect-4x2" to listOf(
            listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0), Offset(3, 0), Offset(0, 1), Offset(1, 1), Offset(2, 1), Offset(3, 1))
        ),
        "rect-2x4" to listOf(
            listOf(Offset(0, 0), Offset(1, 0), Offset(0, 1), Offset(1, 1), Offset(0, 2), Offset(1, 2), Offset(0, 3), Offset(1, 3))
        ),
        "h-6" to listOf(
            listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0), Offset(3, 0), Offset(4, 0), Offset(5, 0))
        ),
        "v-6" to listOf(
            listOf(Offset(0, 0), Offset(0, 1), Offset(0, 2), Offset(0, 3), Offset(0, 4), Offset(0, 5))
        ),
        "T-long" to listOf(
            listOf(Offset(1, 0), Offset(1, 1), Offset(0, 2), Offset(1, 2), Offset(2, 2)), // T hacia arriba (tallo 3, base 3)
            listOf(Offset(0, 0), Offset(1, 0), Offset(2, 0), Offset(1, 1), Offset(1, 2)), // T hacia abajo
            listOf(Offset(0, 1), Offset(1, 1), Offset(2, 0), Offset(2, 1), Offset(2, 2)), // T hacia izquierda
            listOf(Offset(0, 0), Offset(0, 1), Offset(0, 2), Offset(1, 1), Offset(2, 1))  // T hacia derecha
        )
    )

    private val weights = mapOf(
        "single" to 3,
        "v-2" to 6, "h-2" to 6,
        "v-3" to 10, "h-3" to 10,
        "v-4" to 12, "h-4" to 12,
        "v-5" to 10,  "h-5" to 10,
        "sq-2x2" to 12,
        "sq-3x3" to 10,
        "rect-2x3" to 8,
        "rect-3x2" to 8,
        "L-small" to 10,
        "L-big" to 12,
        "T-shape" to 12,
        "Z-shape" to 10,
        "S-shape" to 10,
        "plus" to 12,
        "corner-giant" to 12,
        "stair" to 12,
        "diag-2" to 8,
        "diag-3" to 10,
        "u-shape" to 12,
        "diag-4" to 14,
        "cross-empty" to 10,
        "L-long-4x2" to 14,
        "frame-3x3" to 16,
        "w-shape" to 12,
        "snake-3x3" to 12,
        "rect-4x2" to 18,
        "rect-2x4" to 18,
        "h-6" to 20,
        "v-6" to 20,
        "T-long" to 16
    )

    fun generatePieces(board: Array<Array<Cell>>, maxColorIndex: Int, score: Long = 0L): List<Piece> {
        // Difficulty increases every 500 points
        // level starts at 1 and increases by 1 for every 500 score points
        val level = (score / 500L) + 1L
        
        // The difficulty multiplier scales with the level. 
        // We can cap it if it gets too insane, or just let it grow.
        val difficultyMultiplier = level.toInt()

        // Try up to MAX_ATTEMPTS to find a "just" set of 3 pieces:
        // a set where there exists at least one valid placement sequence
        // such that after placing each piece the remaining ones still fit.
        val MAX_ATTEMPTS = 40

        repeat(MAX_ATTEMPTS) { attempt ->
            val candidates = if (attempt < MAX_ATTEMPTS - 1) {
                // Normal weighted random generation
                List(3) { getRandomPieceByWeight(maxColorIndex, difficultyMultiplier, score).copy(id = Random.nextInt()) }
            } else {
                // Last resort: force 3 small pieces guaranteed to be placeable
                List(3) { getFallbackPiece(board, maxColorIndex) }
            }

            if (isJustSet(board, candidates)) {
                return candidates
            }
        }

        // Absolute fallback: 3 singles (always fit if board has any free cell)
        return List(3) {
            Piece(
                id = Random.nextInt(),
                shape = listOf(Offset(0, 0)),
                colorIndex = Random.nextInt(maxColorIndex)
            )
        }
    }

    /**
     * Returns true if the set of pieces is "just":
     * there exists at least one permutation/sequence of placements
     * such that after placing piece[i] somewhere on the board,
     * the remaining pieces can also be placed (each one in the
     * resulting board after placing all previous ones).
     *
     * We do a DFS over all orderings × positions to find ONE valid path.
     */
    private fun isJustSet(board: Array<Array<Cell>>, pieces: List<Piece>): Boolean {
        return canContinue(board, pieces, BooleanArray(pieces.size) { false })
    }

    /**
     * DFS: at each step we pick an unplaced piece, try every position for it.
     * After placing it (and clearing any completed lines), we recurse with the
     * remaining pieces. If at any point all pieces are "resolved" (placed or
     * confirmed fittable in the resulting board), we return true.
     *
     * "Just" condition: after placing piece[i] on some valid position,
     * for EVERY remaining unplaced piece j, canPieceFitAnywhere(resultBoard, pieces[j]) == true.
     * (We don't require them all placeable simultaneously, just each independently.)
     */
    private fun canContinue(
        board: Array<Array<Cell>>,
        pieces: List<Piece>,
        placed: BooleanArray
    ): Boolean {
        val size = GameConstants.BOARD_SIZE

        // If all pieces are placed, we succeeded
        if (placed.all { it }) return true

        // Pick the next unplaced piece (try each one)
        for (i in pieces.indices) {
            if (placed[i]) continue

            val shape = pieces[i].shape

            // Try every position on the board for this piece
            for (row in 0 until size) {
                for (col in 0 until size) {
                    if (!canPlacePiece(board, shape, row, col)) continue

                    // Simulate placing the piece
                    val newBoard = placePieceOnBoard(board, shape, row, col, pieces[i].colorIndex)
                    val clearedBoard = clearLines(newBoard)

                    // Mark this piece as placed
                    placed[i] = true

                    // Check that all OTHER unplaced pieces can fit in the resulting board
                    val remainingFit = pieces.indices
                        .filter { !placed[it] }
                        .all { j -> canPieceFitAnywhere(clearedBoard, pieces[j].shape) }

                    if (remainingFit) {
                        // Recurse to verify the full chain
                        if (canContinue(clearedBoard, pieces, placed)) {
                            placed[i] = false // restore (not strictly needed but clean)
                            return true
                        }
                    }

                    placed[i] = false
                }
            }
        }

        return false
    }

    /** Simulate placing a piece on a copy of the board. */
    private fun placePieceOnBoard(
        board: Array<Array<Cell>>,
        shape: List<Offset>,
        row: Int,
        col: Int,
        colorIndex: Int
    ): Array<Array<Cell>> {
        val size = GameConstants.BOARD_SIZE
        val newBoard = Array(size) { r -> Array(size) { c -> board[r][c] } }
        for (offset in shape) {
            newBoard[row + offset.y][col + offset.x] = Cell(isOccupied = true, colorIndex = colorIndex)
        }
        return newBoard
    }

    /** Clear any fully completed rows/columns (mirrors GameViewModel logic). */
    private fun clearLines(board: Array<Array<Cell>>): Array<Array<Cell>> {
        val size = GameConstants.BOARD_SIZE
        val rowsToClear = (0 until size).filter { r -> (0 until size).all { c -> board[r][c].isOccupied } }
        val colsToClear = (0 until size).filter { c -> (0 until size).all { r -> board[r][c].isOccupied } }
        if (rowsToClear.isEmpty() && colsToClear.isEmpty()) return board
        val newBoard = Array(size) { r -> Array(size) { c -> board[r][c] } }
        rowsToClear.forEach { r -> for (c in 0 until size) newBoard[r][c] = Cell() }
        colsToClear.forEach { c -> for (r in 0 until size) newBoard[r][c] = Cell() }
        return newBoard
    }

    /**
     * Fallback piece: a small shape guaranteed to fit somewhere on the board right now.
     * Prefers singles, then 2-cell, then any fitting shape.
     */
    private fun getFallbackPiece(board: Array<Array<Cell>>, maxColorIndex: Int): Piece {
        val colorIndex = Random.nextInt(maxColorIndex)
        val smallShapes = listOf(
            listOf(Offset(0, 0)),                          // single
            listOf(Offset(0, 0), Offset(1, 0)),            // h-2
            listOf(Offset(0, 0), Offset(0, 1))             // v-2
        )
        for (shape in smallShapes) {
            if (canPieceFitAnywhere(board, shape)) {
                return Piece(id = Random.nextInt(), shape = shape, colorIndex = colorIndex)
            }
        }
        // If even 2-cell pieces don't fit, return single (board must have free cells or game should be over)
        return Piece(id = Random.nextInt(), shape = listOf(Offset(0, 0)), colorIndex = colorIndex)
    }

    private fun getRandomPieceByWeight(maxColorIndex: Int, difficultyMultiplier: Int = 1, score: Long = 0L): Piece {
        val tier1Shapes = listOf("plus", "stair", "L-long-4x2", "corner-giant", "v-5", "h-5")
        val tier2Shapes = listOf("u-shape", "diag-4", "cross-empty", "sq-3x3")
        val tier3Shapes = listOf("frame-3x3", "w-shape", "snake-3x3")
        val tier4Shapes = listOf("rect-4x2", "rect-2x4", "h-6", "v-6", "T-long")
        
        val adjustedWeights = weights.filter { (key, _) ->
            when {
                key in tier4Shapes -> score >= 10000
                key in tier3Shapes -> score >= 5000
                key in tier2Shapes -> score >= 2000
                key in tier1Shapes -> score >= 500
                else -> true
            }
        }.mapValues { (key, weight) ->
            val isDifficult = key in listOf(
                "sq-3x3", "v-5", "h-5", "corner-giant", "stair", "plus", "L-big", 
                "u-shape", "diag-4", "cross-empty", "L-long-4x2", "frame-3x3", 
                "w-shape", "snake-3x3", "rect-4x2", "rect-2x4", "h-6", "v-6", "T-long"
            )
            if (isDifficult) weight * difficultyMultiplier else weight
        }
        
        val totalWeight = adjustedWeights.values.sum()
        var random = Random.nextInt(totalWeight)
        
        var selectedKey = "single"
        for ((key, weight) in adjustedWeights) {
            if (random < weight) {
                selectedKey = key
                break
            }
            random -= weight
        }
        
        val variants = shapes[selectedKey] ?: listOf(listOf(Offset(0,0)))
        val shape = variants.random()
        val colorIndex = Random.nextInt(maxColorIndex)
        
        return Piece(id = 0, shape = shape, colorIndex = colorIndex)
    }

    private fun canPieceFitAnywhere(board: Array<Array<Cell>>, shape: List<Offset>): Boolean {
        val size = GameConstants.BOARD_SIZE
        for (row in 0 until size) {
            for (col in 0 until size) {
                if (canPlacePiece(board, shape, row, col)) return true
            }
        }
        return false
    }

    private fun canPlacePiece(board: Array<Array<Cell>>, shape: List<Offset>, row: Int, col: Int): Boolean {
        val size = GameConstants.BOARD_SIZE
        for (offset in shape) {
            val r = row + offset.y
            val c = col + offset.x
            if (r !in 0 until size || c !in 0 until size || board[r][c].isOccupied) return false
        }
        return true
    }
}
