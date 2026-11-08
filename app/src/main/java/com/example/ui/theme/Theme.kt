package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = EmeraldLight,
  onPrimary = DarkBackground,
  primaryContainer = EmeraldMuted,
  onPrimaryContainer = EmeraldPastel,
  secondary = GoldLuminous,
  onSecondary = DarkBackground,
  secondaryContainer = GoldDark,
  onSecondaryContainer = GoldPastel,
  tertiary = EmeraldPastel,
  background = DarkBackground,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkTextSecondary,
  outline = Color(0xFF1E5244)
)

private val LightColorScheme = lightColorScheme(
  primary = EmeraldPrimary,
  onPrimary = Color.White,
  primaryContainer = EmeraldPastel,
  onPrimaryContainer = EmeraldDark,
  secondary = GoldPrimary,
  onSecondary = Color.White,
  secondaryContainer = GoldPastel,
  onSecondaryContainer = GoldDark,
  tertiary = EmeraldLight,
  background = CreamIvory,
  onBackground = TextCharcoal,
  surface = SurfaceWhite,
  onSurface = TextCharcoal,
  surfaceVariant = Color(0xFFF0FDF4),
  onSurfaceVariant = TextMuted,
  outline = BorderSubtle
)

@Composable
fun NiyaTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
