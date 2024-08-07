package com.maplibre.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

/**
 * Create a map style provider to inject your desired URLs into the Composable hierarchy.
 *
 * You can create a custom style provider that uses any composable context by implementing this
 * interface. See [MapLibreSystemThemeStyleProvider] for an example.
 */
interface MapLibreStyleProvider {
  @Composable fun getStyleUrl(): String
}

/**
 * Create a map style provider to inject an automatic light and dark mode style URLs into the
 * Composable hierarchy.
 *
 * @property lightModeStyleUrl
 * @property darkModeStyleUrl
 */
class MapLibreSystemThemeStyleProvider(
    private var lightModeStyleUrl: String,
    private var darkModeStyleUrl: String
) : MapLibreStyleProvider {
  @Composable
  override fun getStyleUrl(): String {
    val isDarkTheme = isSystemInDarkTheme()
    return if (isDarkTheme) darkModeStyleUrl else lightModeStyleUrl
  }
}

val LocalMapLibreStyleProvider =
    compositionLocalOf<MapLibreStyleProvider> {
      /**
       * If you see this error, you are using [mapLibreStyleUrl()] in your Composable hierarchy but
       * you have not provided a [MapLibreStyleProvider] to the Composable hierarchy. You must wrap
       * your view stack in `[MapLibreStyleProviding]`
       *
       * E.g. this setContent block seen in a typical `MainActivity` where mapLibreStyleProvider may
       * be a val of `MapLibreSystemThemeStyleProvider`:
       * ```kotlin
       * setContent {
       *      MapLibreStyleProviding(mapLibreStyleProvider) {
       *          Surface(modifier = Modifier.fillMaxSize()) {
       *              Main()
       *          }
       *      }
       *  }
       * ```
       *
       * See the maplibre-compose-playground demo app's MainActivity.kt for more details.
       */
      error("No MapLibreStyleProvider wrapping the Composable content calling mapLibreStyleUrl()")
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
  CompositionLocalProvider(LocalMapLibreStyleProvider provides mapLibreStyleProvider) { content() }
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
