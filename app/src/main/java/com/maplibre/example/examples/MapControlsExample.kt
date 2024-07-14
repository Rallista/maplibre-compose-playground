package com.maplibre.example.examples

import android.view.Gravity
import androidx.compose.runtime.Composable
import com.maplibre.compose.MapView
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.rememberSaveableMapViewCamera
import com.maplibre.compose.settings.AttributionSettings
import com.maplibre.compose.settings.CompassSettings
import com.maplibre.compose.settings.LogoSettings
import com.maplibre.compose.settings.MapControls
import com.maplibre.compose.settings.MarginInsets

/**
 * MapControls modify the UiSettings for controls on the map including the compass, logo, and
 * attribution.
 */
@Composable
fun MapControlsExample() {

  val mapViewCamera =
      rememberSaveableMapViewCamera(
          initialCamera = MapViewCamera.Centered(latitude = -50.04, longitude = -73.71, zoom = 7.0))

  MapView(
      styleUrl = "https://demotiles.maplibre.org/style.json",
      camera = mapViewCamera,
      mapControls =
          MapControls(
              attribution =
                  AttributionSettings(
                      gravity = Gravity.CENTER_VERTICAL or Gravity.START,
                      margins = MarginInsets(start = 50, top = 150),
                  ),
              compass =
                  CompassSettings(
                      fadeFacingNorth = false,
                      margins = MarginInsets(end = 150, top = 250),
                  ),
              logo = LogoSettings(enabled = false)))
}
