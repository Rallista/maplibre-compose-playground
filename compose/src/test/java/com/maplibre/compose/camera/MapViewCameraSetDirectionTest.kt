package com.maplibre.compose.camera

import com.maplibre.compose.camera.extensions.setDirection
import org.junit.Assert.assertEquals
import org.junit.Test

class MapViewCameraSetDirectionTest {

  @Test
  fun `test MapViewCamera Centered setDirection`() {
    val mapViewCamera = MapViewCamera()
    val newCamera = mapViewCamera.setDirection(99.9)

    when (newCamera.state) {
      is CameraState.Centered -> {
        val centered = newCamera.state as CameraState.Centered
        assertEquals(99.9, centered.direction, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.Centered")
    }
  }

  @Test
  fun `test MapViewCamera Centered setDirection with super min pitch`() {
    val mapViewCamera = MapViewCamera()
    val newCamera = mapViewCamera.setDirection(-915.0)

    when (newCamera.state) {
      is CameraState.Centered -> {
        val centered = newCamera.state as CameraState.Centered
        assertEquals(165.0, centered.direction, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.Centered")
    }
  }

  @Test
  fun `test MapViewCamera Centered setDirection with super max max pitch`() {
    val mapViewCamera = MapViewCamera()
    val newCamera = mapViewCamera.setDirection(1375.0)

    when (newCamera.state) {
      is CameraState.Centered -> {
        val centered = newCamera.state as CameraState.Centered
        assertEquals(centered.direction, 295.0, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.Centered")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocation setDirection`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocation()
    val newCamera = mapViewCamera.setDirection(99.9)

    when (newCamera.state) {
      is CameraState.TrackingUserLocation -> {
        val value = newCamera.state as CameraState.TrackingUserLocation
        assertEquals(99.9, value.direction, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocation")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocation setDirection with below min pitch`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocation()
    val newCamera = mapViewCamera.setDirection(-15.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocation -> {
        val value = newCamera.state as CameraState.TrackingUserLocation
        assertEquals(345.0, value.direction, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocation")
    }
  }

  @Test
  fun `test MapViewCamera TrackingUserLocation setDirection with invalid max pitch`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocation()
    val newCamera = mapViewCamera.setDirection(375.0)

    when (newCamera.state) {
      is CameraState.TrackingUserLocation -> {
        val value = newCamera.state as CameraState.TrackingUserLocation
        assertEquals(15.0, value.direction, 0.01)
      }
      else -> throw AssertionError("Expected MapViewCamera.TrackingUserLocation")
    }
  }
}
