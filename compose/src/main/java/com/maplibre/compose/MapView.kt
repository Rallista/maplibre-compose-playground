package com.maplibre.compose

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mapbox.mapboxsdk.location.engine.LocationEngine
import com.mapbox.mapboxsdk.maps.Style
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.ramani.LocationRequestProperties
import com.maplibre.compose.ramani.LocationStyling
import com.maplibre.compose.ramani.MapGestureContext
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
    onMapReadyCallback: ((Style) -> Unit)? = null,
    onTapGestureCallback: ((MapGestureContext) -> Unit)? = null,
    onLongPressGestureCallback: ((MapGestureContext) -> Unit)? = null,
    content: (@Composable @MapLibreComposable () -> Unit)? = null
) {

  val cameraPosition =
      rememberSaveable(camera.value) { mutableStateOf(camera.value.toCameraPosition()) }

  // Update the parent camera when the internal camera position changes from the map (e.g. pan
  // gesture).
  LaunchedEffect(cameraPosition.value) {
    camera.value = MapViewCamera.fromCameraPosition(cameraPosition.value)
  }

  MapLibre(
      modifier,
      styleUrl,
      cameraPosition = cameraPosition,
      locationEngine = locationEngine,
      locationRequestProperties = locationRequestProperties,
      locationStyling = locationStyling,
      userLocation = userLocation,
      onMapReadyCallback = onMapReadyCallback,
      onTapGestureCallback = onTapGestureCallback,
      onLongPressGestureCallback = onLongPressGestureCallback) {
        content?.invoke()
      }
}

@Preview
@Composable
fun MapViewPreview() {
  MapView(styleUrl = "https://demotiles.maplibre.org/style.json")
}
