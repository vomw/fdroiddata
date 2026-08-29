package com.blockfusion.logic

import androidx.compose.ui.graphics.Color

enum class SkinType(
    val displayName: String,
    val backgroundColor: Color,
    val boardColor: Color,
    val cellEmptyColor: Color,
    val pieceColors: List<Color>,
    val accentColor: Color
) {
    CLASSIC(
        displayName = "Classic",
        backgroundColor = Color(0xFF0B132B),
        boardColor = Color(0xFF14213D),
        cellEmptyColor = Color(0xFF1F3154),
        pieceColors = listOf(
            Color(0xFF42A5F5), Color(0xFF66BB6A), Color(0xFFFFA726),
            Color(0xFFAB47BC), Color(0xFFEF5350), Color(0xFFFFEE58)
        ),
        accentColor = Color(0xFF42A5F5)
    ),
    SILVER(
        displayName = "Silver",
        backgroundColor = Color(0xFF101216),
        boardColor = Color(0xFF1A1D24),
        cellEmptyColor = Color(0xFF252A34),
        pieceColors = listOf(
            Color(0xFF80DEEA), Color(0xFFA5D6A7), Color(0xFFFFAB91),
            Color(0xFFB0BEC5), Color(0xFF90A4AE), Color(0xFFECEFF1)
        ),
        accentColor = Color(0xFFB0BEC5)
    ),
    FOREST(
        displayName = "Deep Forest",
        backgroundColor = Color(0xFF0B1A10),
        boardColor = Color(0xFF142B1A),
        cellEmptyColor = Color(0xFF1B3B24),
        pieceColors = listOf(
            Color(0xFF4DB6AC), Color(0xFF4CAF50), Color(0xFFFFB74D),
            Color(0xFF8BC34A), Color(0xFFCDDC39), Color(0xFFFFC107)
        ),
        accentColor = Color(0xFF4CAF50)
    ),
    OCEAN(
        displayName = "Abyss",
        backgroundColor = Color(0xFF011627),
        boardColor = Color(0xFF011C2C),
        cellEmptyColor = Color(0xFF012A3D),
        pieceColors = listOf(
            Color(0xFF20A4F3), Color(0xFF2EC4B6), Color(0xFFFF9F1C),
            Color(0xFF00E5FF), Color(0xFFE71D36), Color(0xFFFDFFFC)
        ),
        accentColor = Color(0xFF2EC4B6)
    ),
    AMBER(
        displayName = "Amber",
        backgroundColor = Color(0xFF14120B),
        boardColor = Color(0xFF221E12),
        cellEmptyColor = Color(0xFF302B1B),
        pieceColors = listOf(
            Color(0xFF4FC3F7), Color(0xFF81C784), Color(0xFFFF9800),
            Color(0xFFFF7043), Color(0xFFFFD54F), Color(0xFFFFEE58)
        ),
        accentColor = Color(0xFFFFD54F)
    ),
    VOLCANO(
        displayName = "Magma",
        backgroundColor = Color(0xFF1A0F0F),
        boardColor = Color(0xFF2B1616),
        cellEmptyColor = Color(0xFF3D1B1B),
        pieceColors = listOf(
            Color(0xFFFFAB40), Color(0xFFFFC107), Color(0xFFFF9800),
            Color(0xFFFF7043), Color(0xFFD50000), Color(0xFFFF5722)
        ),
        accentColor = Color(0xFFFF5722)
    ),
    PASTEL(
        displayName = "Pastel",
        backgroundColor = Color(0xFF1A0A10),
        boardColor = Color(0xFF251118),
        cellEmptyColor = Color(0xFF351B25),
        pieceColors = listOf(
            Color(0xFFF8BBD0), Color(0xFFE1BEE7), Color(0xFFB3E5FC),
            Color(0xFFC8E6C9), Color(0xFFFFF9C4), Color(0xFFFFE0B2)
        ),
        accentColor = Color(0xFFF8BBD0)
    )
}
