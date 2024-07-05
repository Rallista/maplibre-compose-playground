package com.maplibre.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Create a map style provider to inject your desired URLs
 * into the Composable hierarchy.
 *
 * You can create a custom style provider that uses any composable context
 * by implementing this interface. See [MapLibreSystemThemeStyleProvider] for an example.
 */
interface MapLibreStyleProvider {
    @Composable
    fun getStyleUrl(): String
}

/**
 * Create a map style provider to inject an automatic light and dark mode style URLs
 * into the Composable hierarchy.
 *
 * @property lightModeStyleUrl
 * @property darkModeStyleUrl
 */
class MapLibreSystemThemeStyleProvider(
    private var lightModeStyleUrl: String,
    private var darkModeStyleUrl: String
): MapLibreStyleProvider {
    @Composable
    override fun getStyleUrl(): String {
        val isDarkTheme = isSystemInDarkTheme()
        return if (isDarkTheme) darkModeStyleUrl else lightModeStyleUrl
    }
}

val LocalMapLibreStyleProvider = compositionLocalOf<MapLibreStyleProvider> {
    error("No MapLibreStyleProvider provided when using mapLibreStyleUrl()")
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
fun mapLibreStyleUrl(): String {
    return LocalMapLibreStyleProvider.current.getStyleUrl()
}