package com.maplibre.compose.camera

import com.maplibre.compose.camera.extensions.fromMapLibre
import com.maplibre.compose.camera.extensions.toMapLibre
import com.maplibre.compose.camera.models.CameraPitchRange
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CameraPitchRangeTests {

  @Test
  fun `test CameraPitchRange fromMapLibre`() {
    assertEquals(
        CameraPitchRange.fromMapLibre(0.0, 40.0), CameraPitchRange.FreeWithPitch(0.0, 40.0))
    assertEquals(
        CameraPitchRange.fromMapLibre(30.0, 50.0), CameraPitchRange.FreeWithPitch(30.0, 50.0))
    assertEquals(CameraPitchRange.fromMapLibre(0.0, 0.0), CameraPitchRange.Fixed(0.0))
    assertEquals(CameraPitchRange.fromMapLibre(20.0, 20.0), CameraPitchRange.Fixed(20.0))
    assertEquals(CameraPitchRange.fromMapLibre(0.0, 60.0), CameraPitchRange.Free)
    assertEquals(CameraPitchRange.fromMapLibre(0.0, 85.0), CameraPitchRange.Free)
  }

  @Test
  fun `test CameraPitchRange toMapLibre`() {
    assertEquals(CameraPitchRange.Free.toMapLibre(), Pair(0.0, 60.0))
    assertEquals(CameraPitchRange.FreeWithPitch(0.0, 40.0).toMapLibre(), Pair(0.0, 40.0))
    assertEquals(CameraPitchRange.FreeWithPitch(30.0, 50.0).toMapLibre(), Pair(30.0, 50.0))
    assertEquals(CameraPitchRange.Fixed(0.0).toMapLibre(), Pair(0.0, 0.0))
    assertEquals(CameraPitchRange.Fixed(20.0).toMapLibre(), Pair(20.0, 20.0))
  }

  @Test
  fun `test CameraPitchRange rangeValue`() {
    assertEquals(CameraPitchRange.Free.rangeValue, Pair(0.0, 60.0))
    assertEquals(CameraPitchRange.FreeWithPitch(0.0, 40.0).rangeValue, Pair(0.0, 40.0))
    assertEquals(CameraPitchRange.FreeWithPitch(30.0, 50.0).rangeValue, Pair(30.0, 50.0))
    assertEquals(CameraPitchRange.Fixed(0.0).rangeValue, Pair(0.0, 0.0))
    assertEquals(CameraPitchRange.Fixed(20.0).rangeValue, Pair(20.0, 20.0))
  }

  @Test
  fun `test CameraPitchRange equals`() {
    assertEquals(CameraPitchRange.Free, CameraPitchRange.Free)
    assertEquals(
        CameraPitchRange.FreeWithPitch(0.0, 40.0), CameraPitchRange.FreeWithPitch(0.0, 40.0))
    assertEquals(
        CameraPitchRange.FreeWithPitch(30.0, 50.0), CameraPitchRange.FreeWithPitch(30.0, 50.0))
    assertEquals(CameraPitchRange.Fixed(0.0), CameraPitchRange.Fixed(0.0))
    assertEquals(CameraPitchRange.Fixed(20.0), CameraPitchRange.Fixed(20.0))
  }

  @Test
  fun `test CameraPitchRange not equals`() {
    assertNotEquals(CameraPitchRange.Free, CameraPitchRange.FreeWithPitch(0.0, 40.0))
    assertNotEquals(CameraPitchRange.Free, CameraPitchRange.Fixed(0.0))
    assertNotEquals(CameraPitchRange.Free, CameraPitchRange.Fixed(20.0))
    assertNotEquals(CameraPitchRange.FreeWithPitch(30.0, 50.0), CameraPitchRange.Free)
    assertNotEquals(CameraPitchRange.FreeWithPitch(0.0, 40.0), CameraPitchRange.Fixed(0.0))
    assertNotEquals(CameraPitchRange.Fixed(20.0), CameraPitchRange.Free)
    assertNotEquals(CameraPitchRange.Fixed(0.0), CameraPitchRange.FreeWithPitch(0.0, 40.0))
  }

  // MARK: HashCode

  @Test
  fun `test CameraPitchRange hashCode`() {
    assertEquals(CameraPitchRange.Free.hashCode(), CameraPitchRange.Free.hashCode())
    assertEquals(
        CameraPitchRange.FreeWithPitch(0.0, 40.0).hashCode(),
        CameraPitchRange.FreeWithPitch(0.0, 40.0).hashCode())
    assertEquals(
        CameraPitchRange.FreeWithPitch(30.0, 50.0).hashCode(),
        CameraPitchRange.FreeWithPitch(30.0, 50.0).hashCode())
    assertEquals(CameraPitchRange.Fixed(0.0).hashCode(), CameraPitchRange.Fixed(0.0).hashCode())
    assertEquals(CameraPitchRange.Fixed(20.0).hashCode(), CameraPitchRange.Fixed(20.0).hashCode())
  }

  @Test
  fun `test CameraPitchRange mismatch hashCode`() {
    assertNotEquals(
        CameraPitchRange.Free.hashCode(), CameraPitchRange.FreeWithPitch(0.0, 40.0).hashCode())
    assertNotEquals(CameraPitchRange.Free.hashCode(), CameraPitchRange.Fixed(0.0).hashCode())
    assertNotEquals(CameraPitchRange.Free.hashCode(), CameraPitchRange.Fixed(20.0).hashCode())
    assertNotEquals(
        CameraPitchRange.FreeWithPitch(30.0, 50.0).hashCode(), CameraPitchRange.Free.hashCode())
    assertNotEquals(
        CameraPitchRange.FreeWithPitch(0.0, 40.0).hashCode(),
        CameraPitchRange.Fixed(0.0).hashCode())
    assertNotEquals(CameraPitchRange.Fixed(20.0).hashCode(), CameraPitchRange.Free.hashCode())
    assertNotEquals(
        CameraPitchRange.Fixed(0.0).hashCode(),
        CameraPitchRange.FreeWithPitch(0.0, 40.0).hashCode())
  }

  // MARK: toString

  @Test
  fun `test CameraPitchRange toString`() {
    assertEquals("Free", CameraPitchRange.Free.toString())
    assertEquals(
        "FreeWithPitch(minimum=0.0, maximum=40.0)",
        CameraPitchRange.FreeWithPitch(0.0, 40.0).toString())
    assertEquals(
        "FreeWithPitch(minimum=30.0, maximum=50.0)",
        CameraPitchRange.FreeWithPitch(30.0, 50.0).toString())
    assertEquals("Fixed(pitch=0.0)", CameraPitchRange.Fixed(0.0).toString())
    assertEquals("Fixed(pitch=20.0)", CameraPitchRange.Fixed(20.0).toString())
  }
}
