package com.maplibre.compose.camera.extensions

import com.maplibre.compose.camera.CameraState
import com.maplibre.compose.camera.MapViewCamera

/**
 * Get the camera's current direction.
 *
 * Direction is null while tracking user location with bearing. In this case, the MapView is always
 * oriented to the user's current direction of travel.
 *
 * @return the current direction.
 */
fun MapViewCamera.getDirection(): Double? {
  return when (this.state) {
    is CameraState.Centered -> this.state.direction
    is CameraState.BoundingBox -> this.state.direction
    is CameraState.TrackingUserLocation -> this.state.direction
    is CameraState.TrackingUserLocationWithBearing -> null
  }
}

/**
 * Set the camera's direction (0 to 360 degrees).
 *
 * This function will ensure that the direction is within the min and max direction values.
 *
 * ```kotlin
 * val camera = rememberSaveableMapViewCamera()
 * camera.value = camera.value.setDirection(90.0)
 * ```
 *
 * @param direction the new direction.
 * @return the updated camera. You must set your camera.value to this return value.
 */
fun MapViewCamera.setDirection(direction: Double): MapViewCamera {
  when (this.state) {
    is CameraState.Centered -> {
      return this.copy(
          state =
              CameraState.Centered(
                  latitude = this.state.latitude,
                  longitude = this.state.longitude,
                  zoom = this.state.zoom,
                  pitch = this.state.pitch,
                  direction = direction,
                  motion = this.state.motion))
    }
    is CameraState.BoundingBox -> {
      return this.copy(
          state =
              CameraState.BoundingBox(
                  bounds = this.state.bounds,
                  pitch = this.state.pitch,
                  direction = direction,
                  motion = this.state.motion))
    }
    is CameraState.TrackingUserLocation -> {
      return this.copy(
          state =
              CameraState.TrackingUserLocation(
                  zoom = this.state.zoom, pitch = this.state.pitch, direction = direction))
    }
    is CameraState.TrackingUserLocationWithBearing -> {
      // No change because direction is ignored.
      return this
    }
  }
}
