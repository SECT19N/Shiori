package com.section.shiori.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = BlueOnPrimaryDark,
    primaryContainer = BluePrimaryContainerDark,
    onPrimaryContainer = BlueOnPrimaryContainerDark,
    secondary = BlueSecondaryDark,
    onSecondary = BlueOnSecondaryDark,
    secondaryContainer = BlueSecondaryContainerDark,
    onSecondaryContainer = BlueOnSecondaryContainerDark,
    tertiary = BlueTertiaryDark,
    onTertiary = BlueOnTertiaryDark,
    tertiaryContainer = BlueTertiaryContainerDark,
    onTertiaryContainer = BlueOnTertiaryContainerDark,
    error = BlueErrorDark,
    onError = BlueOnErrorDark,
    errorContainer = BlueErrorContainerDark,
    onErrorContainer = BlueOnErrorContainerDark,
    background = BlueBackgroundDark,
    onBackground = BlueOnBackgroundDark,
    surface = BlueSurfaceDark,
    onSurface = BlueOnSurfaceDark,
    surfaceVariant = BlueSurfaceVariantDark,
    onSurfaceVariant = BlueOnSurfaceVariantDark,
    outline = BlueOutlineDark,
    outlineVariant = BlueOutlineVariantDark,
    inverseSurface = BlueInverseSurfaceDark,
    inverseOnSurface = BlueInverseOnSurfaceDark,
    inversePrimary = BlueInversePrimaryDark,
    surfaceDim = BlueSurfaceDimDark,
    surfaceBright = BlueSurfaceBrightDark,
    surfaceContainerLowest = BlueSurfaceContainerLowestDark,
    surfaceContainerLow = BlueSurfaceContainerLowDark,
    surfaceContainer = BlueSurfaceContainerDark,
    surfaceContainerHigh = BlueSurfaceContainerHighDark,
    surfaceContainerHighest = BlueSurfaceContainerHighestDark
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimaryLight,
    onPrimary = BlueOnPrimaryLight,
    primaryContainer = BluePrimaryContainerLight,
    onPrimaryContainer = BlueOnPrimaryContainerLight,
    secondary = BlueSecondaryLight,
    onSecondary = BlueOnSecondaryLight,
    secondaryContainer = BlueSecondaryContainerLight,
    onSecondaryContainer = BlueOnSecondaryContainerLight,
    tertiary = BlueTertiaryLight,
    onTertiary = BlueOnTertiaryLight,
    tertiaryContainer = BlueTertiaryContainerLight,
    onTertiaryContainer = BlueOnTertiaryContainerLight,
    error = BlueErrorLight,
    onError = BlueOnErrorLight,
    errorContainer = BlueErrorContainerLight,
    onErrorContainer = BlueOnErrorContainerLight,
    background = BlueBackgroundLight,
    onBackground = BlueOnBackgroundLight,
    surface = BlueSurfaceLight,
    onSurface = BlueOnSurfaceLight,
    surfaceVariant = BlueSurfaceVariantLight,
    onSurfaceVariant = BlueOnSurfaceVariantLight,
    outline = BlueOutlineLight,
    outlineVariant = BlueOutlineVariantLight,
    inverseSurface = BlueInverseSurfaceLight,
    inverseOnSurface = BlueInverseOnSurfaceLight,
    inversePrimary = BlueInversePrimaryLight,
    surfaceDim = BlueSurfaceDimLight,
    surfaceBright = BlueSurfaceBrightLight,
    surfaceContainerLowest = BlueSurfaceContainerLowestLight,
    surfaceContainerLow = BlueSurfaceContainerLowLight,
    surfaceContainer = BlueSurfaceContainerLight,
    surfaceContainerHigh = BlueSurfaceContainerHighLight,
    surfaceContainerHighest = BlueSurfaceContainerHighestLight
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ShioriTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Off by default so the app keeps its own blue identity; opt in per-call if you
    // ever want to follow the system wallpaper palette on Android 12+.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        // Expressive motion for the whole app: springier, more overshoot on the shape morphs
        // and container transforms than the standard scheme.
        motionScheme = MotionScheme.expressive(),
        typography = Typography,
        content = content
    )
}
