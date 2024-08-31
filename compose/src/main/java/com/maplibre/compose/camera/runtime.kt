package com.maplibre.compose.camera

import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import com.maplibre.compose.camera.models.CameraPadding
import com.maplibre.compose.runtime.screenHeightPx
import com.maplibre.compose.runtime.screenWidthPx

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
  val screenWidth = screenWidthPx()
  val screenHeight = screenHeightPx()

  return CameraPadding(
      start = start.toDouble() * screenWidth,
      top = top.toDouble() * screenHeight,
      end = end.toDouble() * screenWidth,
      bottom = bottom.toDouble() * screenHeight)
}
