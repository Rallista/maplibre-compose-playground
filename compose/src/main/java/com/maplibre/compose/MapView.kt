package com.maplibre.compose

import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
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
import org.maplibre.android.maps.MapLibreMapOptions
import org.maplibre.android.maps.Style

/**
 * A composable that displays a MapLibre map.
 *
 * @param modifier The modifier to apply to the map.
 * @param styleUrl The URL of the map style to display.
 * @param camera The camera state for controlling the map's viewport.
 * @param mapControls Controls for map UI elements like compass, logo, and attribution.
 * @param mapOptions [MapLibreMapOptions] to pass when initializing the map.
 * @param locationEngine The location engine to use for location tracking.
 * @param locationRequestProperties Properties for location updates.
 * @param locationStyling Styling options for the location indicator.
 * @param userLocation A mutable state that will be updated with the user's location.
 * @param onMapReadyCallback Callback invoked when the map style is fully loaded.
 * @param onTapGestureCallback Callback invoked when the map is tapped.
 * @param onLongPressGestureCallback Callback invoked when the map is long-pressed.
 * @param content Composable content to render on the map (symbols, polylines, etc.).
 */
@Composable
fun MapView(
    modifier: Modifier,
    styleUrl: String,
    camera: MutableState<MapViewCamera> = rememberSaveableMapViewCamera(),
    mapControls: State<MapControls> = rememberSaveableMapControls(),
    mapOptions: MapLibreMapOptions? = null,
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
      camera,
      mapControls,
      mapOptions = mapOptions,
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
