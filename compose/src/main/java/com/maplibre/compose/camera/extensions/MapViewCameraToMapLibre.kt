package com.maplibre.compose.camera.extensions

import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.maplibre.compose.camera.CameraState
import com.maplibre.compose.camera.MapViewCamera

/**
 * Converts a [MapViewCamera] to a MapLibre [CameraPosition].
 *
 * @return The MapLibre [CameraPosition] equivalent of the [MapViewCamera].
 */
internal fun MapViewCamera.toCameraPosition(map: MapboxMap): CameraPosition? {
  val builder = CameraPosition.Builder()

  when (this.state) {
    is CameraState.Centered -> {
      builder
          .target(LatLng(this.state.latitude, this.state.longitude))
          .zoom(this.state.zoom)
          .tilt(this.state.pitch)
          .bearing(this.state.direction)
    }
    is CameraState.BoundingBox -> {
      return map.getCameraForLatLngBounds(
          this.state.bounds,
          intArrayOf(
              this.padding.top.toInt(),
              // TODO: Internally the C++ lib uses left/right, not start/end
              this.padding.start.toInt(),
              this.padding.bottom.toInt(),
              this.padding.end.toInt()),
          this.state.direction,
          this.state.pitch)
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
