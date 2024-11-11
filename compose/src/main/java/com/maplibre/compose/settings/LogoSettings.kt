package com.maplibre.compose.settings

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.parcelize.Parcelize

/**
 * Customize the logo on the map using manual MarginInsets and gravity.
 *
 * Use [LogoSettings.initWithPosition] to configure the logo as it accurately handles the position
 * and margins.
 *
 * @param enabled Whether the logo is enabled.
 * @param gravity The gravity of the logo. E.g. Gravity.TOP or Gravity.START for TopStart.
 * @param margins The margins of the logo.
 */
@Parcelize
data class LogoSettings(
    var enabled: Boolean? = null,
    var gravity: Int? = null,
    var margins: MarginInsets? = null
) : Parcelable {

  companion object {
    /**
     * Configure the logo.
     *
     * @param enabled Whether the logo is enabled.
     * @param position The position of the logo.
     * @return The logo settings.
     */
    @Composable
    fun initWithPosition(
        enabled: Boolean? = null,
        position: MapControlPosition = MapControlPosition.TopStart()
    ): LogoSettings =
        LogoSettings(
            enabled,
            position.asGravity(),
            position.asMarginInsets(LocalLayoutDirection.current, LocalDensity.current))

    /**
     * Configure the logo.
     *
     * @param layoutDirection The layout direction of the logo.
     * @param density The density of the logo.
     * @param enabled Whether the logo is enabled.
     * @param position The position of the logo.
     * @return The logo settings.
     */
    fun initWithLayoutAndPosition(
        layoutDirection: LayoutDirection,
        density: Density,
        enabled: Boolean? = null,
        position: MapControlPosition = MapControlPosition.TopStart()
    ): LogoSettings =
        LogoSettings(
            enabled, position.asGravity(), position.asMarginInsets(layoutDirection, density))
  }
}
