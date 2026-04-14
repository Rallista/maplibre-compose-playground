package com.maplibre.example.examples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.Position

@Composable
fun CallbackExample() {

  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  val cameraState =
      rememberCameraState(
          firstPosition =
              CameraPosition(
                  target = Position(longitude = -155.031807, latitude = 57.636576), zoom = 6.0))

  Box {
    MaplibreMap(
        modifier = Modifier.fillMaxSize(),
        baseStyle = BaseStyle.Uri("https://demotiles.maplibre.org/style.json"),
        cameraState = cameraState,
        onMapLoadFinished = {
          scope.launch { snackbarHostState.showSnackbar("Map ready!") }
        },
        onMapClick = { position, _ ->
          scope.launch {
            snackbarHostState.showSnackbar(
                "Tapped at ${position.latitude}, ${position.longitude}")
          }
          ClickResult.Pass
        },
        onMapLongClick = { position, _ ->
          scope.launch {
            snackbarHostState.showSnackbar(
                "Long pressed at ${position.latitude}, ${position.longitude}")
          }
          ClickResult.Pass
        })

    SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
  }
}

@Composable
@Preview
fun CallbackExamplePreview() {
  CallbackExample()
}
