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
import com.maplibre.compose.MapView
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.rememberSaveableMapViewCamera
import kotlinx.coroutines.launch

@Composable
fun CallbackExample() {

  //    https://developer.android.com/develop/ui/compose/components/snackbar#basic_example

  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  val mapViewCamera =
      rememberSaveableMapViewCamera(
          initialCamera =
              MapViewCamera.Centered(latitude = 57.636576, longitude = -155.031807, zoom = 6.0))
    Box {
        MapView(
            modifier = Modifier.fillMaxSize(),
            styleUrl = "https://demotiles.maplibre.org/style.json",
            camera = mapViewCamera,
            onMapReadyCallback = { scope.launch { snackbarHostState.showSnackbar("Map ready!") } },
            onTapGestureCallback = {
                scope.launch { snackbarHostState.showSnackbar("Tapped at ${it.coordinate}") }
            },
            onLongPressGestureCallback = {
                scope.launch { snackbarHostState.showSnackbar("Long pressed at ${it.coordinate}") }
            })

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// TODO: Can this work with the async map style?
@Composable
@Preview
fun CallbackExamplePreview() {
  CallbackExample()
}
