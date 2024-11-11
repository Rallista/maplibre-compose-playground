package com.maplibre.compose.camera.models

import android.os.Parcelable
import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.maplibre.compose.runtime.screenHeightPx
import com.maplibre.compose.runtime.screenWidthPx
import kotlinx.parcelize.Parcelize

@Parcelize
data class CameraPadding(
    val left: Double = 0.0,
    val top: Double = 0.0,
    val right: Double = 0.0,
    val bottom: Double = 0.0
) : Parcelable {
  companion object {

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
    fun fractionOfScreen(
      @FloatRange(from = 0.0, to = 1.0) start: Float = 0.0f,
      @FloatRange(from = 0.0, to = 1.0) top: Float = 0.0f,
      @FloatRange(from = 0.0, to = 1.0) end: Float = 0.0f,
      @FloatRange(from = 0.0, to = 1.0) bottom: Float = 0.0f
    ): CameraPadding {
      val layoutDirection = LocalLayoutDirection.current
      val screenWidth = screenWidthPx()
      val screenHeight = screenHeightPx()

      val left = when (layoutDirection) {
        LayoutDirection.Ltr -> start * screenWidth
        LayoutDirection.Rtl -> end * screenWidth
      }
      val top = top * screenHeight
      val right = when (layoutDirection) {
        LayoutDirection.Ltr -> end * screenWidth
        LayoutDirection.Rtl -> start * screenWidth
      }
      val bottom = bottom * screenHeight

      return CameraPadding(
        left = left.toDouble(),
        top = top.toDouble(),
        right = right.toDouble(),
        bottom = bottom.toDouble())
    }

    @Composable
    fun padding(all: Dp = 0.dp): CameraPadding {
      return padding(PaddingValues(all))
    }

    @Composable
    fun padding(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp): CameraPadding {
      return padding(PaddingValues(start, top, end, bottom))
    }

    @Composable
    fun padding(horizontal: Dp = 0.dp, vertical: Dp = 0.dp): CameraPadding {
      return padding(PaddingValues(horizontal, vertical))
    }

    @Composable
    fun padding(paddingValues: PaddingValues): CameraPadding {
      val scale = LocalDensity.current.density
      val layoutDirection = LocalLayoutDirection.current

      val left = paddingValues.calculateLeftPadding(layoutDirection) * scale
      val top = paddingValues.calculateTopPadding() * scale
      val right = paddingValues.calculateRightPadding(layoutDirection) * scale
      val bottom = paddingValues.calculateBottomPadding() * scale

      return CameraPadding(
          left = left.value.toDouble(),
          top = top.value.toDouble(),
          right = right.value.toDouble(),
          bottom = bottom.value.toDouble()
      )
    }

    fun doubleArray(padding: DoubleArray?): CameraPadding {
      return CameraPadding(
          left = padding?.getOrNull(0) ?: 0.0,
          top = padding?.getOrNull(1) ?: 0.0,
          right = padding?.getOrNull(2) ?: 0.0,
          bottom = padding?.getOrNull(3) ?: 0.0)
    }
  }

  fun toDoubleArray(): DoubleArray {
    return doubleArrayOf(left, top, right, bottom)
  }
}
