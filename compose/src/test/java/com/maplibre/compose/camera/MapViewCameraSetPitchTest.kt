package com.maplibre.compose.camera

import com.maplibre.compose.camera.extensions.setPitch
import org.junit.Assert.assertEquals
import org.junit.Test

class MapViewCameraSetPitchTest {

  @Test
  fun `test MapViewCamera Centered setPitch`() {
    val mapViewCamera = MapViewCamera()
    val newCamera = mapViewCamera.setPitch(30.0)

    when (newCamera.state) {
      is CameraState.Centered -> {
        val centered = newCamera.state as CameraState.Centered
        assertEquals(centered.pitch, 30.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.Centered")
    }
  }

  @Test
  fun `test MapViewCamera Centered setPitch with invalid min pitch`() {
    val mapViewCamera = MapViewCamera()
    val newCamera = mapViewCamera.setPitch(-1.0)

    when (newCamera.state) {
      is CameraState.Centered -> {
        val centered = newCamera.state as CameraState.Centered
        assertEquals(centered.pitch, 0.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.Centered")
    }
  }

  @Test
  fun `test MapViewCamera Centered setPitch with invalid max pitch`() {
    val mapViewCamera = MapViewCamera()
    val newCamera = mapViewCamera.setPitch(61.0)

    when (newCamera.state) {
      is CameraState.Centered -> {
        val centered = newCamera.state as CameraState.Centered
        assertEquals(centered.pitch, 60.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.Centered")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocation setPitch`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocation()
    val newCamera = mapViewCamera.setPitch(30.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocation -> {
        val value = newCamera.state as CameraState.TrackingUserLocation
        assertEquals(value.pitch, 30.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocation")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocation setPitch with invalid min pitch`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocation()
    val newCamera = mapViewCamera.setPitch(-1.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocation -> {
        val value = newCamera.state as CameraState.TrackingUserLocation
        assertEquals(value.pitch, 0.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocation")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocation setPitch with invalid max pitch`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocation()
    val newCamera = mapViewCamera.setPitch(61.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocation -> {
        val value = newCamera.state as CameraState.TrackingUserLocation
        assertEquals(value.pitch, 60.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocation")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocationWithBearing setPitch`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocationWithBearing()
    val newCamera = mapViewCamera.setPitch(30.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocationWithBearing -> {
        val value = newCamera.state as CameraState.TrackingUserLocationWithBearing
        assertEquals(value.pitch, 30.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocationWithBearing")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocationWithBearing setPitch with invalid min pitch`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocationWithBearing()
    val newCamera = mapViewCamera.setPitch(-1.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocationWithBearing -> {
        val value = newCamera.state as CameraState.TrackingUserLocationWithBearing
        assertEquals(value.pitch, 0.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocationWithBearing")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocationWithBearing setPitch with invalid max pitch`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocationWithBearing()
    val newCamera = mapViewCamera.setPitch(61.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocationWithBearing -> {
        val value = newCamera.state as CameraState.TrackingUserLocationWithBearing
        assertEquals(value.pitch, 60.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocationWithBearing")
    }
  }
}