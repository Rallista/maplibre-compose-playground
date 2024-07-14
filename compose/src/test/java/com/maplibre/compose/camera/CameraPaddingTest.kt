package com.maplibre.compose.camera

import com.maplibre.compose.camera.models.CameraPadding
import junit.framework.Assert.assertEquals
import org.junit.Test

class CameraPaddingTest {

  @Test
  fun `test CameraPadding to double array`() {
    val cameraPadding = CameraPadding(10.0, 20.0, 30.0, 40.0)
    val doubleArray = cameraPadding.toDoubleArray()
    val expectedArray = doubleArrayOf(10.0, 20.0, 30.0, 40.0)

    assertEquals(doubleArray[0], expectedArray[0])
    assertEquals(doubleArray[1], expectedArray[1])
    assertEquals(doubleArray[2], expectedArray[2])
    assertEquals(doubleArray[3], expectedArray[3])
  }

  @Test
  fun `test CameraPadding from camera position`() {
    val padding = doubleArrayOf(10.0, 20.0, 30.0, 40.0)
    val cameraPadding = CameraPadding.fromCameraPosition(padding)

    assertEquals(cameraPadding.start, 10.0)
    assertEquals(cameraPadding.top, 20.0)
    assertEquals(cameraPadding.end, 30.0)
    assertEquals(cameraPadding.bottom, 40.0)
  }

  @Test
  fun `test CameraPadding from camera position with null padding`() {
    val cameraPadding = CameraPadding.fromCameraPosition(null)

    assertEquals(cameraPadding.start, 0.0)
    assertEquals(cameraPadding.top, 0.0)
    assertEquals(cameraPadding.end, 0.0)
    assertEquals(cameraPadding.bottom, 0.0)
  }

  @Test
  fun `test CameraPadding from partial camera position`() {
    val padding = doubleArrayOf(1.0)
    val cameraPadding = CameraPadding.fromCameraPosition(padding)

    assertEquals(cameraPadding.start, 1.0)
    assertEquals(cameraPadding.top, 0.0)
    assertEquals(cameraPadding.end, 0.0)
    assertEquals(cameraPadding.bottom, 0.0)
  }

  @Test
  fun `test CameraPadding from too large doubleArray`() {
    val padding = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0)
    val cameraPadding = CameraPadding.fromCameraPosition(padding)

    assertEquals(cameraPadding.start, 1.0)
    assertEquals(cameraPadding.top, 2.0)
    assertEquals(cameraPadding.end, 3.0)
    assertEquals(cameraPadding.bottom, 4.0)
  }
}
