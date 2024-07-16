package com.maplibre.compose.camera

import com.maplibre.compose.camera.models.CameraMotion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CameraMotionTests {

  // MARK: Equality tests

  @Test
  fun `test CameraMotion equals`() {
    assertEquals(CameraMotion.Instant, CameraMotion.Instant)
    assertEquals(CameraMotion.Fly(1000), CameraMotion.Fly(1000))
    assertEquals(CameraMotion.Ease(1000), CameraMotion.Ease(1000))
  }

  @Test
  fun `test CameraMotion not equals`() {
    assertNotEquals(CameraMotion.Instant, CameraMotion.Fly(1000))
    assertNotEquals(CameraMotion.Instant, CameraMotion.Ease(1000))
    assertNotEquals(CameraMotion.Fly(1000), CameraMotion.Ease(1000))
  }

  @Test
  fun `test CameraMotion animationDurationMs not equals`() {
    assertNotEquals(CameraMotion.Fly(1000), CameraMotion.Fly(2000))
    assertNotEquals(CameraMotion.Ease(1000), CameraMotion.Ease(2000))
  }

  @Test
  fun `test CameraMotion hashCode`() {
    assertEquals(CameraMotion.Instant.hashCode(), CameraMotion.Instant.hashCode())
    assertEquals(CameraMotion.Fly(1000).hashCode(), CameraMotion.Fly(1000).hashCode())
    assertEquals(CameraMotion.Ease(1000).hashCode(), CameraMotion.Ease(1000).hashCode())
  }

  @Test
  fun `test CameraMotion mismatched hashCode`() {
    assertNotEquals(CameraMotion.Instant.hashCode(), CameraMotion.Fly(1000).hashCode())
    assertNotEquals(CameraMotion.Instant.hashCode(), CameraMotion.Ease(1000).hashCode())
    assertNotEquals(CameraMotion.Fly(1000).hashCode(), CameraMotion.Ease(1000).hashCode())
    assertNotEquals(CameraMotion.Fly(1000).hashCode(), CameraMotion.Fly(2000).hashCode())
    assertNotEquals(CameraMotion.Ease(1000).hashCode(), CameraMotion.Ease(2000).hashCode())
  }
}
