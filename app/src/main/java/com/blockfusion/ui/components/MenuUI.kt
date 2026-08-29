package com.blockfusion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blockfusion.logic.SkinType

@Composable
fun MenuUI(
    hapticEnabled: Boolean,
    highContrastEnabled: Boolean = false,
    currentSkin: SkinType,
    onResume: () -> Unit,
    onToggleHaptic: () -> Unit,
    onToggleHighContrast: () -> Unit = {},
    onToggleDocumentation: () -> Unit,
    onToggleAbout: () -> Unit,
    onSetSkin: (SkinType) -> Unit,
    onTriggerHaptic: () -> Unit = {}
) {
    val isLightAccent = currentSkin == SkinType.SILVER || currentSkin == SkinType.AMBER || currentSkin == SkinType.OCEAN || currentSkin == SkinType.PASTEL
    val resumeTextColor = if (isLightAccent) Color.Black else Color.White

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val scrollState = rememberScrollState()
        
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .width(320.dp)
                .heightIn(max = 500.dp) // Limit height for small screens
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
                text = "PAUSE",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón Principal
                Button(
                    onClick = {
                        onTriggerHaptic()
                        onResume()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                        .then(
                            if (highContrastEnabled) Modifier.background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp)).padding(2.dp)
                            else Modifier
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = currentSkin.accentColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "RESUME",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = resumeTextColor
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Documentation Button
                Button(
                    onClick = {
                        onTriggerHaptic()
                        onToggleDocumentation()
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
                        text = "DOCUMENTATION",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // About Button
                Button(
                    onClick = {
                        onTriggerHaptic()
                        onToggleAbout()
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
                        text = "ABOUT",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Settings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("High contrast", color = Color.White, fontWeight = FontWeight.Medium)
                    Switch(
                        checked = highContrastEnabled,
                        onCheckedChange = {
                            onTriggerHaptic()
                            onToggleHighContrast()
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = if (isLightAccent) Color.Black else Color.White,
                            checkedTrackColor = currentSkin.accentColor,
                            uncheckedThumbColor = Color(0xFFA0A0A0),
                            uncheckedTrackColor = Color.White.copy(alpha = 0.1f),
                            checkedBorderColor = Color.Transparent,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Haptic UI", color = Color.White, fontWeight = FontWeight.Medium)
                    Switch(
                        checked = hapticEnabled,
                        onCheckedChange = {
                            onTriggerHaptic()
                            onToggleHaptic()
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = if (isLightAccent) Color.Black else Color.White,
                            checkedTrackColor = currentSkin.accentColor,
                            uncheckedThumbColor = Color(0xFFA0A0A0),
                            uncheckedTrackColor = Color.White.copy(alpha = 0.1f),
                            checkedBorderColor = Color.Transparent,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "SKIN",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(SkinType.entries) { skin ->
                        val isSelected = skin == currentSkin
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                onTriggerHaptic()
                                onSetSkin(skin)
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(if (highContrastEnabled) Color.White.copy(alpha = 0.5f) else skin.backgroundColor)
                                    .padding(if (highContrastEnabled) 2.dp else 4.dp)
                                    .then(
                                        if (isSelected) Modifier.background(skin.accentColor, CircleShape).padding(2.dp)
                                        else Modifier
                                    )
                                    .clip(CircleShape)
                                    .background(skin.accentColor)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = skin.displayName,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}
