package com.blockfusion.logic

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "game_prefs")

class PersistenceManager(private val context: Context) {

    private val BOARD_KEY = stringPreferencesKey("board")
    private val SCORE_KEY = longPreferencesKey("score")
    private val HIGH_SCORE_KEY = longPreferencesKey("high_score")
    private val PIECES_KEY = stringPreferencesKey("pieces")
    private val HAPTIC_KEY = booleanPreferencesKey("haptic_enabled")
    private val HIGH_CONTRAST_KEY = booleanPreferencesKey("high_contrast_enabled")
    private val SKIN_KEY = stringPreferencesKey("current_skin")

    suspend fun saveGame(board: Array<Array<Cell>>, score: Long, pieces: List<Piece>) {
        context.dataStore.edit { prefs ->
            prefs[BOARD_KEY] = serializeBoard(board)
            prefs[SCORE_KEY] = score
            prefs[PIECES_KEY] = serializePieces(pieces)
        }
    }

    suspend fun saveHighScore(score: Long) {
        context.dataStore.edit { prefs ->
            val current = prefs[HIGH_SCORE_KEY] ?: 0L
            if (score > current) {
                prefs[HIGH_SCORE_KEY] = score
            }
        }
    }

    suspend fun saveSettings(hapticEnabled: Boolean, skin: SkinType, highContrastEnabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[HAPTIC_KEY] = hapticEnabled
            prefs[HIGH_CONTRAST_KEY] = highContrastEnabled
            prefs[SKIN_KEY] = skin.name
        }
    }

    suspend fun clearSavedGame() {
        context.dataStore.edit { prefs ->
            prefs.remove(BOARD_KEY)
            prefs.remove(SCORE_KEY)
            prefs.remove(PIECES_KEY)
        }
    }

    fun getSettings(): Flow<Triple<Boolean, SkinType, Boolean>> = context.dataStore.data.map { prefs ->
        val haptic = prefs[HAPTIC_KEY] ?: false
        val highContrast = prefs[HIGH_CONTRAST_KEY] ?: false
        val skinName = prefs[SKIN_KEY] ?: SkinType.CLASSIC.name
        val skin = try { SkinType.valueOf(skinName) } catch (e: Exception) { SkinType.CLASSIC }
        Triple(haptic, skin, highContrast)
    }

    fun getHighScore(): Flow<Long> = context.dataStore.data.map { it[HIGH_SCORE_KEY] ?: 0L }

    fun getSavedGame(): Flow<Triple<Array<Array<Cell>>?, Long, List<Piece>?>> = context.dataStore.data.map { prefs ->
        val boardStr = prefs[BOARD_KEY]
        val score = prefs[SCORE_KEY] ?: 0L
        val piecesStr = prefs[PIECES_KEY]
        
        Triple(
            boardStr?.let { deserializeBoard(it) },
            score,
            piecesStr?.let { deserializePieces(it) }
        )
    }

    private fun serializeBoard(board: Array<Array<Cell>>): String {
        return board.flatten().joinToString(",") { 
            if (it.isOccupied) it.colorIndex.toString() else "-1" 
        }
    }

    private fun deserializeBoard(str: String): Array<Array<Cell>>? {
        try {
            val parts = str.split(",")
            val size = GameConstants.BOARD_SIZE
            if (parts.size != size * size) return null
            
            val board = Array(size) { Array(size) { Cell() } }
            var occupiedCount = 0
            
            parts.forEachIndexed { index, s ->
                val r = index / size
                val c = index % size
                val idx = s.toIntOrNull() ?: -1
                if (idx in 0..10) {
                    board[r][c] = Cell(isOccupied = true, colorIndex = idx)
                    occupiedCount++
                }
            }
            
            // Si el tablero guardado está 100% lleno (64 celdas), no es válido -> retornar null para iniciar partida limpia
            if (occupiedCount >= size * size) return null
            
            return board
        } catch (e: Exception) {
            return null
        }
    }

    private fun serializePieces(pieces: List<Piece>): String {
        return pieces.joinToString("|") { piece ->
            val shapeStr = piece.shape.joinToString(";") { "${it.x},${it.y}" }
            "${piece.id}:${shapeStr}:${piece.colorIndex}:${piece.isUsed}"
        }
    }

    private fun deserializePieces(str: String): List<Piece>? {
        try {
            if (str.isEmpty()) return null
            val pieces = str.split("|").map { pStr ->
                val parts = pStr.split(":")
                val id = parts[0].toInt()
                val shape = parts[1].split(";").map {
                    val coords = it.split(",")
                    Offset(coords[0].toInt(), coords[1].toInt())
                }
                val colorIdx = parts[2].toInt()
                val isUsed = parts[3].toBoolean()
                if (shape.isEmpty() || colorIdx < 0) return null
                Piece(id, shape, colorIdx, isUsed)
            }
            if (pieces.size != 3) return null
            return pieces
        } catch (e: Exception) {
            return null
        }
    }
}
