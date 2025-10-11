package com.diegoferreiracaetano.dlearn.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Dark Theme Colors - Black Based
val primaryDark = Color(0xFF12CDD9) // Keep accent
val onPrimaryDark = Color(0xFFFFFFFF)
val primaryContainerDark = Color(0xFF1E1E1E) // Dark gray
val onPrimaryContainerDark = Color(0xFFFFFFFF)
val backgroundDark = Color(0xFF000000) // Black
val onBackgroundDark = Color(0xFFFFFFFF)
val surfaceDark = Color(0xFF121212) // Near black
val onSurfaceDark = Color(0xFFFFFFFF)
val surfaceVariantDark = Color(0xFF222222) // Lighter gray
val onSurfaceVariantDark = Color(0xFFEAEAEA)

val secondaryDark = Color(0xFF22B07D) // Green
val onSecondaryDark = Color(0xFFFFFFFF)
val secondaryContainerDark = Color(0xFF1E1E1E)
val onSecondaryContainerDark = Color(0xFFFFFFFF)

val tertiaryDark = Color(0xFFFF8700) // Orange
val onTertiaryDark = Color(0xFF000000)
val tertiaryContainerDark = Color(0xFF1E1E1E)
val onTertiaryContainerDark = Color(0xFFFFFFFF)

val errorDark = Color(0xFFFF7256) // Red
val onErrorDark = Color(0xFFFFFFFF)
val errorContainerDark = Color(0xFF1E1E1E)
val onErrorContainerDark = Color(0xFFFFFFFF)

val outlineDark = Color(0xFF92929D) // Dark Grey
val outlineVariantDark = Color(0xFF444444) // Grey
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFEAEAEA) // Light Grey
val inverseOnSurfaceDark = Color(0xFF000000)
val inversePrimaryDark = Color(0xFF12CDD9) // Blue Accent

val surfaceDimDark = Color(0xFF121212)
val surfaceBrightDark = Color(0xFF383838)
val surfaceContainerLowestDark = Color(0xFF0D0D0D)
val surfaceContainerLowDark = Color(0xFF1E1E1E)
val surfaceContainerDark = Color(0xFF222222)
val surfaceContainerHighDark = Color(0xFF2C2C2C)
val surfaceContainerHighestDark = Color(0xFF383838)

val successDark = Color(0xFF22B07D)
val onSuccessDark = Color(0xFFFFFFFF)
val successContainerDark = Color(0xFF1E1E1E)
val onSuccessContainerDark = Color(0xFF22B07D)

val warningDark = Color(0xFFFF8700)
val onWarningDark = Color(0xFFFFFFFF)
val warningContainerDark = Color(0xFF1E1E1E)
val onWarningContainerDark = Color(0xFFFF8700)


// Light Theme Colors - White Based
val primaryLight = Color(0xFF12CDD9) // Keep accent
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFE0F7FA) // Light cyan
val onPrimaryContainerLight = Color(0xFF001F23)
val backgroundLight = Color(0xFFFFFFFF) // White
val onBackgroundLight = Color(0xFF000000) // Black
val surfaceLight = Color(0xFFFFFFFF) // White
val onSurfaceLight = Color(0xFF000000) // Black
val surfaceVariantLight = Color(0xFFF0F0F0) // Light gray
val onSurfaceVariantLight = Color(0xFF42474E)

val secondaryLight = Color(0xFF5C9B70)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFDCE2DA)
val onSecondaryContainerLight = Color(0xFF191C1E)

val tertiaryLight = Color(0xFFBF6750)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFFFDBCF)
val onTertiaryContainerLight = Color(0xFF410002)

val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)

val outlineLight = Color(0xFF727982)
val outlineVariantLight = Color(0xFFC2C7CF)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF2E3137) // Dark gray
val inverseOnSurfaceLight = Color(0xFFF0F0F5)
val inversePrimaryLight = Color(0xFF86D2DB)

val surfaceDimLight = Color(0xFFDCDCDC)
val surfaceBrightLight = Color(0xFFFAFAFA)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFF5F5F5)
val surfaceContainerLight = Color(0xFFF0F0F0)
val surfaceContainerHighLight = Color(0xFFEAEAEA)
val surfaceContainerHighestLight = Color(0xFFE0E0E0)


//@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

@Immutable
data class ExtendedColorScheme(
    val success: ColorFamily,
    val warning: ColorFamily,
)
