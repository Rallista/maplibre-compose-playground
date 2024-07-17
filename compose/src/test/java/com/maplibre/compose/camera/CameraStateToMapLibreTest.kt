package com.maplibre.compose.camera

import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.maplibre.compose.camera.extensions.needsUpdate
import com.maplibre.compose.camera.extensions.toCameraMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CameraStateToMapLibreTest {

  @Test
  fun `test CameraState to CameraMode`() {
    assertEquals(CameraState.Centered(0.0, 0.0).toCameraMode(), CameraMode.NONE)
    assertEquals(CameraState.TrackingUserLocation().toCameraMode(), CameraMode.TRACKING)
    assertEquals(
        CameraState.TrackingUserLocationWithBearing().toCameraMode(), CameraMode.TRACKING_GPS)
  }

  @Test
  fun `test CameraState Centered defaults needsUpdate`() {
    val cameraState = CameraState.Centered(0.0, 0.0)
    val mapCurrentCameraMode = CameraMode.NONE
    val mapCurrentZoom = MapViewCameraDefaults.ZOOM
    val mapCurrentPitch = MapViewCameraDefaults.PITCH
    assertTrue(cameraState.needsUpdate(mapCurrentCameraMode, mapCurrentZoom, mapCurrentPitch))
  }

  @Test
  fun `test CameraState Centered needsUpdate`() {
    val cameraState = CameraState.Centered(0.0, 0.0)
    val mapCurrentCameraMode = CameraMode.NONE
    val mapCurrentZoom = 15.0
    val mapCurrentPitch = 32.0
    assertTrue(cameraState.needsUpdate(mapCurrentCameraMode, mapCurrentZoom, mapCurrentPitch))
  }

  @Test
  fun `test CameraState TrackingUserLocation defaults needsUpdate`() {
    val cameraState = CameraState.TrackingUserLocation()
    val mapCurrentCameraMode = CameraMode.TRACKING
    val mapCurrentZoom = MapViewCameraDefaults.ZOOM
    val mapCurrentPitch = MapViewCameraDefaults.PITCH
    assertFalse(cameraState.needsUpdate(mapCurrentCameraMode, mapCurrentZoom, mapCurrentPitch))
  }

  @Test
  fun `test CameraState TrackingUserLocation needsUpdate because cameraMode`() {
    val cameraState = CameraState.TrackingUserLocation()
    val mapCurrentCameraMode = CameraMode.NONE
    val mapCurrentZoom = MapViewCameraDefaults.ZOOM
    val mapCurrentPitch = MapViewCameraDefaults.PITCH
    assertTrue(cameraState.needsUpdate(mapCurrentCameraMode, mapCurrentZoom, mapCurrentPitch))
  }

  @Test
  fun `test CameraState TrackingUserLocation needsUpdate because zoom`() {
    val cameraState = CameraState.TrackingUserLocation()
    val mapCurrentCameraMode = CameraMode.TRACKING
    val mapCurrentZoom = 12.0
    val mapCurrentPitch = MapViewCameraDefaults.PITCH
    assertTrue(cameraState.needsUpdate(mapCurrentCameraMode, mapCurrentZoom, mapCurrentPitch))
  }

  @Test
  fun `test CameraState TrackingUserLocation needsUpdate because pitch`() {
    val cameraState = CameraState.TrackingUserLocation()
    val mapCurrentCameraMode = CameraMode.TRACKING
    val mapCurrentZoom = MapViewCameraDefaults.ZOOM
    val mapCurrentPitch = 55.0
    assertTrue(cameraState.needsUpdate(mapCurrentCameraMode, mapCurrentZoom, mapCurrentPitch))
  }

  @Test
  fun `test CameraState TrackingUserLocationWithBearing defaults needsUpdate`() {
    val cameraState = CameraState.TrackingUserLocationWithBearing()
    val mapCurrentCameraMode = CameraMode.TRACKING_GPS
    val mapCurrentZoom = MapViewCameraDefaults.ZOOM
    val mapCurrentPitch = MapViewCameraDefaults.PITCH
    assertFalse(cameraState.needsUpdate(mapCurrentCameraMode, mapCurrentZoom, mapCurrentPitch))
  }

  @Test
  fun `test CameraState TrackingUserLocationWithBearing needsUpdate because cameraMode`() {
    val cameraState = CameraState.TrackingUserLocationWithBearing()
    val mapCurrentCameraMode = CameraMode.NONE
    val mapCurrentZoom = MapViewCameraDefaults.ZOOM
    val mapCurrentPitch = MapViewCameraDefaults.PITCH
    assertTrue(cameraState.needsUpdate(mapCurrentCameraMode, mapCurrentZoom, mapCurrentPitch))
  }

  @Test
  fun `test CameraState TrackingUserLocationWithBearing needsUpdate because zoom`() {
    val cameraState = CameraState.TrackingUserLocationWithBearing()
    val mapCurrentCameraMode = CameraMode.TRACKING_GPS
    val mapCurrentZoom = 12.0
    val mapCurrentPitch = MapViewCameraDefaults.PITCH
    assertTrue(cameraState.needsUpdate(mapCurrentCameraMode, mapCurrentZoom, mapCurrentPitch))
  }

  @Test
  fun `test CameraState TrackingUserLocationWithBearing needsUpdate because pitch`() {
    val cameraState = CameraState.TrackingUserLocationWithBearing()
    val mapCurrentCameraMode = CameraMode.TRACKING_GPS
    val mapCurrentZoom = MapViewCameraDefaults.ZOOM
    val mapCurrentPitch = 55.0
    assertTrue(cameraState.needsUpdate(mapCurrentCameraMode, mapCurrentZoom, mapCurrentPitch))
  }
}
