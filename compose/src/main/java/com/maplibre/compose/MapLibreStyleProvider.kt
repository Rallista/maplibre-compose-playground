package com.maplibre.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Create a map style provider to inject your desired light and dark mode style URLs
 * into the Composable hierarchy.
 *
 * @property lightModeStyleUrl
 * @property darkModeStyleUrl
 */
class MapLibreStyleProvider(
    private var lightModeStyleUrl: String,
    private var darkModeStyleUrl: String
) {
    fun getStyleUrl(isDarkMode: Boolean): String {
        return if (isDarkMode) darkModeStyleUrl else lightModeStyleUrl
    }
}

val LocalMapLibreStyleProvider = staticCompositionLocalOf<MapLibreStyleProvider> {
    error("No MapLibreStyleProvider provided")
}

/**
 * Provide a [MapLibreStyleProvider] to the Composable hierarchy.
 *
 * @param mapLibreStyleProvider
 * @param content
 */
@Composable
fun MapLibreStyleProviding(
    mapLibreStyleProvider: MapLibreStyleProvider,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalMapLibreStyleProvider provides mapLibreStyleProvider) {
        content()
    }
}

/**
 * Remember the map style URL based on the current system theme.
 *
 * @return the map style URL string
 */
@Composable
fun rememberMapStyleUrl(): String {
    val isDarkTheme = isSystemInDarkTheme()
    val mapStyleProvider = LocalMapLibreStyleProvider.current
    return mapStyleProvider.getStyleUrl(isDarkTheme)
}