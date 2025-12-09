package com.maplibre.auto

import com.maplibre.compose.camera.CameraState
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.camera.models.CameraPadding
import org.maplibre.android.geometry.LatLngBounds

internal fun getNextCamera(currentState: CameraState, padding: CameraPadding): MapViewCamera {
  return when (currentState) {
    is CameraState.Centered -> MapViewCamera.TrackingUserLocation(zoom = 10.0, pitch = 45.0)
    is CameraState.TrackingUserLocation ->
        MapViewCamera.TrackingUserLocationWithBearing(zoom = 10.0, pitch = 45.0, padding = padding)
    is CameraState.TrackingUserLocationWithBearing ->
        MapViewCamera.BoundingBox(
            LatLngBounds.Companion.from(47.8308275417, 10.4427014502, 45.7769477403, 6.02260949059),
            padding = CameraPadding(100.0, 20.0, 100.0, 20.0))
    is CameraState.BoundingBox -> MapViewCamera.Default
  }
}
