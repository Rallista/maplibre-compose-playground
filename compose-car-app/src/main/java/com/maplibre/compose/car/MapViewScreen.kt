package com.maplibre.compose.car

import android.location.Location
import androidx.car.app.AppManager
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Template
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.maplibre.compose.MapView
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.ramani.LocationRequestProperties
import com.maplibre.compose.ramani.LocationStyling
import com.maplibre.compose.ramani.MapLibreComposable
import com.maplibre.compose.rememberSaveableMapControls
import com.maplibre.compose.settings.MapControls
import org.maplibre.android.location.engine.LocationEngine
import org.maplibre.android.maps.Style

abstract class MapViewScreen(
  carContext: CarContext,
  protected val styleUrl: String,
  protected val camera: MutableState<MapViewCamera>,
  protected val locationEngine: LocationEngine? = null,
  protected val locationRequestProperties: LocationRequestProperties = LocationRequestProperties.Default,
  protected val locationStyling: LocationStyling = LocationStyling.Default,
  protected val userLocation: MutableState<Location>? = null,
  protected val onMapReadyCallback: ((Style) -> Unit)? = null,
  protected val content: (@Composable @MapLibreComposable () -> Unit)? = null
) : Screen(carContext) {

  private val surfaceCallback: ComposeViewSurfaceCallback = ComposeViewSurfaceCallback(
    context = carContext,
    content = {
      MapView(
        modifier = Modifier,
        styleUrl = styleUrl,
        camera = camera,
        locationEngine = locationEngine,
        locationRequestProperties = locationRequestProperties,
        locationStyling = locationStyling,
        userLocation = userLocation,
        onMapReadyCallback = onMapReadyCallback,
        content = content
      )
    }
  )

  init {
    carContext
      .getCarService(AppManager::class.java)
      .setSurfaceCallback(surfaceCallback)
  }

  // Abstract method that subclasses must implement to provide their template
  abstract override fun onGetTemplate(): Template
}