package com.maplibre.compose.camera

import com.maplibre.compose.camera.extensions.setZoom
import org.junit.Assert.assertEquals
import org.junit.Test

class MapViewCameraZoomOperationTest {

  @Test
  fun `test MapViewCamera Centered setZoom`() {
    val mapViewCamera = MapViewCamera()
    val newCamera = mapViewCamera.setZoom(13.0)

    when (newCamera.state) {
      is CameraState.Centered -> {
        val centered = newCamera.state as CameraState.Centered
        assertEquals(centered.zoom, 13.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.Centered")
    }
  }

  @Test
  fun `test MapViewCamera Centered setZoom with invalid min zoom`() {
    val mapViewCamera = MapViewCamera()
    val newCamera = mapViewCamera.setZoom(-1.0)

    when (newCamera.state) {
      is CameraState.Centered -> {
        val centered = newCamera.state as CameraState.Centered
        assertEquals(centered.zoom, 0.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.Centered")
    }
  }

  @Test
  fun `test MapViewCamera Centered setZoom with invalid max zoom`() {
    val mapViewCamera = MapViewCamera()
    val newCamera = mapViewCamera.setZoom(25.0)

    when (newCamera.state) {
      is CameraState.Centered -> {
        val centered = newCamera.state as CameraState.Centered
        assertEquals(centered.zoom, 24.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.Centered")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocation setZoom`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocation()
    val newCamera = mapViewCamera.setZoom(13.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocation -> {
        val value = newCamera.state as CameraState.TrackingUserLocation
        assertEquals(value.zoom, 13.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocation")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocation setZoom with invalid min zoom`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocation()
    val newCamera = mapViewCamera.setZoom(-1.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocation -> {
        val value = newCamera.state as CameraState.TrackingUserLocation
        assertEquals(value.zoom, 0.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocation")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocation setZoom with invalid max zoom`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocation()
    val newCamera = mapViewCamera.setZoom(25.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocation -> {
        val value = newCamera.state as CameraState.TrackingUserLocation
        assertEquals(value.zoom, 24.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocation")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocationWithBearing setZoom`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocationWithBearing()
    val newCamera = mapViewCamera.setZoom(13.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocationWithBearing -> {
        val value = newCamera.state as CameraState.TrackingUserLocationWithBearing
        assertEquals(value.zoom, 13.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocationWithBearing")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocationWithBearing setZoom with invalid min zoom`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocationWithBearing()
    val newCamera = mapViewCamera.setZoom(-1.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocationWithBearing -> {
        val value = newCamera.state as CameraState.TrackingUserLocationWithBearing
        assertEquals(value.zoom, 0.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocationWithBearing")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocationWithBearing setZoom with invalid max zoom`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocationWithBearing()
    val newCamera = mapViewCamera.setZoom(25.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocationWithBearing -> {
        val value = newCamera.state as CameraState.TrackingUserLocationWithBearing
        assertEquals(value.zoom, 24.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocationWithBearing")
    }
  }
}
