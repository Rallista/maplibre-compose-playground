package com.maplibre.compose.car

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.MainThread
import androidx.car.app.CarContext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.maplibre.compose.ramani.MapLibreComposable
import com.maplibre.compose.ramani.newComposition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.maplibre.android.MapLibre
import org.maplibre.android.location.engine.LocationEngine
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

/**
 * Container for managing a MapView instance for Android Auto.
 *
 * This class handles the lifecycle of a MapView and provides it in a format that can be rendered
 * via VirtualDisplay/Presentation on Android Auto.
 */
class CarMapViewContainer(
    private val carContext: CarContext,
    private val styleUrl: String,
    private val locationEngine: LocationEngine? = null,
    private val compositionContext: CompositionContext? = null,
    private val onMapReady: ((MapLibreMap, Style) -> Unit)? = null,
    private val content: (@Composable @MapLibreComposable () -> Unit)? = null
) : DefaultLifecycleObserver {

  private var mapView: MapView? = null
  private var mapContainer: FrameLayout? = null
  private var mapLibreMap: MapLibreMap? = null

  /**
   * Sets up the MapView and returns a View that can be rendered via Presentation. Must be called on
   * the main thread.
   */
  @MainThread
  fun setupMap(): View {
    // Initialize MapLibre
    MapLibre.getInstance(carContext)

    // Create the MapView
    val map =
        MapView(carContext).apply {
          onCreate(Bundle())
          onStart()
          onResume()
        }

    mapView = map

    // Set up the map asynchronously
    map.getMapAsync { maplibreMap ->
      this.mapLibreMap = maplibreMap

      maplibreMap.setStyle(Style.Builder().fromUri(styleUrl)) { style ->
        // Call the ready callback
        onMapReady?.invoke(maplibreMap, style)

        // Set up composable content if provided
        if (content != null && compositionContext != null) {
          CoroutineScope(Dispatchers.Main).launch {
            try {
              map.newComposition(compositionContext, style) { content.invoke() }
            } catch (e: Exception) {
              // Silently ignore composition errors during cleanup
            }
          }
        }
      }
    }

    // Create and return the container
    val container = createCarMapContainer(map)
    mapContainer = container
    return container
  }

  /** Cleans up the MapView resources. Must be called on the main thread. */
  @MainThread
  fun cleanUpMap() {
    mapView?.let { map ->
      mapContainer?.removeView(map)
      map.onPause()
      map.onStop()
      map.onDestroy()
    }
    mapView = null
    mapContainer = null
    mapLibreMap = null
  }

  /** Gets the current MapLibreMap instance, if available. */
  fun getMapLibreMap(): MapLibreMap? = mapLibreMap

  override fun onDestroy(owner: LifecycleOwner) {
    cleanUpMap()
  }
}

/** Builder for creating a CarMapViewContainer with a fluent API. */
class CarMapViewContainerBuilder(private val carContext: CarContext) {
  private var styleUrl: String = "https://demotiles.maplibre.org/style.json"
  private var locationEngine: LocationEngine? = null
  private var compositionContext: CompositionContext? = null
  private var onMapReady: ((MapLibreMap, Style) -> Unit)? = null
  private var content: (@Composable @MapLibreComposable () -> Unit)? = null

  fun styleUrl(url: String) = apply { this.styleUrl = url }

  fun locationEngine(engine: LocationEngine) = apply { this.locationEngine = engine }

  fun compositionContext(context: CompositionContext) = apply { this.compositionContext = context }

  fun onMapReady(callback: (MapLibreMap, Style) -> Unit) = apply { this.onMapReady = callback }

  fun content(content: @Composable @MapLibreComposable () -> Unit) = apply {
    this.content = content
  }

  fun build(): CarMapViewContainer {
    return CarMapViewContainer(
        carContext = carContext,
        styleUrl = styleUrl,
        locationEngine = locationEngine,
        compositionContext = compositionContext,
        onMapReady = onMapReady,
        content = content)
  }
}
