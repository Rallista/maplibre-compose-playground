/*
 * This file is part of maplibre-compose.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.maplibre.compose.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal for providing a custom pixel ratio to MapView composables.
 *
 * The pixel ratio controls how MapLibre renders symbols, text, and other map elements. Higher
 * values make these elements appear larger on screen.
 * - A value of 0 (default) uses the system's display density
 * - Values between 1.5 and 2.0 typically work well for Android Auto displays where symbols may
 *   appear too small
 *
 * Example usage:
 * ```kotlin
 * ProvidePixelRatio(pixelRatio = 1.5f) {
 *     MapView(
 *         modifier = Modifier.fillMaxSize(),
 *         styleUrl = "...",
 *         // pixelRatio will be automatically picked up from LocalPixelRatio
 *     )
 * }
 * ```
 */
val LocalPixelRatio = staticCompositionLocalOf { 0f }

/**
 * Provides a custom pixel ratio to all MapView composables within the content block.
 *
 * @param pixelRatio The pixel ratio to use for rendering. Values greater than 0 will override the
 *   default pixel ratio calculated from display density. Higher values make symbols, text, and
 *   other map elements appear larger. A value of 0 uses the system's display density.
 * @param content The composable content that will receive the pixel ratio.
 */
@Composable
fun ProvidePixelRatio(pixelRatio: Float, content: @Composable () -> Unit) {
  CompositionLocalProvider(LocalPixelRatio provides pixelRatio) { content() }
}
