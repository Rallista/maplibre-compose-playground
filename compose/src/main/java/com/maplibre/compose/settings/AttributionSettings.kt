package com.maplibre.compose.settings

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.maplibre.compose.runtime.localLayoutDirection
import kotlinx.parcelize.Parcelize

/**
 * Customize the attribution icon on the map.
 *
 * @param enabled Whether the attribution view is enabled.
 * @param gravity The gravity of the attribution view. E.g. Gravity.TOP or Gravity.START for
 *   TopStart.
 * @param margins The margins of the attribution view.
 * @param tintColor The tint color of the attribution view.
 */
@Parcelize
data class AttributionSettings(
    var enabled: Boolean? = null,
    var gravity: Int? = null,
    var margins: MarginInsets? = null,
    var tintColor: Int? = null
) : Parcelable {

  companion object {


    /**
     * Configure the attribution view.
     *
     * @param enabled Whether the attribution view is enabled.
     * @param position The position of the attribution view.
     * @param tintColor The tint color of the attribution view.
     * @return The attribution settings.
     */
    @Composable
    fun initWithPosition(
        enabled: Boolean? = null,
        position: MapControlPosition = MapControlPosition.TopStart(),
        tintColor: Int? = null
    ): AttributionSettings = initWithLayoutAndPosition(
        localLayoutDirection(), LocalDensity.current, enabled, position, tintColor)

    /**
     * Configure the attribution view.
     *
     * @param layoutDirection The layout direction of the screen.
     * @param density The layout density (e.g. [LocalDensity.current]).
     * @param enabled Whether the attribution view is enabled.
     * @param position The position of the attribution view.
     * @param tintColor The tint color of the attribution view.
     * @return The attribution settings.
     */
    fun initWithLayoutAndPosition(
      layoutDirection: LayoutDirection,
      density: Density,
      enabled: Boolean? = null,
      position: MapControlPosition = MapControlPosition.TopStart(),
      tintColor: Int? = null
    ): AttributionSettings =
        AttributionSettings(enabled, position.asGravity(), position.asMarginInsets(layoutDirection, density), tintColor)
  }
}
