package com.maplibre.compose

import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.ramani.LocationRequestProperties
import com.maplibre.compose.ramani.LocationStyling
import com.maplibre.compose.ramani.MapGestureContext
import com.maplibre.compose.ramani.MapLibre
import com.maplibre.compose.ramani.MapLibreComposable
import com.maplibre.compose.settings.MapControls
import org.maplibre.android.location.engine.LocationEngine
import org.maplibre.android.maps.Style

@Composable
fun MapView(
    modifier: Modifier,
    styleUrl: String,
    mapControls: MapControls = MapControls(),
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
  // TODO: We may be close to just renaming this to MapView and propagating the defaults above.
  MapLibre(
      modifier,
      styleUrl,
      mapControls = mapControls,
      camera = camera,
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
  MapView(modifier = Modifier.fillMaxSize(), styleUrl = "https://demotiles.maplibre.org/style.json")
}
