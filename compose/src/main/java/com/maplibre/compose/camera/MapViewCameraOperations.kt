package com.maplibre.compose.camera

import kotlin.math.roundToInt

/**
 * Set the camera's zoom level.
 *
 * This function will ensure that the zoom level is within the min and max zoom levels.
 *
 * ```kotlin
 * val camera = remember { mutableStateOf(MapViewCamera()) }
 * camera.value = camera.value.setZoom(10.0)
 * ```
 *
 * @param zoom the new zoom level.
 * @return the updated camera. You must set your camera.value to this return value.
 */
fun MapViewCamera.setZoom(zoom: Double): MapViewCamera {
  return this.copy(zoom = zoom)
}

/**
 * Increment the camera's zoom level.
 *
 * This function will ensure that the zoom level is within the min and max zoom levels.
 *
 * ```kotlin
 * val camera = remember { mutableStateOf(MapViewCamera()) }
 * camera.value = camera.value.incrementZoom(1.0)
 * ```
 *
 * @param increment the amount to increment the zoom level by.
 * @param rounded by default, the zoom level is rounded to the nearest integer to reset from manual
 *   control. For fractional increments, consider setting this to false.
 * @return the updated camera. You must set your camera.value to this return value.
 */
fun MapViewCamera.incrementZoom(increment: Double, rounded: Boolean = true): MapViewCamera {
  val currentZoom = if (rounded) this.zoom.roundToInt().toDouble() else this.zoom
  return this.setZoom(currentZoom + increment)
}
