package com.example.oriencoop_score.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object AppTheme {
    object spacings {
        val ShadowElevation = 2.dp
        val HeaderHeight = 80.dp
        val IconSize = 60.dp
        val LogoSize = 120.dp
        val Spacing = 25.dp
        val SmallSpacing = 5.dp
        val MediumSpacing = 40.dp
        val BoxHeight = 65.dp
        val BoxWidth = 300.dp
    }
    // Colores
    object colors {
        val azul = Color(0xFF006FB6)
        val verde = Color(0xFF067211)
        val verdeClaro = Color(0xFF60cc5a)

        val rojo = Color(0xFFbd0000)
        val gris = Color(0xFFcccccc)
        val amarillo = Color(0xFFf49600)

        val blanco = Color(0xFFFFFFFF)
        val negro = Color(0xFF000000)

        val primary = Color(0xFF0069C0)
        val onPrimary = Color(0xFFFFFFFF)
        val background = Color(0xFFE8F5F6)
        val onSurface = Color(0xFF212121)
        val surface = Color(0xFFFFFFFF)
        val outline = Color(0xFFCCCCCC)
    }
    // Typography
    object typography {
        val detalles = TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 9.sp
        )
        val titulos = TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 24.sp
        )
        val normal = TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 15.sp
        )

        val small = TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 11.sp
        )
    }


}