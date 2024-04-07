package com.maplibre.compose

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mapbox.mapboxsdk.location.engine.LocationEngine
import com.maplibre.compose.ramani.CameraPosition
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.ramani.LocationRequestProperties
import com.maplibre.compose.ramani.LocationStyling
import com.maplibre.compose.ramani.MapLibre
import com.maplibre.compose.ramani.MapLibreComposable

@Composable
fun MapView(
    modifier: Modifier = Modifier.fillMaxSize(),
    styleUrl: String,
    camera: MutableState<MapViewCamera> = rememberSaveable { mutableStateOf(MapViewCamera()) },
    locationEngine: LocationEngine? = null,
    locationRequestProperties: LocationRequestProperties = LocationRequestProperties.Default,
    locationStyling: LocationStyling = LocationStyling.Default,
    userLocation: MutableState<Location>? = null,
    content: (@Composable @MapLibreComposable () -> Unit)? = null
) {

    val cameraState by rememberUpdatedState(camera)

    val cameraPosition = remember(cameraState.value) { mutableStateOf(cameraState.value.toCameraPosition()) }
//    Log.d("MapView", "CameraPosition value is: ${cameraPosition.value.trackingMode}")

    // Update the camera position when the camera changes
    LaunchedEffect(cameraState.value) {
        cameraPosition.value = camera.value.toCameraPosition()
    }

    LaunchedEffect(cameraPosition.value) {
        Log.d("MapView", "CameraPosition value is updating to: ${cameraPosition.value}")
        camera.value = MapViewCamera.fromCameraPosition(cameraPosition.value)
    }

    MapLibre(
        modifier,
        styleUrl,
        cameraPosition = cameraPosition,
        locationEngine = locationEngine,
        locationRequestProperties = locationRequestProperties,
        locationStyling = locationStyling,
        userLocation = userLocation
    ) {
        content?.invoke()
    }
}

@Preview
@Composable
fun MapViewPreview() {
    MapView(styleUrl = "https://demotiles.maplibre.org/style.json")
}