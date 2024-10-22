package com.maplibre.compose.camera.extensions

import com.maplibre.compose.camera.CameraState
import com.maplibre.compose.camera.MapViewCamera

/**
 * Get the camera's current pitch.
 *
 * @return the current pitch.
 */
fun MapViewCamera.getPitch(): Double {
  return when (this.state) {
    is CameraState.Centered -> this.state.pitch
    is CameraState.TrackingUserLocation -> this.state.pitch
    is CameraState.TrackingUserLocationWithBearing -> this.state.pitch
  }
}

/**
 * Set the camera's pitch.
 *
 * This function will ensure that the pitch is within the min and max pitch values.
 *
 * ```kotlin
 * val camera = remember { mutableStateOf(MapViewCamera()) }
 * camera.value = camera.value.incrementZoom(1.0)
 * ```
 *
 * @param pitch the new pitch.
 * @return the updated camera. You must set your camera.value to this return value.
 */
fun MapViewCamera.setPitch(pitch: Double): MapViewCamera {
  when (this.state) {
    is CameraState.Centered -> {
      return this.copy(
        state =
        CameraState.Centered(
          latitude = this.state.latitude,
          longitude = this.state.longitude,
          zoom = this.state.zoom,
          pitch = pitch,
          direction = this.state.direction))
    }
    is CameraState.TrackingUserLocation -> {
      return this.copy(
        state =
        CameraState.TrackingUserLocation(
          zoom = this.state.zoom, pitch = pitch, direction = this.state.direction))
    }
    is CameraState.TrackingUserLocationWithBearing -> {
      return this.copy(
        state =
        CameraState.TrackingUserLocationWithBearing(zoom = this.state.zoom, pitch = pitch))
    }
  }
}