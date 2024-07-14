package com.maplibre.compose.settings

import android.os.Parcelable
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
) : Parcelable
