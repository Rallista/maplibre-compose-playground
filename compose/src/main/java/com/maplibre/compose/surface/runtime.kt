package com.maplibre.compose.surface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import com.maplibre.compose.ramani.MapApplier
import com.maplibre.compose.surface.MapSurfaceGestureCallback.Companion.DEFAULT_FLING_DURATION_MS
import com.maplibre.compose.surface.MapSurfaceGestureCallback.Companion.DEFAULT_FLING_VELOCITY_FACTOR

@Composable
fun rememberMapSurfaceGestureCallback(
    flingDurationMs: Int = DEFAULT_FLING_DURATION_MS,
    flingVelocityFactor: Float = DEFAULT_FLING_VELOCITY_FACTOR,
    applyCallback: (callback: MapSurfaceGestureCallback) -> Unit = {}
) {
  val mapApplier = currentComposer.applier as MapApplier
  val callback =
      remember(mapApplier.map) {
        MapSurfaceGestureCallback(
            map = mapApplier.map,
            flingDurationMs = flingDurationMs,
            flingVelocityFactor = flingVelocityFactor)
      }
  applyCallback(callback)
}
