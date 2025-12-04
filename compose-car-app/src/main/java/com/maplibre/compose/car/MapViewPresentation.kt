package com.maplibre.compose.car

import android.app.Presentation
import android.content.Context
import android.hardware.display.VirtualDisplay
import android.os.Bundle
import android.view.Display
import android.view.View
import android.widget.FrameLayout
import androidx.car.app.SurfaceCallback
import androidx.car.app.SurfaceContainer
import org.maplibre.android.maps.MapView

/**
 * Presentation that displays a MapView on a VirtualDisplay for Android Auto.
 *
 * Based on the high-performance rendering approach from:
 * https://github.com/maplibre/MapLibre-Android-Auto-Sample/pull/13
 */
internal class MapViewPresentation(
    context: Context,
    display: Display,
    private val mapViewProvider: () -> View
) : Presentation(context, display) {

  private var contentView: View? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val view = mapViewProvider()
    contentView = view
    setContentView(view)
  }

  override fun onStop() {
    super.onStop()
    contentView = null
  }
}

/** Surface callback that manages the VirtualDisplay and Presentation for Android Auto. */
class MapSurfaceCallback(
    private val context: Context,
    private val mapViewProvider: () -> View,
    private val onCleanup: () -> Unit = {}
) : SurfaceCallback {

  private var virtualDisplay: VirtualDisplay? = null
  private var presentation: MapViewPresentation? = null

  override fun onSurfaceAvailable(surfaceContainer: SurfaceContainer) {
    val displayManager =
        context.getSystemService(Context.DISPLAY_SERVICE) as android.hardware.display.DisplayManager

    // Create a VirtualDisplay that renders to the Android Auto surface
    virtualDisplay =
        displayManager.createVirtualDisplay(
            "MapLibreDisplay",
            surfaceContainer.width,
            surfaceContainer.height,
            surfaceContainer.dpi,
            surfaceContainer.surface,
            0)

    virtualDisplay?.display?.let { display ->
      // Create and show the Presentation with the MapView
      presentation = MapViewPresentation(context, display, mapViewProvider).apply { show() }
    }
  }

  override fun onSurfaceDestroyed(surfaceContainer: SurfaceContainer) {
    presentation?.dismiss()
    presentation = null
    virtualDisplay?.release()
    virtualDisplay = null
    onCleanup()
  }

  override fun onVisibleAreaChanged(visibleArea: android.graphics.Rect) {
    // Handle visible area changes if needed
  }

  override fun onStableAreaChanged(stableArea: android.graphics.Rect) {
    // Handle stable area changes if needed
  }

  override fun onScroll(distanceX: Float, distanceY: Float) {
    // Handle scroll if needed
  }

  override fun onFling(velocityX: Float, velocityY: Float) {
    // Handle fling if needed
  }

  override fun onScale(focusX: Float, focusY: Float, scaleFactor: Float) {
    // Handle scale if needed
  }
}

/**
 * Creates a container for the MapView to be used with Android Auto. Returns a FrameLayout
 * containing the MapView that can be rendered via Presentation.
 */
fun createCarMapContainer(mapView: MapView): FrameLayout {
  return FrameLayout(mapView.context).apply {
    addView(
        mapView,
        FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
  }
}
