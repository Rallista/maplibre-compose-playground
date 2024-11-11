package com.maplibre.compose.settings

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.parcelize.Parcelize

/**
 * Customize the compass on the map.
 *
 * @param enabled Whether the compass is enabled.
 * @param fadeFacingNorth Whether the compass fades away when facing north.
 * @param gravity The gravity of the compass. E.g. Gravity.TOP or Gravity.START for TopStart.
 * @param margins The margins of the compass.
 */
@Parcelize
data class CompassSettings(
    var enabled: Boolean? = null,
    var fadeFacingNorth: Boolean? = null,
    var gravity: Int? = null,
    var margins: MarginInsets? = null,
) : Parcelable {

  companion object {

    /**
     * Configure the compass.
     *
     * @param enabled Whether the compass is enabled.
     * @param isFacingNorth
     * @param position The position of the compass.
     * @return The compass settings.
     */
    @Composable
    fun initWithPosition(
        enabled: Boolean? = null,
        isFacingNorth: Boolean? = null,
        position: MapControlPosition = MapControlPosition.TopStart()
    ): CompassSettings =
        initWithLayoutAndPosition(
          LocalLayoutDirection.current, LocalDensity.current, enabled, isFacingNorth, position)

    /**
     * Configure the compass.
     *
     * @param layoutDirection The layout direction of the screen.
     * @param density The layout density (e.g. [LocalDensity.current]).
     * @param enabled Whether the compass is enabled.
     * @param isFacingNorth
     * @param position The position of the compass.
     * @return The compass settings.
     */
    fun initWithLayoutAndPosition(
        layoutDirection: LayoutDirection,
        density: Density,
        enabled: Boolean? = null,
        isFacingNorth: Boolean? = null,
        position: MapControlPosition = MapControlPosition.TopStart()
    ): CompassSettings =
        CompassSettings(
            enabled,
            isFacingNorth,
            position.asGravity(),
            position.asMarginInsets(layoutDirection, density))
  }
}
