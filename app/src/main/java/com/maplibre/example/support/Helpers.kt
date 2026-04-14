package com.maplibre.example.support

import org.maplibre.compose.camera.CameraPosition
import org.maplibre.spatialk.geojson.Position

// --- New library helpers ---

// FLAG: The old library had a MapViewCamera sealed class with states:
// Centered, TrackingUserLocation, TrackingUserLocationWithBearing, BoundingBox.
// The new library has no camera mode concept. We define a simple enum to represent
// different demo camera positions. TrackingUserLocation and TrackingUserLocationWithBearing
// modes are not directly supported by the new library's CameraState — location tracking
// is handled separately via LocationTrackingEffect.
enum class CameraMode {
  Centered,
  TrackingUserLocation,
  TrackingUserLocationWithBearing,
  BoundingBox,
}

internal fun getNextCameraMode(currentMode: CameraMode): CameraMode {
  return when (currentMode) {
    CameraMode.Centered -> CameraMode.TrackingUserLocation
    CameraMode.TrackingUserLocation -> CameraMode.TrackingUserLocationWithBearing
    CameraMode.TrackingUserLocationWithBearing -> CameraMode.BoundingBox
    CameraMode.BoundingBox -> CameraMode.Centered
  }
}

internal fun getCameraPositionForMode(mode: CameraMode): CameraPosition {
  return when (mode) {
    CameraMode.Centered ->
        CameraPosition(target = Position(longitude = -2.9779, latitude = 53.4106))
    // FLAG: TrackingUserLocation used to automatically follow the user's location.
    // In the new library, this would be handled by LocationTrackingEffect.
    // Here we just set a representative camera position with tilt.
    CameraMode.TrackingUserLocation ->
        CameraPosition(
            target = Position(longitude = -18.529602, latitude = 66.137331),
            zoom = 18.0,
            tilt = 45.0)
    CameraMode.TrackingUserLocationWithBearing ->
        CameraPosition(
            target = Position(longitude = -18.529602, latitude = 66.137331),
            zoom = 18.0,
            tilt = 45.0,
            bearing = 90.0)
    // FLAG: BoundingBox camera mode (MapViewCamera.BoundingBox(LatLngBounds)) has no direct
    // equivalent as a CameraPosition. In the new library, use cameraState.animateTo(boundingBox).
    // Approximating with a centered view of the bounding box area.
    CameraMode.BoundingBox ->
        CameraPosition(
            target = Position(longitude = 8.2326, latitude = 46.8039), zoom = 6.0)
  }
}

// --- Old library helpers (used by car app screens that still depend on the old compose module) ---

internal fun getNextCamera(
    currentState: com.maplibre.compose.camera.CameraState
): com.maplibre.compose.camera.MapViewCamera {
  return when (currentState) {
    is com.maplibre.compose.camera.CameraState.Centered ->
        com.maplibre.compose.camera.MapViewCamera.TrackingUserLocation(zoom = 18.0, pitch = 45.0)
    is com.maplibre.compose.camera.CameraState.TrackingUserLocation ->
        com.maplibre.compose.camera.MapViewCamera.TrackingUserLocationWithBearing(
            zoom = 18.0, pitch = 45.0)
    is com.maplibre.compose.camera.CameraState.TrackingUserLocationWithBearing ->
        com.maplibre.compose.camera.MapViewCamera.BoundingBox(
            org.maplibre.android.geometry.LatLngBounds.from(
                47.8308275417, 10.4427014502, 45.7769477403, 6.02260949059))
    is com.maplibre.compose.camera.CameraState.BoundingBox ->
        com.maplibre.compose.camera.MapViewCamera.Centered(53.4106, -2.9779)
  }
}
