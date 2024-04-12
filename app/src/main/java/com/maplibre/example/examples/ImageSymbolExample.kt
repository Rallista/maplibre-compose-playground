package com.maplibre.example.examples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.mapboxsdk.geometry.LatLng
import com.maplibre.compose.MapView
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.rememberSaveableMapViewCamera
import com.maplibre.compose.symbols.Symbol
import com.maplibre.example.R

@Composable
fun ImageSymbolExample() {

    val mapViewCamera = rememberSaveableMapViewCamera(
        initialCamera = MapViewCamera.Centered(
            latitude = 1.227,
            longitude = 103.962,
            zoom = 10.0
        )
    )

    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            MapView(
                styleUrl = "https://demotiles.maplibre.org/style.json",
                camera = mapViewCamera
            ) {
                Symbol(
                    center = LatLng(1.203, 103.873),
                    size = 2f,
                    imageId = R.drawable.bitmap,
                    imageRotation = -20f
                )

                Symbol(
                    center = LatLng(1.253, 104.019),
                    imageId = R.drawable.vector,
                    imageRotation = 40f
                )
            }
        }
    }
}