package com.maplibre.compose.camera

import com.maplibre.compose.camera.extensions.toCameraPosition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MapViewCameraToMapLibreTest {

  @Test
  fun `test MapViewCamera Centered toMapLibre`() {
    val mapViewCamera = MapViewCamera.Centered(0.0, 0.0)
    val cameraPosition = mapViewCamera.toCameraPosition()

    assertEquals(0.0, cameraPosition.target!!.latitude, 0.01)
    assertEquals(0.0, cameraPosition.target!!.longitude, 0.01)
    assertEquals(10.0, cameraPosition.zoom, 0.01)
    assertEquals(0.0, cameraPosition.bearing, 0.1)
    assertEquals(0.0, cameraPosition.tilt, 0.1)
    assertEquals(0.0, cameraPosition.padding!![0], 0.1)
    assertEquals(0.0, cameraPosition.padding!![1], 0.1)
    assertEquals(0.0, cameraPosition.padding!![2], 0.1)
    assertEquals(0.0, cameraPosition.padding!![3], 0.1)
    assertNull(cameraPosition.padding!!.getOrNull(4))
  }

  @Test
  fun `test MapViewCamera TrackingUserLocation toMapLibre`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocation()
    val cameraPosition = mapViewCamera.toCameraPosition()

    assertNull(cameraPosition.target)
    assertEquals(10.0, cameraPosition.zoom, 0.01)
    assertEquals(0.0, cameraPosition.bearing, 0.1)
    assertEquals(0.0, cameraPosition.tilt, 0.1)
    assertEquals(0.0, cameraPosition.padding!![0], 0.1)
    assertEquals(0.0, cameraPosition.padding!![1], 0.1)
    assertEquals(0.0, cameraPosition.padding!![2], 0.1)
    assertEquals(0.0, cameraPosition.padding!![3], 0.1)
    assertNull(cameraPosition.padding!!.getOrNull(4))
  }

  @Test
  fun `test MapViewCamera TrackingUserLocationWithBearing toMapLibre`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocationWithBearing()
    val cameraPosition = mapViewCamera.toCameraPosition()

    assertNull(cameraPosition.target)
    assertEquals(10.0, cameraPosition.zoom, 0.01)
    assertEquals(-1.0, cameraPosition.bearing, 0.1) // -1.0 is probably the native unset value?
    assertEquals(0.0, cameraPosition.tilt, 0.1)
    assertEquals(0.0, cameraPosition.padding!![0], 0.1)
    assertEquals(0.0, cameraPosition.padding!![1], 0.1)
    assertEquals(0.0, cameraPosition.padding!![2], 0.1)
    assertEquals(0.0, cameraPosition.padding!![3], 0.1)
    assertNull(cameraPosition.padding!!.getOrNull(4))
  }
}
