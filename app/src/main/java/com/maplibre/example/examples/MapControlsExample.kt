package com.maplibre.example.examples

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.serialization.json.buildJsonObject
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.LineLayer
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.LineString
import org.maplibre.spatialk.geojson.Position

// FLAG: The old library's MapControls / AttributionSettings / CompassSettings / LogoSettings /
// MapControlPosition / MarginInsets system has no equivalent in the new library.
// The new library offers OrnamentOptions presets (AllEnabled, OnlyLogo, AllDisabled) only.
// Fine-grained control positioning requires using Material3 ornament composables
// (CompassButton, ExpandingAttributionButton) placed manually via Compose modifiers.
// This example demonstrates the concept using animated Compose positioning instead.

@Composable
fun MapControlsExample() {

  val phase = remember { mutableStateOf(0) }
  val color = remember { mutableStateOf(Color.Black) }

  // Dynamically update the polyline color after a delay to demonstrate reactive state.
  LaunchedEffect(Unit) {
    delay(2000)
    phase.value = 1
    color.value = Color.White
  }

  val cameraState =
      rememberCameraState(
          firstPosition =
              CameraPosition(
                  target = Position(longitude = -73.71, latitude = -50.04), zoom = 7.0))

  // Animate control positions based on phase
  val topPadding = animateDpAsState(targetValue = if (phase.value == 0) 64.dp else 16.dp)
  val startPadding = animateDpAsState(targetValue = if (phase.value == 0) 64.dp else 16.dp)

  Box(modifier = Modifier.fillMaxSize()) {
    MaplibreMap(
        modifier = Modifier.fillMaxSize(),
        baseStyle = BaseStyle.Uri("https://demotiles.maplibre.org/style.json"),
        cameraState = cameraState,
        // FLAG: Using OnlyLogo since we can't do fine-grained control positioning with the
        // new library's OrnamentOptions. In a full implementation, you would add Material3
        // composables (CompassButton, ExpandingAttributionButton) as overlays.
        options = MapOptions(ornamentOptions = OrnamentOptions.OnlyLogo)) {
          // Source must be created inside MaplibreMap content scope (LocalStyleNode)
          val polylineSource =
              rememberGeoJsonSource(
                  data =
                      GeoJsonData.Features(
                          FeatureCollection(
                              Feature(
                                  geometry =
                                      LineString(
                                          Position(longitude = -73.69, latitude = -50.20),
                                          Position(longitude = -73.71, latitude = -50.10),
                                          Position(longitude = -73.73, latitude = -50.00)),
                                  properties = buildJsonObject {}))))

          LineLayer(
              id = "polyline-layer",
              source = polylineSource,
              width = const(12.dp),
              color = const(color.value))
        }

    // FLAG: The old example dynamically repositioned compass, attribution, and logo controls.
    // The new library doesn't support this via MapControls. We show a placeholder text to
    // demonstrate the animated positioning concept.
    androidx.compose.material3.Text(
        text = if (phase.value == 0) "Controls: Phase 1" else "Controls: Phase 2",
        modifier =
            Modifier.align(Alignment.TopEnd)
                .padding(top = topPadding.value, end = startPadding.value),
        color = Color.White)
  }
}
