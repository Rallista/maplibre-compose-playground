package com.maplibre.compose.settings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Customize the logo on the map.
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
) : Parcelable
