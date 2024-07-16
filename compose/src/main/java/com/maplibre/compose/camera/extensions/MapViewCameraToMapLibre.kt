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
      builder
          .target(LatLng(this.state.latitude, this.state.longitude))
          .zoom(this.state.zoom)
          .tilt(this.state.pitch)
          .bearing(this.state.direction)
    }
    is CameraState.TrackingUserLocation -> {
      builder.zoom(this.state.zoom).tilt(this.state.pitch).bearing(this.state.direction)
    }
    is CameraState.TrackingUserLocationWithBearing -> {
      builder.zoom(this.state.zoom).tilt(this.state.pitch)
    }
  }

  padding.let { builder.padding(it.start, it.top, it.end, it.bottom) }
  return builder.build()
}
