package com.maplibre.compose.settings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Customize the MapView's UiSetting controls on the map. This allows optional adjustment from the
 * default settings. Any value that is null is not applied/modified on the map for this and all
 * values in the settings classes.
 *
 * @param attribution The settings for the attribution control.
 * @param compass The settings for the compass control.
 * @param logo The settings for the logo control.
 */
@Parcelize
data class MapControls(
    val attribution: AttributionSettings? = null,
    val compass: CompassSettings? = null,
    val logo: LogoSettings? = null
) : Parcelable
