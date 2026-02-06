package com.maplibre.compose.camera

import android.graphics.Point
import android.graphics.PointF
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import com.maplibre.compose.ramani.MapApplier
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.maps.MapLibreMap

/**
 * A gesture handler that binds Android Auto surface callbacks to map camera movements.
 *
 * This handler translates raw screen-coordinate gestures (scroll, fling, scale) into map camera
 * updates using MapLibre's CameraUpdateFactory. The camera state is automatically synchronized back
 * to the MutableState via the existing onCameraIdle listener in MapCameraNode.
 *
 * Usage:
 * ```kotlin
 * class MyScreen(carContext: CarContext) : ComposableScreen(
 *     carContext,
 *     onScroll = { dx, dy -> gestureHandler?.onScroll(dx, dy) },
 *     onFling = { vx, vy -> gestureHandler?.onFling(vx, vy) },
 *     onScale = { fx, fy, sf -> gestureHandler?.onScale(fx, fy, sf) },
 * ) {
 *     private var gestureHandler: MapGestureHandler? = null
 *
 *     @Composable
 *     override fun content() {
 *         val camera = rememberSaveableMapViewCamera(...)
 *         MapView(..., camera = camera) {
 *             gestureHandler = rememberMapGestureHandler()
 *         }
 *     }
 * }
 * ```
 */
class MapGestureHandler internal constructor(private val map: MapLibreMap) {
  companion object {
    /** Default duration for fling animations in milliseconds. */
    const val DEFAULT_FLING_DURATION_MS = 300

    /** Factor to convert fling velocity to scroll distance. */
    const val DEFAULT_FLING_VELOCITY_FACTOR = 0.1f
  }

  fun onScroll(distanceX: Float, distanceY: Float) {
    val currentTarget = map.cameraPosition.target ?: return
    val projection = map.projection

    val currentScreenPosition = projection.toScreenLocation(currentTarget)
    val newScreenPosition =
        PointF(currentScreenPosition.x + distanceX, currentScreenPosition.y + distanceY)

    val newTarget = projection.fromScreenLocation(newScreenPosition)

    val cameraPosition =
        CameraPosition.Builder()
            .target(newTarget)
            .zoom(map.cameraPosition.zoom)
            .tilt(map.cameraPosition.tilt)
            .bearing(map.cameraPosition.bearing)
            .build()

    map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
  }

  fun onFling(
      velocityX: Float,
      velocityY: Float,
      durationMs: Int = DEFAULT_FLING_DURATION_MS,
      velocityFactor: Float = DEFAULT_FLING_VELOCITY_FACTOR
  ) {
    val currentTarget = map.cameraPosition.target ?: return
    val projection = map.projection

    val scrollX = -velocityX * velocityFactor
    val scrollY = -velocityY * velocityFactor

    val currentScreenPosition = projection.toScreenLocation(currentTarget)
    val newScreenPosition =
        PointF(currentScreenPosition.x + scrollX, currentScreenPosition.y + scrollY)

    val newTarget = projection.fromScreenLocation(newScreenPosition)

    val cameraPosition =
        CameraPosition.Builder()
            .target(newTarget)
            .zoom(map.cameraPosition.zoom)
            .tilt(map.cameraPosition.tilt)
            .bearing(map.cameraPosition.bearing)
            .build()

    map.easeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), durationMs)
  }

  fun onScale(focusX: Float, focusY: Float, scaleFactor: Float) {
    val zoomDelta = kotlin.math.ln(scaleFactor.toDouble()) / kotlin.math.ln(2.0)
    val focusPoint = Point(focusX.toInt(), focusY.toInt())
    map.moveCamera(CameraUpdateFactory.zoomBy(zoomDelta, focusPoint))
  }
}

/**
 * Remember a [MapGestureHandler] that binds Android Auto surface gestures to map camera movements.
 *
 * This must be called within a MapView composition context where the MapApplier is available. The
 * camera state will be automatically synchronized via the existing onCameraIdle listener in
 * MapCameraNode.
 *
 * @return A [MapGestureHandler] instance that can be used to handle gesture callbacks.
 */
@Composable
fun rememberMapGestureHandler(): MapGestureHandler {
  val mapApplier = currentComposer.applier as MapApplier
  return remember(mapApplier.map) { MapGestureHandler(mapApplier.map) }
}
