package com.maplibre.compose.camera

import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.maplibre.compose.camera.models.CameraPadding

/**
 * Create a camera padding that's a fraction of the screen size.
 *
 * The default is 0.0f for all values.
 *
 * @param start The fraction of the screen width to pad on the start/left.
 * @param top The fraction of the screen height to pad on the top.
 * @param end The fraction of the screen width to pad on the end/right.
 * @param bottom The fraction of the screen height to pad on the bottom.
 * @return The camera padding.
 */
@Composable
fun cameraPaddingFractionOfScreen(
    @FloatRange(from = 0.0, to = 1.0) start: Float = 0.0f,
    @FloatRange(from = 0.0, to = 1.0) top: Float = 0.0f,
    @FloatRange(from = 0.0, to = 1.0) end: Float = 0.0f,
    @FloatRange(from = 0.0, to = 1.0) bottom: Float = 0.0f
): CameraPadding {
  // Get the pixel dimensions of the screen. Dp is a density-independent pixel unit.
  val screenHeightDp = LocalConfiguration.current.screenHeightDp
  val screenWidthDp = LocalConfiguration.current.screenWidthDp

  val screenHeight = LocalDensity.current.run { screenHeightDp.dp.toPx() }
  val screenWidth = LocalDensity.current.run { screenWidthDp.dp.toPx() }

  return CameraPadding(
      start = start.toDouble() * screenWidth,
      top = top.toDouble() * screenHeight,
      end = end.toDouble() * screenWidth,
      bottom = bottom.toDouble() * screenHeight)
}
