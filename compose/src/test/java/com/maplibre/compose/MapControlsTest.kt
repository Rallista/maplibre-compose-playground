package com.maplibre.compose

import com.maplibre.compose.settings.AttributionSettings
import com.maplibre.compose.settings.CompassSettings
import com.maplibre.compose.settings.LogoSettings
import com.maplibre.compose.settings.MapControls
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class MapControlsTest {

  @Test
  fun `test MapControls empty`() {
    val mapControls = MapControls()
    assertNull(mapControls.attribution)
    assertNull(mapControls.compass)
    assertNull(mapControls.logo)
  }

  @Test
  fun `test MapControls with attribution`() {
    val mapControls = MapControls(attribution = AttributionSettings())
    assertNotNull(mapControls.attribution)
  }

  @Test
  fun `test MapControls with compass`() {
    val mapControls = MapControls(compass = CompassSettings())
    assertNotNull(mapControls.compass)
  }

  @Test
  fun `test MapControls with logo`() {
    val mapControls = MapControls(logo = LogoSettings())
    assertNotNull(mapControls.logo)
  }
}
