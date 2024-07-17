package com.maplibre.compose.camera

import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.maplibre.compose.camera.models.CameraPadding

/**
 * Create a camera padding that's a fraction of the screen size.
 *
 * Important! This function can take values larger than 1.0 (need to investigate maplibre-native to
 * determine what the acceptable range is).
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
    @FloatRange(from = 0.0, to = 2.0) start: Float = 0.0f,
    @FloatRange(from = 0.0, to = 2.0) top: Float = 0.0f,
    @FloatRange(from = 0.0, to = 2.0) end: Float = 0.0f,
    @FloatRange(from = 0.0, to = 2.0) bottom: Float = 0.0f
): CameraPadding {
  // Get the pixel dimensions of the screen. Dp is a density-independent pixel unit.
  val screenHeight = LocalConfiguration.current.screenHeightDp.toDouble()
  val screenWidth = LocalConfiguration.current.screenWidthDp.toDouble()

  return CameraPadding(
      start = start.toDouble() * screenWidth,
      top = top.toDouble() * screenHeight,
      end = end.toDouble() * screenWidth,
      bottom = bottom.toDouble() * screenHeight)
}
