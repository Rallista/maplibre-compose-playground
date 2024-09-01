package com.maplibre.compose.settings

import android.os.Parcelable
import android.view.Gravity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.maplibre.compose.runtime.localLayoutDirection
import kotlinx.parcelize.Parcelize

sealed class MapControlPosition(open val horizontal: Dp = 0.dp, open val vertical: Dp = 0.dp) {
  /**
   * Position the control in the top start (top leading) corner of the map
   *
   * @property horizontal The padding in the horizontal direction.
   * @property vertical The padding in the vertical direction.
   */
  class TopStart(override val horizontal: Dp = 16.dp, override val vertical: Dp = 16.dp) :
      MapControlPosition(horizontal, vertical)

  /**
   * Position the control in the top center (top trailing) corner of the map
   *
   * @param vertical The padding on the vertical axis.
   */
  class TopCenter(override val vertical: Dp = 16.dp) : MapControlPosition(0.dp, vertical)

  /**
   * Position the control in the top end (top trailing) corner of the map
   *
   * @param horizontal The padding on the horizontal axis.
   * @param vertical The padding on the vertical axis.
   */
  class TopEnd(override val horizontal: Dp = 16.dp, override val vertical: Dp = 16.dp) :
      MapControlPosition(horizontal, vertical)

  /**
   * Position the control on the start/leading edge of the vertical center of the map.
   *
   * @param horizontal The padding on the horizontal axis.
   */
  class CenterStart(override val horizontal: Dp = 16.dp) :
      MapControlPosition(horizontal, 0.dp) // Center end does not respond to vertical

  /**
   * Position the control in the center of the map.
   *
   * Padding is not supported for this position.
   */
  class Center() : MapControlPosition(0.dp, 0.dp)

  /**
   * Position the control on the end/trailing edge of the vertical center of the map.
   *
   * @property horizontal The padding in the horizontal direction.
   */
  class CenterEnd(override val horizontal: Dp = 16.dp) : MapControlPosition(horizontal, 0.dp)

  /**
   * Position the control in the bottom start (bottom leading) corner of the map
   *
   * @property horizontal The padding in the horizontal direction.
   * @property vertical The padding in the vertical direction.
   */
  class BottomStart(override val horizontal: Dp = 16.dp, override val vertical: Dp = 16.dp) :
      MapControlPosition(horizontal, vertical)

  /**
   * Position the control in the bottom center of the map
   *
   * @property vertical The padding in the vertical direction.
   */
  class BottomCenter(override val vertical: Dp = 16.dp) : MapControlPosition(0.dp, vertical)

  /**
   * Position the control in the bottom end (bottom trailing) corner of the map
   *
   * @property horizontal The padding in the horizontal direction.
   * @property vertical The padding in the vertical direction.
   */
  class BottomEnd(override val horizontal: Dp = 16.dp, override val vertical: Dp = 16.dp) :
      MapControlPosition(horizontal, vertical)

  internal fun asGravity(): Int {
    return when (this) {
      is TopStart -> Gravity.TOP or Gravity.START
      is TopCenter -> Gravity.TOP or Gravity.CENTER_HORIZONTAL
      is TopEnd -> Gravity.TOP or Gravity.END
      is CenterStart -> Gravity.CENTER_VERTICAL or Gravity.START
      is Center -> Gravity.CENTER
      is CenterEnd -> Gravity.CENTER_VERTICAL or Gravity.END
      is BottomStart -> Gravity.BOTTOM or Gravity.START
      is BottomCenter -> Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
      is BottomEnd -> Gravity.BOTTOM or Gravity.END
    }
  }

  internal fun asMarginInsets(layoutDirection: LayoutDirection, density: Density): MarginInsets {
    return MarginInsets.createFromLayoutAndPadding(
        layoutDirection = layoutDirection,
        density = density,
        padding =
            PaddingValues(start = horizontal, top = vertical, end = horizontal, bottom = vertical))
  }
}

/**
 * Manually position the control on the map using pixels.
 *
 * To more easily position the control on a map use the control's `initWithPosition` and
 * [MapControlPosition] as it safely handles the nuances of map control positioning as well as DPI
 * scaling. [createFromPadding] can be used to manually create a [MarginInsets] with DPI scaling.
 *
 * @property start The start margin.
 * @property top The top margin.
 * @property end The end margin.
 * @property bottom The bottom margin.
 */
@Parcelize
data class MarginInsets(
    val start: Int = 0,
    val top: Int = 0,
    val end: Int = 0,
    val bottom: Int = 0
) : Parcelable {
  companion object {

    /**
     * Manually position the control on the map using dp padding.
     *
     * To more easily position the control on a map use the control's `initWithPosition` and
     * [MapControlPosition] as it safely handles the nuances of map control positioning.
     *
     * @param padding The padding values to use.
     */
    @Composable
    fun createFromPadding(padding: PaddingValues): MarginInsets {
      return createFromLayoutAndPadding(
        layoutDirection = localLayoutDirection(),
        density = LocalDensity.current,
        padding = padding
      )
    }

    /**
     * Manually position the control on the map using dp padding, a layout direction, and a density.
     *
     * To more easily position the control on a map use the control's `initWithPosition` and
     * [MapControlPosition] as it safely handles the nuances of map control positioning.
     *
     * @param layoutDirection The layout direction to use
     * @param density The screen density to use (e.g. LocalDensity.current).
     * @param padding The padding values to use.
     */
    fun createFromLayoutAndPadding(
      layoutDirection: LayoutDirection,
      density: Density,
      padding: PaddingValues
    ): MarginInsets {
      val start = padding.calculateStartPadding(layoutDirection)
      val top = padding.calculateTopPadding()
      val end = padding.calculateEndPadding(layoutDirection)
      val bottom = padding.calculateBottomPadding()

      val startPx = with(density) { start.toPx().toInt() }
      val topPx = with(density) { top.toPx().toInt() }
      val endPx = with(density) { end.toPx().toInt() }
      val bottomPx = with(density) { bottom.toPx().toInt() }

      return MarginInsets(start = startPx, top = topPx, end = endPx, bottom = bottomPx)
    }
  }
}
