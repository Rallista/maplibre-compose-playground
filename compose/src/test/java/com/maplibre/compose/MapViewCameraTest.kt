package com.maplibre.compose

import androidx.compose.runtime.mutableStateOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import com.maplibre.compose.camera.MapViewCamera

class MapViewCameraTest {

    @Test
    fun `test MapViewCamera default equality`() {
        assertEquals(MapViewCamera.Default, MapViewCamera.Default)
        assertEquals(MapViewCamera.Default, MapViewCamera.Centered(0.0, 0.0))
        assertNotEquals(MapViewCamera.Default, MapViewCamera.TrackingUserLocation())
        assertNotEquals(MapViewCamera.Default, MapViewCamera.TrackingUserLocationWithBearing())
    }

    @Test
    fun `test MapViewCamera centered equality`() {
        assertEquals(MapViewCamera.Centered(0.0, 0.0), MapViewCamera.Centered(0.0, 0.0))
        assertNotEquals(MapViewCamera.Centered(0.0, 0.0), MapViewCamera.Centered(0.0, 1.0))
        assertNotEquals(MapViewCamera.Centered(0.0, 0.0), MapViewCamera.TrackingUserLocation())
        assertNotEquals(MapViewCamera.Centered(0.0, 0.0), MapViewCamera.TrackingUserLocationWithBearing())
    }

    @Test
    fun `test MapViewCamera tracking user location equality`() {
        assertEquals(MapViewCamera.TrackingUserLocation(), MapViewCamera.TrackingUserLocation())
        assertNotEquals(MapViewCamera.TrackingUserLocation(), MapViewCamera.TrackingUserLocationWithBearing())
        assertNotEquals(MapViewCamera.TrackingUserLocation(), MapViewCamera.Centered(0.0, 0.0))
    }

    @Test
    fun `test MapViewCamera tracking user location with bearing equality`() {
        assertEquals(MapViewCamera.TrackingUserLocationWithBearing(), MapViewCamera.TrackingUserLocationWithBearing())
        assertNotEquals(MapViewCamera.TrackingUserLocationWithBearing(), MapViewCamera.TrackingUserLocation())
        assertNotEquals(MapViewCamera.TrackingUserLocationWithBearing(), MapViewCamera.Centered(0.0, 0.0))
    }

    // MARK: HashCode tests

    @Test
    fun `test MapViewCamera default hashCode`() {
        assertEquals(MapViewCamera.Default.hashCode(), MapViewCamera.Default.hashCode())
        assertEquals(MapViewCamera.Default.hashCode(), MapViewCamera.Centered(0.0, 0.0).hashCode())
        assertNotEquals(MapViewCamera.Default.hashCode(), MapViewCamera.TrackingUserLocation().hashCode())
        assertNotEquals(MapViewCamera.Default.hashCode(), MapViewCamera.TrackingUserLocationWithBearing().hashCode())
    }

    @Test
    fun `test MapViewCamera centered hashCode`() {
        assertEquals(MapViewCamera.Centered(0.0, 0.0).hashCode(), MapViewCamera.Centered(0.0, 0.0).hashCode())
        assertNotEquals(MapViewCamera.Centered(0.0, 0.0).hashCode(), MapViewCamera.Centered(0.0, 1.0).hashCode())
        assertNotEquals(MapViewCamera.Centered(0.0, 0.0).hashCode(), MapViewCamera.TrackingUserLocation().hashCode())
        assertNotEquals(MapViewCamera.Centered(0.0, 0.0).hashCode(), MapViewCamera.TrackingUserLocationWithBearing().hashCode())
    }

    @Test
    fun `test MapViewCamera tracking user location hashCode`() {
        assertEquals(MapViewCamera.TrackingUserLocation().hashCode(), MapViewCamera.TrackingUserLocation().hashCode())
        assertNotEquals(MapViewCamera.TrackingUserLocation().hashCode(), MapViewCamera.TrackingUserLocationWithBearing().hashCode())
        assertNotEquals(MapViewCamera.TrackingUserLocation().hashCode(), MapViewCamera.Centered(0.0, 0.0).hashCode())
    }

    @Test
    fun `test MapViewCamera tracking user location with bearing hashCode`() {
        assertEquals(MapViewCamera.TrackingUserLocationWithBearing().hashCode(), MapViewCamera.TrackingUserLocationWithBearing().hashCode())
        assertNotEquals(MapViewCamera.TrackingUserLocationWithBearing().hashCode(), MapViewCamera.TrackingUserLocation().hashCode())
        assertNotEquals(MapViewCamera.TrackingUserLocationWithBearing().hashCode(), MapViewCamera.Centered(0.0, 0.0).hashCode())
    }

    // MARK: Test MutableState of MapViewCamera

    @Test
    fun `test mutable state of MapCameraView triggers updates`() {
        val mutableCamera = mutableStateOf(MapViewCamera.Default)

        assertEquals(MapViewCamera.Default, mutableCamera.value)

        mutableCamera.value = MapViewCamera.TrackingUserLocation()

        assertEquals(MapViewCamera.TrackingUserLocation(), mutableCamera.value)
    }
}