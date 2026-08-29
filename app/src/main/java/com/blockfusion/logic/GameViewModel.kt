package com.blockfusion.logic

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class GameState(
    val board: Array<Array<Cell>> = Array(GameConstants.BOARD_SIZE) { Array(GameConstants.BOARD_SIZE) { Cell() } },
    val availablePieces: List<Piece> = emptyList(),
    val score: Long = 0L,
    val highScore: Long = 0L,
    val highScoreAtStart: Long = 0L,
    val isGameOver: Boolean = false,
    val isEnding: Boolean = false,
    val resultHighScoreAtStart: Long = 0L,
    val resultScore: Long = 0L,
    val isResettingWithRecord: Boolean = false,
    val isNewRecordCelebration: Boolean = false,
    val clearingRows: List<Int> = emptyList(),
    val clearingCols: List<Int> = emptyList(),
    val hapticEnabled: Boolean = false,
    val highContrastEnabled: Boolean = false,
    val currentSkin: SkinType = SkinType.CLASSIC,
    val isMenuOpen: Boolean = false,
    val isDocumentationOpen: Boolean = false,
    val isAboutOpen: Boolean = false,
    val isFeedbackErrorOpen: Boolean = false,
    val isUrlErrorOpen: Boolean = false
)

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val pieceGenerator = PieceGenerator()
    private val persistenceManager = PersistenceManager(application)
    private var hasCelebratedThisRun = false
    private var highScoreAtStart = 0L
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    init {
        loadGame()
    }

    private fun loadGame() {
        viewModelScope.launch {
            val highScore = persistenceManager.getHighScore().first()
            val (savedBoard, savedScore, savedPieces) = persistenceManager.getSavedGame().first()
            val (haptic, skin, highContrast) = persistenceManager.getSettings().first()
            
            if (savedBoard != null && savedPieces != null && savedPieces.isNotEmpty()) {
                val available = savedPieces.filter { !it.isUsed }
                val canMove = available.any { p -> canPieceFitAnywhere(savedBoard, p.shape) }
                if (canMove) {
                    highScoreAtStart = highScore
                    _gameState.value = GameState(
                        board = savedBoard,
                        availablePieces = savedPieces,
                        score = savedScore,
                        highScore = highScore,
                        highScoreAtStart = highScore,
                        hapticEnabled = haptic,
                        highContrastEnabled = highContrast,
                        currentSkin = skin
                    )
                } else {
                    _gameState.update { it.copy(
                        highScore = highScore,
                        highScoreAtStart = highScore,
                        hapticEnabled = haptic,
                        highContrastEnabled = highContrast,
                        currentSkin = skin
                    ) }
                    highScoreAtStart = highScore
                    startNewGame()
                }
            } else {
                _gameState.update { it.copy(
                    highScore = highScore,
                    highScoreAtStart = highScore,
                    hapticEnabled = haptic,
                    highContrastEnabled = highContrast,
                    currentSkin = skin
                ) }
                highScoreAtStart = highScore
                startNewGame()
            }
            
            persistenceManager.getHighScore().collect { hs ->
                _gameState.update { it.copy(highScore = hs) }
            }
        }
    }

    fun toggleMenu() {
        _gameState.update { it.copy(
            isMenuOpen = !it.isMenuOpen,
            isDocumentationOpen = false,
            isAboutOpen = false
        ) }
    }

    fun toggleDocumentation() {
        _gameState.update { it.copy(isDocumentationOpen = !it.isDocumentationOpen) }
    }

    fun toggleAbout() {
        _gameState.update { it.copy(isAboutOpen = !it.isAboutOpen) }
    }

    fun toggleFeedbackError() {
        _gameState.update { it.copy(isFeedbackErrorOpen = !it.isFeedbackErrorOpen) }
    }

    fun setFeedbackError(open: Boolean) {
        _gameState.update { it.copy(isFeedbackErrorOpen = open) }
    }

    fun toggleUrlError() {
        _gameState.update { it.copy(isUrlErrorOpen = !it.isUrlErrorOpen) }
    }

    fun setUrlError(open: Boolean) {
        _gameState.update { it.copy(isUrlErrorOpen = open) }
    }

    fun toggleHaptic() {
        val currentlyEnabled = _gameState.value.hapticEnabled
        val newVal = !currentlyEnabled
        _gameState.update { it.copy(hapticEnabled = newVal) }
        triggerHaptic(isStrong = false, force = true)
        saveSettings()
    }

    fun toggleHighContrast() {
        val currentlyEnabled = _gameState.value.highContrastEnabled
        val newVal = !currentlyEnabled
        _gameState.update { it.copy(highContrastEnabled = newVal) }
        saveSettings()
    }

    fun setSkin(skin: SkinType) {
        _gameState.update { it.copy(currentSkin = skin) }
        saveSettings()
    }

    private fun saveSettings() {
        viewModelScope.launch {
            val state = _gameState.value
            persistenceManager.saveSettings(state.hapticEnabled, state.currentSkin, state.highContrastEnabled)
        }
    }

    fun startNewGame(force: Boolean = false) {
        if (!force && _gameState.value.score > highScoreAtStart && !_gameState.value.isGameOver) {
            _gameState.update { it.copy(
                isResettingWithRecord = true,
                resultScore = it.score,
                resultHighScoreAtStart = it.highScoreAtStart,
                isMenuOpen = false,
                isNewRecordCelebration = false
            ) }
            viewModelScope.launch { persistenceManager.clearSavedGame() }
            return
        }
        
        highScoreAtStart = _gameState.value.highScore
        hasCelebratedThisRun = false
        val initialBoard = Array(GameConstants.BOARD_SIZE) { Array(GameConstants.BOARD_SIZE) { Cell() } }
        _gameState.update { it.copy(
            board = initialBoard,
            availablePieces = pieceGenerator.generatePieces(initialBoard, it.currentSkin.pieceColors.size, 0L),
            score = 0L,
            isGameOver = false,
            isResettingWithRecord = false,
            highScoreAtStart = highScoreAtStart,
            clearingRows = emptyList(),
            clearingCols = emptyList(),
            isMenuOpen = false,
            isEnding = false
        ) }
        saveGame()
    }

    fun confirmReset() {
        startNewGame(force = true)
    }

    fun tryPlacePiece(piece: Piece, row: Int, col: Int): Boolean {
        if (_gameState.value.clearingRows.isNotEmpty() || _gameState.value.clearingCols.isNotEmpty()) return false

        if (canPlacePiece(piece, row, col)) {
            val newBoard = copyBoard(_gameState.value.board)
            piece.shape.forEach { offset ->
                newBoard[row + offset.y][col + offset.x] = Cell(isOccupied = true, colorIndex = piece.colorIndex)
            }

            val updatedPieces = _gameState.value.availablePieces.map {
                if (it.id == piece.id) it.copy(isUsed = true) else it
            }

            val (rowsToClear, colsToClear) = detectLines(newBoard)
            
            val size = GameConstants.BOARD_SIZE
            val cellsToClear = mutableSetOf<Pair<Int, Int>>()
            rowsToClear.forEach { r -> (0 until size).forEach { c -> cellsToClear.add(r to c) } }
            colsToClear.forEach { c -> (0 until size).forEach { r -> cellsToClear.add(r to c) } }
            val clearedCellsCount = cellsToClear.size

            val willBeEmpty = newBoard.indices.all { r ->
                newBoard[r].indices.all { c ->
                    (newBoard[r][c].isOccupied && (r in rowsToClear || c in colsToClear)) || !newBoard[r][c].isOccupied
                }
            } && clearedCellsCount > 0

            val points = calculateScore(rowsToClear.size + colsToClear.size, clearedCellsCount, piece.shape.size, willBeEmpty)

            if (rowsToClear.isNotEmpty() || colsToClear.isNotEmpty()) {
                val linesCount = rowsToClear.size + colsToClear.size
                triggerHaptic(isStrong = true)

                viewModelScope.launch {
                    _gameState.update { it.copy(
                        board = newBoard,
                        availablePieces = updatedPieces,
                        score = it.score + points,
                        clearingRows = rowsToClear,
                        clearingCols = colsToClear
                    ) }
                    
                    kotlinx.coroutines.delay(300) 
                    
                    triggerHaptic(isStrong = linesCount >= 3 || willBeEmpty)

                    _gameState.update { current ->
                        val finalBoard = performClear(current.board, rowsToClear, colsToClear)
                        val nextPieces = if (updatedPieces.all { p -> p.isUsed }) {
                            pieceGenerator.generatePieces(finalBoard, current.currentSkin.pieceColors.size, current.score)
                        } else {
                            updatedPieces
                        }
                        
                        current.copy(
                            board = finalBoard,
                            clearingRows = emptyList(),
                            clearingCols = emptyList(),
                            availablePieces = nextPieces
                        )
                    }
                    finalizeTurn()
                }
            } else {
                triggerHaptic(isStrong = false)
                _gameState.update { current ->
                    val nextPieces = if (updatedPieces.all { p -> p.isUsed }) {
                        pieceGenerator.generatePieces(newBoard, current.currentSkin.pieceColors.size, current.score + points)
                    } else {
                        updatedPieces
                    }
                    current.copy(
                        board = newBoard,
                        availablePieces = nextPieces,
                        score = current.score + points
                    )
                }
                finalizeTurn()
            }
            return true
        }
        return false
    }

    fun triggerHaptic(isStrong: Boolean = false, force: Boolean = false) {
        if (!force && !_gameState.value.hapticEnabled) return
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val duration = if (isStrong) 120L else 70L
                val amplitude = if (isStrong) 255 else 180
                val effect = VibrationEffect.createOneShot(duration, amplitude)
                vibrator.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(if (isStrong) 120L else 70L)
            }
        } catch (_: Exception) {}
    }

    private fun finalizeTurn() {
        val state = _gameState.value
        if (state.score > state.highScore) {
            viewModelScope.launch { persistenceManager.saveHighScore(state.score) }
            
            if (state.score > highScoreAtStart && highScoreAtStart > 0 && !hasCelebratedThisRun) {
                hasCelebratedThisRun = true
                viewModelScope.launch {
                    _gameState.update { it.copy(isNewRecordCelebration = true) }
                    kotlinx.coroutines.delay(1500) // Esperamos a que la animación de "New Record" termine
                    _gameState.update { it.copy(isNewRecordCelebration = false) }
                    checkGameOver() // Comprobamos si ha perdido DESPUÉS de celebrar el récord
                }
                saveGame()
                return // Salimos para no ejecutar checkGameOver() prematuramente
            }
        }
        saveGame()
        checkGameOver()
    }

    fun canPlaceAt(shape: List<Offset>, row: Int, col: Int): Boolean {
        return canPlaceAt(_gameState.value.board, shape, row, col)
    }

    private fun canPlaceAt(board: Array<Array<Cell>>, shape: List<Offset>, row: Int, col: Int): Boolean {
        val size = GameConstants.BOARD_SIZE
        for (offset in shape) {
            val r = row + offset.y
            val c = col + offset.x
            if (r !in 0 until size || c !in 0 until size || board[r][c].isOccupied) return false
        }
        return true
    }

    private fun canPlacePiece(piece: Piece, row: Int, col: Int): Boolean {
        return canPlaceAt(piece.shape, row, col)
    }

    private fun detectLines(board: Array<Array<Cell>>): Pair<List<Int>, List<Int>> {
        val size = GameConstants.BOARD_SIZE
        val rows = (0 until size).filter { r -> (0 until size).all { c -> board[r][c].isOccupied } }
        val cols = (0 until size).filter { c -> (0 until size).all { r -> board[r][c].isOccupied } }
        return Pair(rows, cols)
    }

    private fun performClear(board: Array<Array<Cell>>, rows: List<Int>, cols: List<Int>): Array<Array<Cell>> {
        val newBoard = copyBoard(board)
        val size = GameConstants.BOARD_SIZE
        rows.forEach { r -> for (c in 0 until size) newBoard[r][c] = Cell() }
        cols.forEach { c -> for (r in 0 until size) newBoard[r][c] = Cell() }
        return newBoard
    }

    private fun calculateScore(linesCount: Int, cellsCleared: Int, pieceSize: Int, isBoardEmpty: Boolean): Long {
        return when {
            isBoardEmpty -> (pieceSize * 3L) + (cellsCleared * 2L)
            linesCount > 0 -> (pieceSize * 2L) + (cellsCleared * 2L)
            else -> pieceSize.toLong()
        }
    }

    private fun checkGameOver() {
        val state = _gameState.value
        val available = state.availablePieces.filter { !it.isUsed }
        
        if (available.isEmpty()) return 

        val canMove = available.any { piece ->
            canPieceFitAnywhere(state.board, piece.shape)
        }

        if (!canMove) {
            viewModelScope.launch {
                persistenceManager.clearSavedGame() // Clear immediately so app restart starts clean
                _gameState.update { it.copy(
                    isEnding = true,
                    isMenuOpen = false
                ) }
                
                // Wait for the requested animation and delay (3 seconds total)
                kotlinx.coroutines.delay(3000)
                
                _gameState.update { it.copy(
                    isGameOver = true,
                    resultScore = it.score,
                    resultHighScoreAtStart = it.highScoreAtStart
                ) }
            }
        }
    }

    private fun canPieceFitAnywhere(board: Array<Array<Cell>>, shape: List<Offset>): Boolean {
        val size = GameConstants.BOARD_SIZE
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (canPlaceAt(board, shape, r, c)) return true
            }
        }
        return false
    }

    private fun copyBoard(board: Array<Array<Cell>>): Array<Array<Cell>> {
        return Array(GameConstants.BOARD_SIZE) { r ->
            Array(GameConstants.BOARD_SIZE) { c ->
                board[r][c]
            }
        }
    }

    private fun saveGame() {
        viewModelScope.launch {
            val state = _gameState.value
            persistenceManager.saveGame(state.board, state.score, state.availablePieces)
        }
    }
}
