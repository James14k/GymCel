package cl.duocuc.gymcel.presentacion.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = Color.White,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,

    secondary = SecondaryColor,
    onSecondary = Color.White,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,

    tertiary = TertiaryColor,
    onTertiary = Color.Black,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,

    background = BackgroundColor,
    onBackground = TextPrimary,

    surface = SurfaceColor,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,

    error = ErrorColor,
    errorContainer = ErrorContainer,
    onError = Color.White,
    onErrorContainer = OnErrorContainer,

    outline = Outline,
    outlineVariant = OutlineVariant
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF004916),
    onPrimaryContainer = PrimaryContainer,

    secondary = SecondaryDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF00491A),
    onSecondaryContainer = SecondaryContainer,

    tertiary = TertiaryDark,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF4A3700),
    onTertiaryContainer = TertiaryContainer,

    background = Color(0xFF121212),
    onBackground = Color.White,

    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF3A3D3A),
    onSurfaceVariant = Color(0xFFC5C7C5),

    error = ErrorColor,
    errorContainer = Color(0xFF8C1D18),
    onError = Color.White,
    onErrorContainer = ErrorContainer,

    outline = Outline,
    outlineVariant = Color(0xFF3A3D3A)
)

@Composable
fun GymTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
