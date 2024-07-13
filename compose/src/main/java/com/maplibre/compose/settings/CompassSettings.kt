package com.maplibre.compose.settings

import android.os.Parcelable
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
) : Parcelable
