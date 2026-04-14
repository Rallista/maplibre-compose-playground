package com.maplibre.example.examples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.maplibre.example.LocalMapStyleUrl
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

@Composable
fun DarkAndLightModeExample() {

  val cameraState =
      rememberCameraState(
          firstPosition =
              CameraPosition(
                  target = Position(longitude = 74.1, latitude = 15.3), zoom = 9.0))

  // Get the map style URL from the CompositionLocal provider wrapping the MainActivity
  val mapStyleUrl = LocalMapStyleUrl.current

  Box {
    MaplibreMap(
        modifier = Modifier.fillMaxSize(),
        baseStyle = BaseStyle.Uri(mapStyleUrl),
        cameraState = cameraState)
  }
}

@Composable
@Preview
fun DarkAndLightModeExamplePreview() {
  DarkAndLightModeExample()
}
