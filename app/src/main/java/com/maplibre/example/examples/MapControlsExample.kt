package com.maplibre.example.examples

import android.view.Gravity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maplibre.compose.MapView
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.rememberSaveableMapViewCamera
import com.maplibre.compose.settings.AttributionSettings
import com.maplibre.compose.settings.CompassSettings
import com.maplibre.compose.settings.LogoSettings
import com.maplibre.compose.settings.MapControlPosition
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
      modifier = Modifier.fillMaxSize(),
      styleUrl = "https://demotiles.maplibre.org/style.json",
      camera = mapViewCamera,
      mapControls =
          MapControls(
              attribution =
                  AttributionSettings.initWithPosition(
                      position =
                          MapControlPosition.TopEnd(horizontal = 64.dp, vertical = 64.dp)),
              compass =
                  // Using margins directly is possible, but it's better to use
                  // [MapControlPosition] as seen above in with AttributionSettings
                  CompassSettings(
                      fadeFacingNorth = false,
                      gravity = Gravity.START or Gravity.BOTTOM,
                      margins =
                          MarginInsets.createFromPadding(
                              PaddingValues(start = 64.dp, bottom = 128.dp)),
                  ),
              logo = LogoSettings(enabled = false)))
}
