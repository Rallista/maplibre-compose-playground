package com.maplibre.compose.runtime

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.LayoutDirection

/**
 * Get the layout direction of the screen as a [LayoutDirection].
 *
 * @return The current layout direction of the screen.
 */
@Composable
fun localLayoutDirection(): LayoutDirection {
  val direction = LocalConfiguration.current.layoutDirection
  return LayoutDirection.entries[direction]
}