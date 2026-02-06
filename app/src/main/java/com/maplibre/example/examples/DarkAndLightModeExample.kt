package com.maplibre.example.examples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.maplibre.compose.MapView
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.mapLibreStyleUrl
import com.maplibre.compose.rememberSaveableMapViewCamera

@Composable
fun DarkAndLightModeExample() {

  val mapViewCamera =
      rememberSaveableMapViewCamera(
          initialCamera = MapViewCamera.Centered(latitude = 15.3, longitude = 74.1, zoom = 9.0))

  // Get the MapLibre style URL from the provider wrapping the MainActivity
  // See MainActivity.kt L23 & L29
  val mapStyleUrl = mapLibreStyleUrl()

  Box { MapView(modifier = Modifier.fillMaxSize(), styleUrl = mapStyleUrl, camera = mapViewCamera) }
}

@Composable
@Preview
fun DarkAndLightModeExamplePreview() {
  DarkAndLightModeExample()
}
