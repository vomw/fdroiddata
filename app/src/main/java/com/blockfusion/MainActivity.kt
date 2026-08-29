package com.blockfusion

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import com.blockfusion.logic.GameViewModel
import com.blockfusion.ui.components.*
import com.blockfusion.ui.theme.BlockFusionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge(
            statusBarStyle = androidx.activity.SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = androidx.activity.SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            val viewModel: GameViewModel = viewModel()
            
            BlockFusionTheme(darkTheme = true) {
                val view = LocalView.current
                if (!view.isInEditMode) {
                    val window = (view.context as android.app.Activity).window
                    val insetsController = WindowCompat.getInsetsController(window, view)
                    window.statusBarColor = android.graphics.Color.TRANSPARENT
                    window.navigationBarColor = android.graphics.Color.TRANSPARENT
                    insetsController.isAppearanceLightStatusBars = false
                    insetsController.isAppearanceLightNavigationBars = false
                }
                BlockFusionGame(viewModel)
            }
        }
    }
}

@Composable
fun BlockFusionGame(viewModel: GameViewModel) {
    val state by viewModel.gameState.collectAsState()
    val dragDropState = remember { DragDropState() }
    
    val isOverlayVisible = state.isMenuOpen || state.isGameOver || state.isResettingWithRecord || state.isNewRecordCelebration || state.isEnding || state.isDocumentationOpen || state.isAboutOpen || state.isFeedbackErrorOpen || state.isUrlErrorOpen

    // Manejo del gesto de atrás
    BackHandler(enabled = state.isMenuOpen || state.isGameOver || state.isResettingWithRecord || state.isDocumentationOpen || state.isAboutOpen || state.isFeedbackErrorOpen || state.isUrlErrorOpen) {
        when {
            state.isUrlErrorOpen -> viewModel.setUrlError(false)
            state.isFeedbackErrorOpen -> viewModel.setFeedbackError(false)
            state.isAboutOpen -> viewModel.toggleAbout()
            state.isDocumentationOpen -> viewModel.toggleDocumentation()
            state.isMenuOpen -> viewModel.toggleMenu()
            state.isGameOver -> viewModel.startNewGame()
            state.isResettingWithRecord -> viewModel.confirmReset()
        }
    }

    val animatedScore by animateFloatAsState(
        targetValue = state.score.toFloat(),
        animationSpec = tween(durationMillis = 500),
        label = "animatedScore"
    )
    val displayedScore = animatedScore.toLong()

    CompositionLocalProvider(LocalDragDropState provides dragDropState) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = state.currentSkin.backgroundColor,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val isLandscape = maxWidth > maxHeight
                val isSmallHeight = maxHeight < 700.dp
                
                if (isLandscape) {
                    // Landscape Layout
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .displayCutoutPadding(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Board
                        Box(
                            modifier = Modifier
                                .weight(1.2f)
                                .fillMaxHeight()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BoardUI(
                                board = state.board,
                                clearingRows = state.clearingRows,
                                clearingCols = state.clearingCols,
                                skin = state.currentSkin,
                                highContrastMode = state.highContrastEnabled,
                                isEnding = state.isEnding,
                                isGameOver = state.isGameOver,
                                onCanPlace = { shape, r, c -> viewModel.canPlaceAt(shape, r, c) },
                                modifier = Modifier.fillMaxHeight().aspectRatio(1f)
                            )
                        }

                        // Right: Controls and Pieces
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Header condensed
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Reset Button
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = if (state.score > 0) 0.1f else 0.05f))
                                        .clickable(enabled = state.score > 0 && !isOverlayVisible) {
                                            viewModel.triggerHaptic()
                                            viewModel.startNewGame()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    androidx.compose.foundation.Canvas(modifier = Modifier.size(16.dp)) {
                                        val contentAlpha = if (state.score > 0) 1f else 0.3f
                                        drawArc(
                                            color = Color.White.copy(alpha = contentAlpha),
                                            startAngle = 45f,
                                            sweepAngle = 280f,
                                            useCenter = false,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(
                                                width = 2.dp.toPx(),
                                                cap = androidx.compose.ui.graphics.StrokeCap.Round
                                            )
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    ScoreText(
                                        score = displayedScore,
                                        color = Color.White,
                                        fontSize = 42.sp,
                                        fontWeight = FontWeight.Light
                                    )
                                }

                                // Menu Button
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.1f))
                                        .clickable(enabled = !isOverlayVisible) {
                                            viewModel.triggerHaptic()
                                            viewModel.toggleMenu()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                        repeat(3) {
                                            Box(modifier = Modifier.size(16.dp, 2.dp).background(Color.White))
                                        }
                                    }
                                }
                            }

                            // Pieces stacked vertically in landscape
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                state.availablePieces.forEach { piece ->
                                    key(piece.id) {
                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            PieceUI(
                                                piece = piece,
                                                skin = state.currentSkin,
                                                highContrastMode = state.highContrastEnabled,
                                                enabled = !isOverlayVisible,
                                                isEnding = state.isEnding,
                                                isGameOver = state.isGameOver,
                                                onPlace = { r, c -> viewModel.tryPlacePiece(piece, r, c) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Portrait Layout (Adaptive for split screen)
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Custom Menu Button and Header
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = if (isSmallHeight) 8.dp else 24.dp)) {
                            // Minimalist Reset/Restart button
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = if (state.score > 0) 0.1f else 0.05f))
                                    .then(
                                        if (state.highContrastEnabled) Modifier.background(Color.White.copy(alpha = if (state.score > 0) 0.2f else 0.1f), CircleShape).padding(2.dp)
                                        else Modifier
                                    )
                                    .clickable(enabled = state.score > 0 && !isOverlayVisible) {
                                        viewModel.triggerHaptic()
                                        viewModel.startNewGame()
                                    }
                                    .align(Alignment.TopStart),
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.foundation.Canvas(modifier = Modifier.size(20.dp)) {
                                    val contentAlpha = if (state.score > 0) 1f else 0.3f
                                    val strokeWidth = 2.dp.toPx()
                                    drawArc(
                                        color = Color.White.copy(alpha = contentAlpha),
                                        startAngle = 45f,
                                        sweepAngle = 280f,
                                        useCenter = false,
                                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                                            width = strokeWidth,
                                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                                        )
                                    )
                                    val path = androidx.compose.ui.graphics.Path().apply {
                                        moveTo(size.width * 0.70f, size.height * 0.02f)
                                        lineTo(size.width * 0.98f, size.height * 0.22f)
                                        lineTo(size.width * 0.62f, size.height * 0.32f)
                                        close()
                                    }
                                    drawPath(path = path, color = Color.White.copy(alpha = contentAlpha))
                                }
                            }

                            // Minimalist menu button
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.1f))
                                    .then(
                                        if (state.highContrastEnabled) Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape).padding(2.dp)
                                        else Modifier
                                    )
                                    .clickable(enabled = !isOverlayVisible) {
                                        viewModel.triggerHaptic()
                                        viewModel.toggleMenu()
                                    }
                                    .align(Alignment.TopEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    repeat(3) {
                                        Box(modifier = Modifier.size(20.dp, 2.dp).background(Color.White))
                                    }
                                }
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                if (!isSmallHeight) {
                                    LogoUI(skin = state.currentSkin)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                                
                                Text(
                                    text = "BEST: ${state.highScore}",
                                    color = Color(0xFFFFA726),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    maxLines = 1
                                )
                                
                                ScoreText(
                                    score = displayedScore,
                                    color = Color.White,
                                    fontSize = if (isSmallHeight) 48.sp else 64.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        }

                        // Board - Using weight to adapt to available height
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BoardUI(
                                board = state.board,
                                clearingRows = state.clearingRows,
                                clearingCols = state.clearingCols,
                                skin = state.currentSkin,
                                highContrastMode = state.highContrastEnabled,
                                isEnding = state.isEnding,
                                isGameOver = state.isGameOver,
                                onCanPlace = { shape, r, c -> viewModel.canPlaceAt(shape, r, c) },
                                modifier = Modifier.fillMaxHeight().aspectRatio(1f)
                            )
                        }

                        // Pieces Container - Adaptive height
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp, max = 220.dp)
                                .height(if (isSmallHeight) 140.dp else 200.dp)
                                .background(state.currentSkin.boardColor)
                                .navigationBarsPadding()
                                .padding(if (isSmallHeight) 8.dp else 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            state.availablePieces.forEach { piece ->
                                key(piece.id) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        PieceUI(
                                            piece = piece,
                                            skin = state.currentSkin,
                                            highContrastMode = state.highContrastEnabled,
                                            enabled = !isOverlayVisible,
                                            isEnding = state.isEnding,
                                            isGameOver = state.isGameOver,
                                            onPlace = { r, c ->
                                                viewModel.tryPlacePiece(piece, r, c)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                val isPopUpVisible = state.isMenuOpen || state.isGameOver || state.isResettingWithRecord || state.isNewRecordCelebration || state.isDocumentationOpen || state.isAboutOpen || state.isFeedbackErrorOpen || state.isUrlErrorOpen

                AnimatedVisibility(
                    visible = isPopUpVisible,
                    enter = fadeIn(tween(0)),
                    exit = fadeOut(tween(500))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.8f))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { }
                            )
                    )
                }

                AnimatedVisibility(
                    visible = state.isMenuOpen,
                    enter = fadeIn(tween(500)) + scaleIn(tween(500), initialScale = 0.8f),
                    exit = fadeOut(tween(500)) + scaleOut(tween(500), targetScale = 0.8f)
                ) {
                    MenuUI(
                        hapticEnabled = state.hapticEnabled,
                        highContrastEnabled = state.highContrastEnabled,
                        currentSkin = state.currentSkin,
                        onResume = { viewModel.toggleMenu() },
                        onToggleHaptic = { viewModel.toggleHaptic() },
                        onToggleHighContrast = { viewModel.toggleHighContrast() },
                        onToggleDocumentation = { viewModel.toggleDocumentation() },
                        onToggleAbout = { viewModel.toggleAbout() },
                        onSetSkin = { viewModel.setSkin(it) },
                        onTriggerHaptic = { viewModel.triggerHaptic() }
                    )
                }

                AnimatedVisibility(
                    visible = state.isDocumentationOpen,
                    enter = fadeIn(tween(500)) + scaleIn(tween(500), initialScale = 0.8f),
                    exit = fadeOut(tween(500)) + scaleOut(tween(500), targetScale = 0.8f)
                ) {
                    DocumentationUI(
                        currentSkin = state.currentSkin,
                        highContrastEnabled = state.highContrastEnabled,
                        onClose = { viewModel.toggleDocumentation() },
                        onTriggerHaptic = { viewModel.triggerHaptic() }
                    )
                }

                AnimatedVisibility(
                    visible = state.isAboutOpen,
                    enter = fadeIn(tween(500)) + scaleIn(tween(500), initialScale = 0.8f),
                    exit = fadeOut(tween(500)) + scaleOut(tween(500), targetScale = 0.8f)
                ) {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    AboutUI(
                        currentSkin = state.currentSkin,
                        highContrastEnabled = state.highContrastEnabled,
                        onClose = { viewModel.toggleAbout() },
                        onSendFeedback = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:victorigorrubiodominguez@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Support / Feedback: Block Fusion")
                            }
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                viewModel.setFeedbackError(true)
                            }
                        },
                        onOpenGitHub = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/vird04/Block-Fusion"))
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                viewModel.setUrlError(true)
                            }
                        },
                        onTriggerHaptic = { viewModel.triggerHaptic() }
                    )
                }

                AnimatedVisibility(
                    visible = state.isFeedbackErrorOpen,
                    enter = fadeIn(tween(500)) + scaleIn(tween(500), initialScale = 0.8f),
                    exit = fadeOut(tween(500)) + scaleOut(tween(500), targetScale = 0.8f)
                ) {
                    ErrorPopupUI(
                        title = "NO EMAIL APP FOUND",
                        message = "We couldn't find an email application on your device to send feedback. Please ensure you have an email app installed and configured.",
                        currentSkin = state.currentSkin,
                        highContrastEnabled = state.highContrastEnabled,
                        onClose = { viewModel.setFeedbackError(false) },
                        onTriggerHaptic = { viewModel.triggerHaptic() }
                    )
                }

                AnimatedVisibility(
                    visible = state.isUrlErrorOpen,
                    enter = fadeIn(tween(500)) + scaleIn(tween(500), initialScale = 0.8f),
                    exit = fadeOut(tween(500)) + scaleOut(tween(500), targetScale = 0.8f)
                ) {
                    ErrorPopupUI(
                        title = "NO APP FOUND",
                        message = "We couldn't find a web browser or the GitHub app on your device to open the link. Please ensure you have a browser or the GitHub app installed.",
                        currentSkin = state.currentSkin,
                        highContrastEnabled = state.highContrastEnabled,
                        onClose = { viewModel.setUrlError(false) },
                        onTriggerHaptic = { viewModel.triggerHaptic() }
                    )
                }

                AnimatedVisibility(
                    visible = state.isGameOver,
                    enter = fadeIn(tween(500)) + scaleIn(tween(500), initialScale = 0.8f),
                    exit = fadeOut(tween(500)) + scaleOut(tween(500), targetScale = 0.8f)
                ) {
                    GameOverUI(
                        score = state.resultScore,
                        highScore = state.highScore,
                        highScoreAtStart = state.resultHighScoreAtStart,
                        currentSkin = state.currentSkin,
                        highContrastMode = state.highContrastEnabled,
                        onRestart = { viewModel.startNewGame() },
                        onTriggerHaptic = { viewModel.triggerHaptic() }
                    )
                }

                AnimatedVisibility(
                    visible = state.isResettingWithRecord,
                    enter = fadeIn(tween(500)) + scaleIn(tween(500), initialScale = 0.8f),
                    exit = fadeOut(tween(500)) + scaleOut(tween(500), targetScale = 0.8f)
                ) {
                    ResetUI(
                        score = state.resultScore,
                        currentSkin = state.currentSkin,
                        highContrastMode = state.highContrastEnabled,
                        highScoreAtStart = state.resultHighScoreAtStart,
                        onConfirm = { viewModel.confirmReset() },
                        onTriggerHaptic = { viewModel.triggerHaptic() }
                    )
                }

                AnimatedVisibility(
                    visible = state.isNewRecordCelebration,
                    enter = fadeIn(tween(500)) + scaleIn(tween(500), initialScale = 0.8f),
                    exit = fadeOut(tween(500)) + scaleOut(tween(500), targetScale = 0.8f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        ) {
                            AutoResizableText(
                                text = "NEW RECORD BEATEN!",
                                color = Color(0xFFFFA726),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            AutoResizableText(
                                text = state.score.toString(),
                                color = Color.White,
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
