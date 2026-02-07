package com.maplibre.compose.surface

import android.graphics.Point
import android.graphics.PointF
import kotlin.math.ln
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.maps.MapLibreMap

class MapSurfaceGestureCallback(
    private val map: MapLibreMap,
    private val flingDurationMs: Int = DEFAULT_FLING_DURATION_MS,
    private val flingVelocityFactor: Float = DEFAULT_FLING_VELOCITY_FACTOR
) : SurfaceGestureCallback {
  companion object {
    /** Default duration for fling animations in milliseconds. */
    const val DEFAULT_FLING_DURATION_MS = 300

    /** Factor to convert fling velocity to scroll distance. */
    const val DEFAULT_FLING_VELOCITY_FACTOR = 0.1f
  }

  override fun onScroll(distanceX: Float, distanceY: Float) {
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

  override fun onFling(velocityX: Float, velocityY: Float) {
    val currentTarget = map.cameraPosition.target ?: return
    val projection = map.projection

    val scrollX = -velocityX * flingVelocityFactor
    val scrollY = -velocityY * flingVelocityFactor

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

    map.easeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), flingDurationMs)
  }

  override fun onScale(focusX: Float, focusY: Float, scaleFactor: Float) {
    super.onScale(focusX, focusY, scaleFactor)

    // Convert scale factor to zoom delta using logarithm base 2
    // scaleFactor of 2.0 = +1 zoom level, 0.5 = -1 zoom level
    val zoomDelta = ln(scaleFactor.toDouble()) / ln(2.0)
    val focusPoint = Point(focusX.toInt(), focusY.toInt())
    map.moveCamera(CameraUpdateFactory.zoomBy(zoomDelta, focusPoint))
  }
}
