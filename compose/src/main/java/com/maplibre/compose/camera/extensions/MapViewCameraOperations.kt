package com.maplibre.compose.camera.extensions

import com.maplibre.compose.camera.CameraState
import com.maplibre.compose.camera.MapViewCamera
import kotlin.math.roundToInt

/**
 * Set the camera's zoom level.
 *
 * This function will ensure that the zoom level is within the min and max zoom levels.
 *
 * ```kotlin
 * val camera = remember { mutableStateOf(MapViewCamera()) }
 * camera.value = camera.value.setZoom(10.0)
 * ```
 *
 * @param zoom the new zoom level.
 * @return the updated camera. You must set your camera.value to this return value.
 */
fun MapViewCamera.setZoom(zoom: Double): MapViewCamera {
  when (this.state) {
    is CameraState.Centered -> {
      val centered = this.state as CameraState.Centered
      return this.copy(
          state =
              CameraState.Centered(
                  latitude = centered.latitude,
                  longitude = centered.longitude,
                  zoom = validZoom(zoom),
                  pitch = validPitch(centered.pitch),
                  direction = centered.direction))
    }
    is CameraState.TrackingUserLocation -> {
      val trackingUserLocation = this.state as CameraState.TrackingUserLocation
      return this.copy(
          state =
              CameraState.TrackingUserLocation(
                  zoom = validZoom(zoom),
                  pitch = validPitch(trackingUserLocation.pitch),
                  direction = trackingUserLocation.direction))
    }
    is CameraState.TrackingUserLocationWithBearing -> {
      val trackingUserLocationWithBearing =
          this.state as CameraState.TrackingUserLocationWithBearing
      return this.copy(
          state =
              CameraState.TrackingUserLocationWithBearing(
                  zoom = validZoom(zoom),
                  pitch = validPitch(trackingUserLocationWithBearing.pitch)))
    }
  }
}

/**
 * Increment the camera's zoom level.
 *
 * This function will ensure that the zoom level is within the min and max zoom levels.
 *
 * ```kotlin
 * val camera = remember { mutableStateOf(MapViewCamera()) }
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
        is CameraState.Centered -> {
          val centered = this.state as CameraState.Centered
          centered.zoom
        }
        is CameraState.TrackingUserLocation -> {
          val trackingUserLocation = this.state as CameraState.TrackingUserLocation
          trackingUserLocation.zoom
        }
        is CameraState.TrackingUserLocationWithBearing -> {
          val trackingUserLocationWithBearing =
              this.state as CameraState.TrackingUserLocationWithBearing
          trackingUserLocationWithBearing.zoom
        }
      }

  val currentZoom = if (rounded) currentRawZoom.roundToInt().toDouble() else currentRawZoom
  return this.setZoom(currentZoom + increment)
}
