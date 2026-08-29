package com.blockfusion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blockfusion.logic.SkinType

@Composable
fun DocumentationUI(
    currentSkin: SkinType,
    highContrastEnabled: Boolean = false,
    onClose: () -> Unit,
    onTriggerHaptic: () -> Unit = {}
) {
    val isLightAccent = currentSkin == SkinType.SILVER || currentSkin == SkinType.AMBER || currentSkin == SkinType.OCEAN || currentSkin == SkinType.PASTEL
    val closeTextColor = if (isLightAccent) Color.Black else Color.White

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val scrollState = rememberScrollState()
        
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .width(320.dp)
                .heightIn(max = 500.dp)
                .then(
                    if (highContrastEnabled) Modifier.background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp)).padding(2.dp)
                    else Modifier
                )
                .background(currentSkin.boardColor, RoundedCornerShape(24.dp))
                .padding(24.dp)
                .clickable(enabled = false) {},
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DOCUMENTATION",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Privacy Policy",
                    color = currentSkin.accentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This application does not collect or share any personal data. Your progress and settings are stored locally on your device.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Open Source Licenses",
                    color = currentSkin.accentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This app is built with Jetpack Compose and other open source libraries under the Apache License 2.0 and MIT License.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Android Jetpack\n• Kotlin Coroutines\n• Material Design 3",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Disclaimer",
                    color = currentSkin.accentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "The application is provided 'as is', without warranties of any kind. The developer will not be liable for any damages or claims arising from the use of this software.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onTriggerHaptic()
                    onClose()
                },
                modifier = Modifier.fillMaxWidth().height(48.dp)
                    .then(
                        if (highContrastEnabled) Modifier.background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp)).padding(2.dp)
                        else Modifier
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = currentSkin.accentColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "CLOSE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = closeTextColor
                )
            }
        }
    }
}

@Composable
fun AboutUI(
    currentSkin: SkinType,
    highContrastEnabled: Boolean = false,
    onClose: () -> Unit,
    onSendFeedback: () -> Unit = {},
    onOpenGitHub: () -> Unit = {},
    onTriggerHaptic: () -> Unit = {}
) {
    val isLightAccent = currentSkin == SkinType.SILVER || currentSkin == SkinType.AMBER || currentSkin == SkinType.OCEAN || currentSkin == SkinType.PASTEL
    val closeTextColor = if (isLightAccent) Color.Black else Color.White

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .width(320.dp)
                .heightIn(max = 500.dp)
                .then(
                    if (highContrastEnabled) Modifier.background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp)).padding(2.dp)
                    else Modifier
                )
                .background(currentSkin.boardColor, RoundedCornerShape(24.dp))
                .padding(24.dp)
                .clickable(enabled = false) {},
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                LogoUI(skin = currentSkin)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "BLOCK FUSION",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
                
                Text(
                    text = "Version 1.0",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Developed by",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                
                Text(
                    text = "Víctor Igor Rubio Domínguez",
                    color = currentSkin.accentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.1f))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "GitHub",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onTriggerHaptic()
                        onOpenGitHub()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {
                        onTriggerHaptic()
                        onSendFeedback()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                        .then(
                            if (highContrastEnabled) Modifier.background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp)).padding(2.dp)
                            else Modifier
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "SUPPORT / SEND FEEDBACK",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        onTriggerHaptic()
                        onClose()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                        .then(
                            if (highContrastEnabled) Modifier.background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp)).padding(2.dp)
                            else Modifier
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = currentSkin.accentColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "CLOSE",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = closeTextColor
                    )
                }
            }
        }
    }
}
