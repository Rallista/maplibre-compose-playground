package com.maplibre.auto

import com.maplibre.compose.camera.CameraState
import com.maplibre.compose.camera.MapViewCamera
import org.maplibre.android.geometry.LatLngBounds

internal fun getNextCamera(currentState: CameraState): MapViewCamera {
  return when (currentState) {
    is CameraState.Centered -> MapViewCamera.TrackingUserLocation(zoom = 18.0, pitch = 45.0)
    is CameraState.TrackingUserLocation ->
        MapViewCamera.TrackingUserLocationWithBearing(zoom = 18.0, pitch = 45.0)
    is CameraState.TrackingUserLocationWithBearing ->
        MapViewCamera.BoundingBox(
            LatLngBounds.Companion.from(47.8308275417, 10.4427014502, 45.7769477403, 6.02260949059))
    is CameraState.BoundingBox -> MapViewCamera.Centered(53.4106, -2.9779)
  }
}
