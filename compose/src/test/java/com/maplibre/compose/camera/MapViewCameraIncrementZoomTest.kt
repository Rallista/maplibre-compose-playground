package com.maplibre.compose.camera

import com.maplibre.compose.camera.extensions.incrementZoom
import org.junit.Assert.assertEquals
import org.junit.Test

class MapViewCameraIncrementZoomTest {

  @Test
  fun `test MapViewCamera Centered incrementZoom`() {
    val mapViewCamera = MapViewCamera()
    val newCamera = mapViewCamera.incrementZoom(1.0)

    when (newCamera.state) {
      is CameraState.Centered -> {
        val centered = newCamera.state as CameraState.Centered
        assertEquals(centered.zoom, 11.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.Centered")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocation incrementZoom`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocation()
    val newCamera = mapViewCamera.incrementZoom(1.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocation -> {
        val value = newCamera.state as CameraState.TrackingUserLocation
        assertEquals(value.zoom, 11.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocation")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocationWithBearing incrementZoom`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocationWithBearing()
    val newCamera = mapViewCamera.incrementZoom(1.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocationWithBearing -> {
        val value = newCamera.state as CameraState.TrackingUserLocationWithBearing
        assertEquals(value.zoom, 11.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocationWithBearing")
    }
  }
}
