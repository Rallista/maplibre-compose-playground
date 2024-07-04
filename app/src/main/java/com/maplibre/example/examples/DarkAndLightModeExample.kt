package com.maplibre.example.examples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.maplibre.compose.MapView
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.rememberMapStyleUrl
import com.maplibre.compose.rememberSaveableMapViewCamera

@Composable
fun DarkAndLightModeExample() {

    val mapViewCamera = rememberSaveableMapViewCamera(
        initialCamera = MapViewCamera.Centered(
            latitude = 15.3,
            longitude = 74.1,
            zoom = 9.0
        )
    )

    // Get the MapLibre style URL from the provider wrapping the MainActivity
    // See MainActivity.kt L1
    val mapStyleUrl = rememberMapStyleUrl()

    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            MapView(
                styleUrl = mapStyleUrl,
                camera = mapViewCamera
            )
        }
    }
}

@Composable
@Preview
fun DarkAndLightModeExamplePreview() {
    DarkAndLightModeExample()
}