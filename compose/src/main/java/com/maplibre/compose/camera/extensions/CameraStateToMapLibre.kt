package com.maplibre.compose.camera.extensions

import com.maplibre.compose.camera.CameraState
import org.maplibre.android.location.modes.CameraMode

/**
 * Converts a [CameraState] to a MapLibre [CameraMode].
 *
 * @return The MapLibre [CameraMode] equivalent of the [CameraState].
 */
internal fun CameraState.toCameraMode(): Int {
  return when (this) {
    is CameraState.Centered -> CameraMode.NONE
    is CameraState.TrackingUserLocation -> CameraMode.TRACKING
    is CameraState.TrackingUserLocationWithBearing -> CameraMode.TRACKING_GPS
  }
}

/**
 * Determines if the map's current cameraMode & cameraPosition needs to be updated.
 *
 * @param mapCurrentCameraMode the current camera mode provided by the map's
 *   [com.mapbox.mapboxsdk.location.LocationComponent].
 * @param mapCurrentZoom the current zoom level provided by the map's
 *   [com.mapbox.mapboxsdk.camera.CameraPosition].
 * @param mapCurrentPitch the current pitch provided by the map's
 *   [com.mapbox.mapboxsdk.camera.CameraPosition].
 * @return true if the camera state needs to be updated.
 */
internal fun CameraState.needsUpdate(
    mapCurrentCameraMode: Int,
    mapCurrentZoom: Double,
    mapCurrentPitch: Double
): Boolean {
  when (this) {
    is CameraState.Centered -> {
      // A new centered camera can always be updated.
      return true
    }
    is CameraState.TrackingUserLocation -> {
      return this.toCameraMode() != mapCurrentCameraMode ||
          this.zoom != mapCurrentZoom ||
          this.pitch != mapCurrentPitch
    }
    is CameraState.TrackingUserLocationWithBearing -> {
      return this.toCameraMode() != mapCurrentCameraMode ||
          this.zoom != mapCurrentZoom ||
          this.pitch != mapCurrentPitch
    }
  }
}
