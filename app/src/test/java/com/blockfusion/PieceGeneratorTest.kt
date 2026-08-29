package com.blockfusion

import com.blockfusion.logic.Cell
import com.blockfusion.logic.GameConstants
import com.blockfusion.logic.PieceGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PieceGeneratorTest {
    @Test
    fun generatesThreeValidPiecesOnAnEmptyBoard() {
        val board = Array(GameConstants.BOARD_SIZE) {
            Array(GameConstants.BOARD_SIZE) { Cell() }
        }
        val pieces = PieceGenerator().generatePieces(board, maxColorIndex = 6)

        assertEquals(3, pieces.size)
        assertTrue(pieces.all { it.shape.isNotEmpty() })
        assertTrue(pieces.all { it.colorIndex in 0 until 6 })
        assertTrue(pieces.all { piece ->
            piece.shape.all { offset ->
                offset.x in 0 until GameConstants.BOARD_SIZE &&
                    offset.y in 0 until GameConstants.BOARD_SIZE
            }
        })
    }
}
