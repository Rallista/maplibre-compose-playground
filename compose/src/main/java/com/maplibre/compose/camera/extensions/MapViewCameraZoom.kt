package com.maplibre.compose.camera.extensions

import com.maplibre.compose.camera.CameraState
import com.maplibre.compose.camera.MapViewCamera
import kotlin.math.roundToInt

/**
 * Get the camera's zoom level.
 *
 * ```kotlin
 * val camera = rememberSaveableMapViewCamera()
 * val zoom = camera.value.getZoom()
 * ```
 *
 * @return the current zoom level.
 */
fun MapViewCamera.getZoom(): Double {
  return when (this.state) {
    is CameraState.Centered -> this.state.zoom
    is CameraState.BoundingBox -> TODO("This is gonna be a problem...")
    is CameraState.TrackingUserLocation -> this.state.zoom
    is CameraState.TrackingUserLocationWithBearing -> this.state.zoom
  }
}

/**
 * Set the camera's zoom level.
 *
 * This function will ensure that the zoom level is within the min and max zoom levels.
 *
 * ```kotlin
 * val camera = rememberSaveableMapViewCamera()
 * camera.value = camera.value.setZoom(10.0)
 * ```
 *
 * @param zoom the new zoom level.
 * @return the updated camera. You must set your camera.value to this return value.
 */
fun MapViewCamera.setZoom(zoom: Double): MapViewCamera {
  when (this.state) {
    is CameraState.Centered -> {
      return this.copy(
          state =
              CameraState.Centered(
                  latitude = this.state.latitude,
                  longitude = this.state.longitude,
                  zoom = zoom,
                  pitch = this.state.pitch,
                  direction = this.state.direction))
    }
    is CameraState.BoundingBox -> {
      TODO("This is gonna be a problem...")
    }
    is CameraState.TrackingUserLocation -> {
      return this.copy(
          state =
              CameraState.TrackingUserLocation(
                  zoom = zoom, pitch = this.state.pitch, direction = this.state.direction))
    }
    is CameraState.TrackingUserLocationWithBearing -> {
      return this.copy(
          state =
              CameraState.TrackingUserLocationWithBearing(zoom = zoom, pitch = this.state.pitch))
    }
  }
}

/**
 * Increment the camera's zoom level.
 *
 * This function will ensure that the zoom level is within the min and max zoom levels.
 *
 * ```kotlin
 * val camera = rememberSaveableMapViewCamera()
 * camera.value = camera.value.incrementZoom(1.0)
 * ```
 *
 * @param increment the amount to increment the zoom level by.
 * @param rounded by default, the zoom level is rounded to the nearest integer to reset from manual
 *   control. For fractional increments, consider setting this to false.
 * @return the updated camera. You must set your camera.value to this return value.
 */
fun MapViewCamera.incrementZoom(increment: Double, rounded: Boolean = true): MapViewCamera {
  val currentRawZoom =
      when (this.state) {
        is CameraState.Centered -> this.state.zoom
        is CameraState.BoundingBox -> TODO("This is gonna be a problem...")
        is CameraState.TrackingUserLocation -> this.state.zoom
        is CameraState.TrackingUserLocationWithBearing -> this.state.zoom
      }

  val currentZoom = if (rounded) currentRawZoom.roundToInt().toDouble() else currentRawZoom
  return this.setZoom(currentZoom + increment)
}
