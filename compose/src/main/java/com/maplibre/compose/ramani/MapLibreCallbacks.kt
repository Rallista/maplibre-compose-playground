package com.maplibre.compose.ramani

import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap

enum class MapGestureType {
    TAP,
    LONG_PRESS
}

data class MapGestureContext(

    /**
     * The type of gesture.
     */
    val type: MapGestureType,

    /**
     * The coordinate of the gesture.
     */
    val coordinate: LatLng,

    // TODO: Bundle other relevant gesture context information here.
)

/**
 * Optionally adds gesture callbacks to the map.
 *
 * @param onTapGestureCallback The callback for a tap gesture.
 * @param onLongPressGestureCallback The callback for a long press gesture.
 */
internal fun MapboxMap.setupEventCallbacks(
    onTapGestureCallback: ((MapGestureContext) -> Unit)? = null,
    onLongPressGestureCallback: ((MapGestureContext) -> Unit)? = null,
) {
    onTapGestureCallback?.let {
        this.addOnMapClickListener { point ->
            onTapGestureCallback?.invoke(
                MapGestureContext(
                    MapGestureType.TAP,
                    LatLng(point.latitude, point.longitude)
                )
            )
            true
        }
    }

    onLongPressGestureCallback?.let {
        this.addOnMapLongClickListener { point ->
            onLongPressGestureCallback?.invoke(
                MapGestureContext(
                    MapGestureType.LONG_PRESS,
                    LatLng(point.latitude, point.longitude)
                )
            )
            true
        }
    }
}