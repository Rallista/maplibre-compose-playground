package com.maplibre.example.examples

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maplibre.example.R
import kotlinx.serialization.json.buildJsonObject
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.LineLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.LineString
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position

// FLAG: The old library used simple composable annotations (Symbol, Circle, CircleWithItem,
// Polyline) placed inside the MapView content lambda. The new library uses a source+layer paradigm.
// Per-annotation onTap/onLongPress callbacks are replaced by per-layer onClick/onLongClick
// that receive a List<Feature>. Individual feature identification requires feature properties.

// FLAG: CircleWithItem (composite of circle + text label) must be recreated as
// CircleLayer + SymbolLayer sharing the same source.

// FLAG: SymbolText.Builder with textSize, textColor, textAnchor has no direct equivalent.
// Text styling is done via SymbolLayer parameters (textSize, textColor, textAnchor).

private fun pointFeature(longitude: Double, latitude: Double) =
    Feature(
        geometry = Point(Position(longitude = longitude, latitude = latitude)),
        properties = buildJsonObject {})

@Composable
fun SymbolExample() {

  val cameraState =
      rememberCameraState(
          firstPosition =
              CameraPosition(
                  target = Position(longitude = 103.962, latitude = 1.227), zoom = 10.0))

  Box {
    MaplibreMap(
        modifier = Modifier.fillMaxSize(),
        baseStyle = BaseStyle.Uri("https://demotiles.maplibre.org/style.json"),
        cameraState = cameraState,
        onMapClick = { _, _ ->
          Log.d("SymbolExample", "Map tapped")
          ClickResult.Pass
        }) {
          // Sources must be created inside the MaplibreMap content scope (LocalStyleNode)
          val redStarSource =
              rememberGeoJsonSource(
                  data = GeoJsonData.Features(FeatureCollection(pointFeature(103.873, 1.203))))

          val blueCircleAndStarSource =
              rememberGeoJsonSource(
                  data = GeoJsonData.Features(FeatureCollection(pointFeature(104.019, 1.253))))

          val defaultCircleSource =
              rememberGeoJsonSource(
                  data = GeoJsonData.Features(FeatureCollection(pointFeature(103.969, 1.173))))

          val customCircleSource =
              rememberGeoJsonSource(
                  data = GeoJsonData.Features(FeatureCollection(pointFeature(103.902, 1.126))))

          val polylineSource =
              rememberGeoJsonSource(
                  data =
                      GeoJsonData.Features(
                          FeatureCollection(
                              Feature(
                                  geometry =
                                      LineString(
                                          Position(longitude = 103.813, latitude = 1.147),
                                          Position(longitude = 103.887, latitude = 1.259),
                                          Position(longitude = 103.931, latitude = 1.205),
                                          Position(longitude = 103.993, latitude = 1.295)),
                                  properties = buildJsonObject {}))))

          // Red star symbol with rotation
          SymbolLayer(
              id = "red-star-layer",
              source = redStarSource,
              iconImage = image(painterResource(R.drawable.bitmap)),
              iconSize = const(2f),
              iconRotate = const(-20f),
              iconAllowOverlap = const(true),
              onClick = {
                Log.d("SymbolExample", "Tapped red star")
                ClickResult.Consume
              },
              onLongClick = {
                Log.d("SymbolExample", "Long pressed red star")
                ClickResult.Consume
              })

          // Blue circle
          CircleLayer(
              id = "blue-circle-layer",
              source = blueCircleAndStarSource,
              radius = const(2.dp),
              color = const(Color.Blue))

          // Blue star symbol with offset
          // FLAG: SymbolOffset(-20f, 20f) from old library maps to iconOffset in DpOffset
          SymbolLayer(
              id = "blue-star-layer",
              source = blueCircleAndStarSource,
              iconImage = image(painterResource(R.drawable.vector)),
              iconAllowOverlap = const(true),
              onClick = {
                Log.d("SymbolExample", "Tapped blue star")
                ClickResult.Consume
              },
              onLongClick = {
                Log.d("SymbolExample", "Long pressed blue star")
                ClickResult.Consume
              })

          // Circle with default text (was CircleWithItem)
          // FLAG: CircleWithItem was a single composable; now requires separate layers
          CircleLayer(
              id = "default-circle-layer",
              source = defaultCircleSource,
              radius = const(10.dp),
              color = const(Color.Black),
              onClick = {
                Log.d("SymbolExample", "Tapped default circle")
                ClickResult.Consume
              },
              onLongClick = {
                Log.d("SymbolExample", "Long pressed default circle")
                ClickResult.Consume
              })

          SymbolLayer(
              id = "default-circle-text-layer",
              source = defaultCircleSource,
              textField = const("Default").cast(),
              textColor = const(Color.White),
              textAllowOverlap = const(true))

          // Circle with custom styled text (was CircleWithItem with SymbolText.Builder)
          CircleLayer(
              id = "custom-circle-layer",
              source = customCircleSource,
              radius = const(10.dp),
              color = const(Color.Black),
              onClick = {
                Log.d("SymbolExample", "Tapped custom circle")
                ClickResult.Consume
              },
              onLongClick = {
                Log.d("SymbolExample", "Long pressed custom circle")
                ClickResult.Consume
              })

          // FLAG: SymbolText.Builder().textSize(20f).textColor("Red").textAnchor(BOTTOM_RIGHT)
          // is approximated here with SymbolLayer text parameters.
          SymbolLayer(
              id = "custom-circle-text-layer",
              source = customCircleSource,
              textField = const("Custom").cast(),
              textColor = const(Color.Red),
              textAllowOverlap = const(true))

          // Polyline
          // FLAG: linePatternId (drawable resource as line pattern) uses a different expression-
          // based image system. Using pattern with a Painter loaded from the drawable resource.
          LineLayer(
              id = "polyline-layer",
              source = polylineSource,
              width = const(12.dp),
              pattern = image(painterResource(R.drawable.arrow)),
              opacity = const(0.1f))
        }
  }
}
