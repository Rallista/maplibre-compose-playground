package com.maplibre.compose.ramani

import android.graphics.PointF
import android.util.Log
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap

enum class MapGestureType {
  TAP,
  LONG_PRESS
}

data class MapGestureContext(

    /** The screen location of the gesture. */
    val screenLocation: PointF,

    /** The type of gesture. */
    val type: MapGestureType,

    /** The coordinate of the gesture. */
    val coordinate: LatLng,

    // TODO: Bundle other relevant gesture context information here.
)

/**
 * Optionally adds gesture callbacks to the map.
 *
 * @param onTapGestureCallback The callback for a tap gesture.
 * @param onLongPressGestureCallback The callback for a long press gesture.
 */
internal fun MapLibreMap.setupEventCallbacks(
    onTapGestureCallback: ((MapGestureContext) -> Unit)? = null,
    onLongPressGestureCallback: ((MapGestureContext) -> Unit)? = null,
) {
  onTapGestureCallback?.let {
    this.addOnMapClickListener { point ->
      val screenLocation = projection.toScreenLocation(point)
      val features = queryRenderedFeatures(screenLocation)

      if (features.isNotEmpty()) {
        Log.d(
            "MapLibre",
            "Ignoring MapView tap gesture because feature exists. Use feature tap gesture instead.")
        return@addOnMapClickListener false
      }

      onTapGestureCallback.invoke(
          MapGestureContext(
              screenLocation, MapGestureType.TAP, LatLng(point.latitude, point.longitude)))
      true
    }
  }

  onLongPressGestureCallback?.let {
    this.addOnMapLongClickListener { point ->
      val screenLocation = projection.toScreenLocation(point)
      val features = queryRenderedFeatures(screenLocation)

      if (features.isNotEmpty()) {
        Log.d(
            "MapLibre",
            "Ignoring MapView long press gesture because feature exists. Use feature tap gesture instead.")
        return@addOnMapLongClickListener false
      }

      onLongPressGestureCallback.invoke(
          MapGestureContext(
              screenLocation, MapGestureType.LONG_PRESS, LatLng(point.latitude, point.longitude)))
      true
    }
  }
}
