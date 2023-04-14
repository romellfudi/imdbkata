package com.example.core.view

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.core.R

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
val ThemeColors = lightColors(
    primary = Color.Black,
    primaryVariant = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)
val DarkThemeColors = darkColors(
    primary = Color.White,
    primaryVariant = Color.White,
    surface = Color.Black,
    onSurface = Color.White
)

@Composable
fun getTextFieldColors(isDarkMode: Boolean): TextFieldColors {
    return TextFieldDefaults.outlinedTextFieldColors(
        backgroundColor = if(isDarkMode) Color4 else Color5,
        cursorColor = if (isDarkMode) Color5 else Color4,
        disabledLabelColor = if (isDarkMode) Color5 else Color4
    )
}

val buttonNoElevation: ButtonElevation
    @Composable
    get() = ButtonDefaults.elevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        disabledElevation = 0.dp,
        hoveredElevation = 0.dp,
        focusedElevation = 0.dp
    )