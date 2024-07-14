package com.maplibre.compose.camera.extensions

import com.maplibre.compose.camera.CameraState
import com.maplibre.compose.camera.MapViewCamera
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng

/**
 * Converts a [MapViewCamera] to a MapLibre [CameraPosition].
 *
 * @return The MapLibre [CameraPosition] equivalent of the [MapViewCamera].
 */
internal fun MapViewCamera.toCameraPosition(): CameraPosition {
  val builder = CameraPosition.Builder()

  when (this.state) {
    is CameraState.Centered -> {
      val value = this.state as CameraState.Centered
      builder
          .target(LatLng(value.latitude, value.longitude))
          .zoom(value.zoom)
          .tilt(value.pitch)
          .bearing(value.direction)
    }
    is CameraState.TrackingUserLocation -> {
      val value = this.state as CameraState.TrackingUserLocation
      builder.zoom(value.zoom).tilt(value.pitch).bearing(value.direction)
    }
    is CameraState.TrackingUserLocationWithBearing -> {
      val value = this.state as CameraState.TrackingUserLocationWithBearing
      builder.zoom(value.zoom).tilt(value.pitch)
    }
  }

  padding.let { builder.padding(it.start, it.top, it.end, it.bottom) }
  return builder.build()
}
