package com.maplibre.compose.camera

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CameraStateTest {

  // MARK: Equality tests

  @Test
  fun `test CameraState Centered equality`() {
    assertEquals(CameraState.Centered(0.0, 0.0), CameraState.Centered(0.0, 0.0))
    assertEquals(
        CameraState.Centered(0.0, 0.0, 10.0, 0.0, 0.0),
        CameraState.Centered(0.0, 0.0, 10.0, 0.0, 0.0))
    assertEquals(
        CameraState.Centered(0.0, 0.0, 0.0, 10.0, 0.0),
        CameraState.Centered(0.0, 0.0, 0.0, 10.0, 0.0))
    assertEquals(
        CameraState.Centered(0.0, 0.0, 0.0, 0.0, 10.0),
        CameraState.Centered(0.0, 0.0, 0.0, 0.0, 10.0))
    assertEquals(
        CameraState.Centered(10.0, 20.0, 0.0, 0.0, 0.0),
        CameraState.Centered(10.0, 20.0, 0.0, 0.0, 0.0))
  }

  @Test
  fun `test CameraState Centered lat lng not equals`() {
    assertNotEquals(CameraState.Centered(10.0, 0.0), CameraState.Centered(0.0, 0.0))
    assertNotEquals(CameraState.Centered(0.0, 10.0), CameraState.Centered(0.0, 0.0))
  }

  @Test
  fun `test CameraState centered not equals`() {
    assertNotEquals(CameraState.Centered(0.0, 0.0, 15.0, 0.0, 0.0), CameraState.Centered(0.0, 0.0))
    assertNotEquals(CameraState.Centered(0.0, 0.0, 0.0, 10.0, 0.0), CameraState.Centered(0.0, 0.0))
    assertNotEquals(CameraState.Centered(0.0, 0.0, 0.0, 0.0, 10.0), CameraState.Centered(0.0, 0.0))

    assertNotEquals(CameraState.Centered(0.0, 0.0), CameraState.TrackingUserLocation())
    assertNotEquals(CameraState.Centered(0.0, 0.0), CameraState.TrackingUserLocationWithBearing())
  }

  @Test
  fun `test CameraState type not equals`() {
    assertNotEquals(CameraState.Centered(0.0, 0.0), CameraState.TrackingUserLocation())
    assertNotEquals(CameraState.Centered(0.0, 0.0), CameraState.TrackingUserLocationWithBearing())
    assertNotEquals(
        CameraState.TrackingUserLocation(), CameraState.TrackingUserLocationWithBearing())
  }

  // MARK: HashCode tests

  @Test
  fun `test CameraState Centered hashCode`() {
    assertEquals(
        CameraState.Centered(0.0, 0.0).hashCode(), CameraState.Centered(0.0, 0.0).hashCode())
    assertEquals(
        CameraState.Centered(0.0, 0.0, 10.0, 0.0, 0.0).hashCode(),
        CameraState.Centered(0.0, 0.0, 10.0, 0.0, 0.0).hashCode())
    assertEquals(
        CameraState.Centered(0.0, 0.0, 0.0, 10.0, 0.0).hashCode(),
        CameraState.Centered(0.0, 0.0, 0.0, 10.0, 0.0).hashCode())
    assertEquals(
        CameraState.Centered(0.0, 0.0, 0.0, 0.0, 10.0).hashCode(),
        CameraState.Centered(0.0, 0.0, 0.0, 0.0, 10.0).hashCode())
    assertEquals(
        CameraState.Centered(10.0, 20.0, 0.0, 0.0, 0.0).hashCode(),
        CameraState.Centered(10.0, 20.0, 0.0, 0.0, 0.0).hashCode())
  }

  @Test
  fun `test CameraState type hashCode`() {
    assertNotEquals(
        CameraState.Centered(0.0, 0.0).hashCode(), CameraState.TrackingUserLocation().hashCode())
    assertNotEquals(
        CameraState.Centered(0.0, 0.0).hashCode(),
        CameraState.TrackingUserLocationWithBearing().hashCode())
    assertNotEquals(
        CameraState.TrackingUserLocation().hashCode(),
        CameraState.TrackingUserLocationWithBearing().hashCode())
  }

  // MARK: toString tests

  @Test
  fun `test CameraState toString`() {
    val centered = CameraState.Centered(0.0, 0.0)
    val trackingUserLocation = CameraState.TrackingUserLocation()
    val trackingUserLocationWithBearing = CameraState.TrackingUserLocationWithBearing()
    assertEquals(
        centered.toString(),
        "Centered(latitude=0.0, longitude=0.0, zoom=10.0, pitch=0.0, direction=0.0, motion=Ease(animationDurationMs=1000))")
    assertEquals(
        trackingUserLocation.toString(),
        "TrackingUserLocation(zoom=10.0, pitch=0.0, direction=0.0)")
    assertEquals(
        trackingUserLocationWithBearing.toString(),
        "TrackingUserLocationWithBearing(zoom=10.0, pitch=0.0)")
  }
}
