package com.maplibre.compose.camera

import com.maplibre.compose.camera.extensions.getDirection
import com.maplibre.compose.camera.extensions.getPitch
import com.maplibre.compose.camera.extensions.getZoom
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MapViewCameraGetterTests {

  @Test
  fun `test MapViewCamera getters defaults`() {
    val mapViewCamera = MapViewCamera()

    assertEquals(mapViewCamera.getZoom(), 10.0, 0.01)
    assertEquals(mapViewCamera.getPitch(), 0.0, 0.01)
    assertEquals(mapViewCamera.getDirection()!!, 0.0, 0.01)
  }

  @Test
  fun `test MapViewCamera getters TrackingUserLocationWithBearing`() {
    val mapViewCamera = MapViewCamera.TrackingUserLocationWithBearing()

    assertEquals(mapViewCamera.getZoom(), 10.0, 0.01)
    assertEquals(mapViewCamera.getPitch(), 0.0, 0.01)
    assertNull(mapViewCamera.getDirection())
  }

  @Test
  fun `test MapViewCamera getters Center custom`() {
    val mapViewCamera =
        MapViewCamera.Centered(
            latitude = 0.0, longitude = 0.0, zoom = 15.5, pitch = 33.3, direction = 155.5)

    assertEquals(mapViewCamera.getZoom(), 15.5, 0.01)
    assertEquals(mapViewCamera.getPitch(), 33.3, 0.01)
    assertEquals(mapViewCamera.getDirection()!!, 155.5, 0.01)
  }
}
