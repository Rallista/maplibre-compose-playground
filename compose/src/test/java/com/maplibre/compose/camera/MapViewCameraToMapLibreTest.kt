package com.maplibre.compose.camera

import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.maplibre.compose.camera.extensions.toCameraPosition
import com.maplibre.compose.camera.models.CameraPadding
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Test

class MapViewCameraToMapLibreTest {
  val map = mockk<MapboxMap>()

  @Test
  fun `test MapViewCamera Centered toMapLibre`() {
    val mapViewCamera = MapViewCamera.Centered(0.0, 0.0)
    val cameraPosition =
        mapViewCamera.toCameraPosition(map) ?: return fail("Excepted to get a camera position")

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
    val cameraPosition =
        mapViewCamera.toCameraPosition(map) ?: return fail("Excepted to get a camera position")

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
    val cameraPosition =
        mapViewCamera.toCameraPosition(map) ?: return fail("Excepted to get a camera position")

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

  @Test
  fun `test MapViewCamera BoundingBox toMapLibre`() {
    val mockCameraPosition =
        CameraPosition.Builder()
            .target(LatLng(0.0, 0.0))
            .zoom(0.0)
            .bearing(0.0)
            .padding(0.0, 0.0, 0.0, 0.0)
            .build()
    every {
      map.getCameraForLatLngBounds(LatLngBounds.world(), intArrayOf(0, 0, 0, 0), 0.0, 0.0)
    } returns mockCameraPosition

    val mapViewCamera = MapViewCamera.BoundingBox(LatLngBounds.world())
    val cameraPosition = mapViewCamera.toCameraPosition(map)

    assertEquals(mockCameraPosition, cameraPosition)
  }

  @Test
  fun `test MapViewCamera BoundingBox with padding toMapLibre`() {
    val mockCameraPosition =
        CameraPosition.Builder()
            .target(LatLng(0.0, 0.0))
            .zoom(0.0)
            .bearing(0.0)
            .padding(1.0, 2.0, 3.0, 4.0)
            .build()
    every {
      // NOTE: This line is subtly critical for the test.
      // The order of the int array is undocumented,
      // so part of this test's purpose is to ensure that the padding dimensions are passed
      // in the correct order.
      map.getCameraForLatLngBounds(LatLngBounds.world(), intArrayOf(1, 2, 3, 4), 0.0, 0.0)
    } returns mockCameraPosition

    val mapViewCamera =
        MapViewCamera.BoundingBox(LatLngBounds.world(), padding = CameraPadding(1.0, 2.0, 3.0, 4.0))
    val cameraPosition = mapViewCamera.toCameraPosition(map)

    assertEquals(mockCameraPosition, cameraPosition)
  }
}
