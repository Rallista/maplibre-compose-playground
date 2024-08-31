package com.maplibre.compose.runtime

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
internal fun screenHeightPx(): Float {
  // Get the pixel dimensions of the screen. Dp is a density-independent pixel unit.
  val screenHeightDp = LocalConfiguration.current.screenHeightDp
  return LocalDensity.current.run { screenHeightDp.dp.toPx() }
}

@Composable
internal fun screenWidthPx(): Float {
  // Get the pixel dimensions of the screen. Dp is a density-independent pixel unit.
  val screenWidthDp = LocalConfiguration.current.screenWidthDp
  return LocalDensity.current.run { screenWidthDp.dp.toPx() }
}
