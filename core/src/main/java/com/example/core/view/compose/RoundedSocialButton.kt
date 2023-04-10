package com.example.core.view.compose

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun RoundedSocialButton(
    modifier: Modifier = Modifier,
    icon: Painter,
    onClick: () -> Unit,
    contentDescription: String,
) {
    Button(
        modifier = modifier.size(64.dp),
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
        ),
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = Color.Unspecified,
            modifier = Modifier.size(60.dp)
        )
    }

}