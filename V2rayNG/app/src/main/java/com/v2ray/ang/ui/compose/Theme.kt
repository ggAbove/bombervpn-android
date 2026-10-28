package com.v2ray.ang.ui.compose

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.v2ray.ang.AppConfig
import com.v2ray.ang.handler.MmkvManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private val LightColor = lightColorScheme(
    primary = Color(0xFF9A5B00), onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDDB1), onPrimaryContainer = Color(0xFF301B00),
    secondary = Color(0xFF5E6780), onSecondary = Color.White,
    secondaryContainer = Color(0xFFE3E7EF), onSecondaryContainer = Color(0xFF151C33),
    tertiary = Color(0xFF137A48), onTertiary = Color.White,
    background = Color(0xFFEEF1F6), onBackground = Color(0xFF0B1122),
    surface = Color.White, onSurface = Color(0xFF0B1122),
    surfaceVariant = Color(0xFFE3E7EF), onSurfaceVariant = Color(0xFF5E6780),
    outline = Color(0xFF747E94), outlineVariant = Color(0xFFE3E7EF),
    surfaceContainerLowest = Color.White, surfaceContainerLow = Color(0xFFF5F7FA),
    surfaceContainer = Color(0xFFEEF1F6), surfaceContainerHigh = Color(0xFFE3E7EF),
    surfaceContainerHighest = Color(0xFFD9DFE9),
    error = Color(0xFFC0362C), onError = Color.White,
)

private val DarkColor = darkColorScheme(
    primary = Color(0xFFFFB02E), onPrimary = Color(0xFF1A1204),
    primaryContainer = Color(0xFF5A3700), onPrimaryContainer = Color(0xFFFFDDB1),
    secondary = Color(0xFFBEC6DB), onSecondary = Color(0xFF232C4A),
    secondaryContainer = Color(0xFF232C4A), onSecondaryContainer = Color(0xFFEEF1F7),
    tertiary = Color(0xFF3CCB7F), onTertiary = Color(0xFF002110),
    background = Color(0xFF0B1122), onBackground = Color(0xFFEEF1F7),
    surface = Color(0xFF151C33), onSurface = Color(0xFFEEF1F7),
    surfaceVariant = Color(0xFF232C4A), onSurfaceVariant = Color(0xFFBEC6DB),
    outline = Color(0xFF8E97AE), outlineVariant = Color(0xFF232C4A),
    surfaceContainerLowest = Color(0xFF080D1B), surfaceContainerLow = Color(0xFF10172A),
    surfaceContainer = Color(0xFF151C33), surfaceContainerHigh = Color(0xFF1C2440),
    surfaceContainerHighest = Color(0xFF232C4A),
    error = Color(0xFFFF6B6B), onError = Color(0xFF370001),
)

// Semantic Colors
val colorPing = Color(0xFF009966) // Green
val colorPingRed = Color(0xFFFF0099) // Pink Red
val colorConfigType = Color(0xFFf97910) // Orange
val colorFabActive = Color(0xFFf97910) // Orange
val colorFabInactiveLight = Color(0xFF9C9C9C) // Gray
val colorFabInactiveDark = Color(0xFF646464) // Dark Gray
val dividerColorLight = Color(0xFFE0E0E0) // Light Gray
val dividerColorDark = Color(0xFF424242) // Dark Gray

// Toast Colors 85%
val toastNormalBgLight = Color(0xD9353A3E) // Dark Gray
val toastNormalBgDark = Color(0xD94A4F54) // Darker Gray
val toastSuccessBg = Color(0xD9388E3C) // Green
val toastErrorBg = Color(0xD9D50000) // Red
val toastInfoBg = Color(0xD93F51B5) // Indigo Blue
val toastIconCircleBg = Color(0x33FFFFFF) // Semi-transparent White
val toastTextColor = Color.White // White

object ThemeManager {
    private val _themeMode = MutableStateFlow(
        MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "0") ?: "0"
    )
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _dynamicColorEnabled = MutableStateFlow(
        MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, false)
    )
    val dynamicColorEnabled: StateFlow<Boolean> = _dynamicColorEnabled.asStateFlow()

    fun setThemeMode(mode: String) {
        MmkvManager.encodeSettings(AppConfig.PREF_UI_MODE_NIGHT, mode)
        _themeMode.value = mode
    }

    fun setDynamicColorEnabled(enabled: Boolean) {
        MmkvManager.encodeSettings(AppConfig.PREF_DYNAMIC_COLOR, enabled)
        _dynamicColorEnabled.value = enabled
    }

    fun refresh() {
        _themeMode.value =
            MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "0") ?: "0"
        _dynamicColorEnabled.value =
            MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, false)
    }
}

@Composable
fun resolveDarkTheme(): Boolean {
    val mode by ThemeManager.themeMode.collectAsState()
    return when (mode) {
        "1" -> false
        "2" -> true
        else -> isSystemInDarkTheme()
    }
}

val LocalDarkTheme = compositionLocalOf { false }

@Composable
fun AppTheme(
    darkTheme: Boolean = resolveDarkTheme(),
    content: @Composable () -> Unit
) {
    val dynamicColor by ThemeManager.dynamicColorEnabled.collectAsState()
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColor
        else -> LightColor
    }
    val snackbarController = rememberAppSnackbarController()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as? Activity ?: return@SideEffect
            val window = activity.window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme,
        LocalAppSnackbar provides snackbarController
    ) {
        MaterialTheme(
            colorScheme = colorScheme
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AppSnackbarBridge(controller = snackbarController)
                content()
                AppSnackbarHost(hostState = snackbarController.hostState)
            }
        }
    }
}
