package com.maplibre.compose

import android.view.Gravity
import androidx.compose.ui.unit.dp
import com.maplibre.compose.settings.MapControlPosition
import org.junit.Assert.assertEquals
import org.junit.Test

class MapControlPositionTest {

  @Test
  fun `test MapControlPosition TopStart default`() {
    val mapControlPosition = MapControlPosition.TopStart()

    assertEquals(Gravity.TOP or Gravity.START, mapControlPosition.asGravity())
    assertEquals(16.dp, mapControlPosition.vertical)
    assertEquals(16.dp, mapControlPosition.horizontal)
  }

  @Test
  fun `test MapControlPosition TopCenter default`() {
    val mapControlPosition = MapControlPosition.TopCenter()

    assertEquals(Gravity.TOP or Gravity.CENTER_HORIZONTAL, mapControlPosition.asGravity())
    assertEquals(16.dp, mapControlPosition.vertical)
    assertEquals(0.dp, mapControlPosition.horizontal)
  }

  @Test
  fun `test MapControlPosition TopEnd default`() {
    val mapControlPosition = MapControlPosition.TopEnd()

    assertEquals(Gravity.TOP or Gravity.END, mapControlPosition.asGravity())
    assertEquals(16.dp, mapControlPosition.vertical)
    assertEquals(16.dp, mapControlPosition.horizontal)
  }

  @Test
  fun `test MapControlPosition CenterStart default`() {
    val mapControlPosition = MapControlPosition.CenterStart()

    assertEquals(Gravity.CENTER or Gravity.START, mapControlPosition.asGravity())
    assertEquals(0.dp, mapControlPosition.vertical)
    assertEquals(16.dp, mapControlPosition.horizontal)
  }

  @Test
  fun `test MapControlPosition Center default`() {
    val mapControlPosition = MapControlPosition.Center()

    assertEquals(Gravity.CENTER, mapControlPosition.asGravity())
    assertEquals(0.dp, mapControlPosition.vertical)
    assertEquals(0.dp, mapControlPosition.horizontal)
  }

  @Test
  fun `test MapControlPosition CenterEnd default`() {
    val mapControlPosition = MapControlPosition.CenterEnd()

    assertEquals(Gravity.CENTER or Gravity.END, mapControlPosition.asGravity())
    assertEquals(0.dp, mapControlPosition.vertical)
    assertEquals(16.dp, mapControlPosition.horizontal)
  }

  @Test
  fun `test MapControlPosition BottomStart default`() {
    val mapControlPosition = MapControlPosition.BottomStart()

    assertEquals(Gravity.BOTTOM or Gravity.START, mapControlPosition.asGravity())
    assertEquals(16.dp, mapControlPosition.vertical)
    assertEquals(16.dp, mapControlPosition.horizontal)
  }

  @Test
  fun `test MapControlPosition BottomCenter default`() {
    val mapControlPosition = MapControlPosition.BottomCenter()

    assertEquals(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, mapControlPosition.asGravity())
    assertEquals(16.dp, mapControlPosition.vertical)
    assertEquals(0.dp, mapControlPosition.horizontal)
  }

  @Test
  fun `test MapControlPosition BottomEnd default`() {
    val mapControlPosition = MapControlPosition.BottomEnd()

    assertEquals(Gravity.BOTTOM or Gravity.END, mapControlPosition.asGravity())
    assertEquals(16.dp, mapControlPosition.vertical)
    assertEquals(16.dp, mapControlPosition.horizontal)
  }
}
