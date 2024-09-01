package com.maplibre.example.examples

import android.view.Gravity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mapbox.mapboxsdk.geometry.LatLng
import com.maplibre.compose.MapView
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.rememberSaveableMapViewCamera
import com.maplibre.compose.runtime.localLayoutDirection
import com.maplibre.compose.settings.AttributionSettings
import com.maplibre.compose.settings.CompassSettings
import com.maplibre.compose.settings.LogoSettings
import com.maplibre.compose.settings.MapControlPosition
import com.maplibre.compose.settings.MapControls
import com.maplibre.compose.settings.MarginInsets
import com.maplibre.compose.symbols.Polyline
import kotlinx.coroutines.delay

/**
 * MapControls modify the UiSettings for controls on the map including the compass, logo, and
 * attribution.
 */
@Composable
fun MapControlsExample() {

  val density = LocalDensity.current
  val layoutDirection = localLayoutDirection()

  // Create an initial set of map controls
  val initialMapControls =
      MapControls(
          attribution =
              AttributionSettings.initWithPosition(
                  position = MapControlPosition.TopEnd(horizontal = 64.dp, vertical = 64.dp)),
          compass =
              // Using margins directly is possible, but it's better to use
              // [MapControlPosition] as seen above in with AttributionSettings
              CompassSettings(
                  fadeFacingNorth = false,
                  gravity = Gravity.START or Gravity.BOTTOM,
                  margins =
                      MarginInsets.createFromPadding(PaddingValues(start = 64.dp, bottom = 128.dp)),
              ),
          logo = LogoSettings(enabled = false))

  // Remember the map controls and color as mutable state to update them in the LaunchedEffect.
  val mapControls = remember { mutableStateOf(initialMapControls) }
  val color = remember { mutableStateOf("#000") }

  // Dynamically update the map controls and polyline after a delay
  LaunchedEffect(Unit) {
    delay(2000)

    mapControls.value =
        MapControls(
            attribution =
                AttributionSettings.initWithLayoutAndPosition(
                    layoutDirection = layoutDirection,
                    density = density,
                    enabled = true,
                    position = MapControlPosition.TopCenter(vertical = 64.dp)),
            compass = CompassSettings(enabled = false),
            logo = LogoSettings(enabled = true))

    color.value = "#fff"
  }

  val mapViewCamera =
      rememberSaveableMapViewCamera(
          initialCamera = MapViewCamera.Centered(latitude = -50.04, longitude = -73.71, zoom = 7.0))

  val latLngs =
      listOf(
          LatLng(-50.20, -73.69),
          LatLng(-50.10, -73.71),
          LatLng(-50.00, -73.73),
      )

  MapView(
      modifier = Modifier.fillMaxSize(),
      styleUrl = "https://demotiles.maplibre.org/style.json",
      camera = mapViewCamera,
      mapControls = mapControls.value) {
        Polyline(points = latLngs, color = color.value, lineWidth = 12f)
      }
}
