package pe.edu.upeu.bibliomobil.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Claro=lightColorScheme(primary=Color(0xFF8A4B08),onPrimary=Color.White,primaryContainer=Color(0xFFFFDDB9),onPrimaryContainer=Color(0xFF2C1600),secondary=Color(0xFF6F5B40),onSecondary=Color.White,secondaryContainer=Color(0xFFFADEBC),onSecondaryContainer=Color(0xFF271905),tertiary=Color(0xFF52643F),onTertiary=Color.White,tertiaryContainer=Color(0xFFD5EABB),onTertiaryContainer=Color(0xFF111F04),error=Color(0xFFBA1A1A),onError=Color.White,errorContainer=Color(0xFFFFDAD6),onErrorContainer=Color(0xFF410002),background=Color(0xFFFFF8F4),onBackground=Color(0xFF211A14),surface=Color(0xFFFFF8F4),onSurface=Color(0xFF211A14),surfaceVariant=Color(0xFFF2DFD1),onSurfaceVariant=Color(0xFF51443A),outline=Color(0xFF837469),outlineVariant=Color(0xFFD5C3B6),scrim=Color.Black,inverseSurface=Color(0xFF372F29),inverseOnSurface=Color(0xFFFDEEE5),inversePrimary=Color(0xFFFFB870),surfaceDim=Color(0xFFE5D8D0),surfaceBright=Color(0xFFFFF8F4),surfaceContainerLowest=Color.White,surfaceContainerLow=Color(0xFFFFF1E9),surfaceContainer=Color(0xFFF9ECE3),surfaceContainerHigh=Color(0xFFF3E6DE),surfaceContainerHighest=Color(0xFFEDE0D8))
private val Oscuro=darkColorScheme(primary=Color(0xFFFFB870),onPrimary=Color(0xFF4A2800),primaryContainer=Color(0xFF693C00),onPrimaryContainer=Color(0xFFFFDDB9),secondary=Color(0xFFDDC2A1),onSecondary=Color(0xFF3E2D16),secondaryContainer=Color(0xFF57432A),onSecondaryContainer=Color(0xFFFADEBC),tertiary=Color(0xFFB9CFA1),onTertiary=Color(0xFF253516),tertiaryContainer=Color(0xFF3B4C2A),onTertiaryContainer=Color(0xFFD5EABB),error=Color(0xFFFFB4AB),onError=Color(0xFF690005),errorContainer=Color(0xFF93000A),onErrorContainer=Color(0xFFFFDAD6),background=Color(0xFF18120D),onBackground=Color(0xFFEDE0D8),surface=Color(0xFF18120D),onSurface=Color(0xFFEDE0D8),surfaceVariant=Color(0xFF51443A),onSurfaceVariant=Color(0xFFD5C3B6),outline=Color(0xFF9E8E82),outlineVariant=Color(0xFF51443A),scrim=Color.Black,inverseSurface=Color(0xFFEDE0D8),inverseOnSurface=Color(0xFF372F29),inversePrimary=Color(0xFF8A4B08),surfaceDim=Color(0xFF18120D),surfaceBright=Color(0xFF403832),surfaceContainerLowest=Color(0xFF120D09),surfaceContainerLow=Color(0xFF211A14),surfaceContainer=Color(0xFF251E18),surfaceContainerHigh=Color(0xFF302822),surfaceContainerHighest=Color(0xFF3B332D))
@Composable
fun BiblioMobilTheme(
    oscuro: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(colorScheme = if (oscuro) Oscuro else Claro, content = content)
}
